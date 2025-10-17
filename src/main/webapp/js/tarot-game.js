// 塔罗解惑游戏JavaScript - 优化版本
(function () {
    'use strict';

    // 私有变量
    let allCards = [];
    let droppedCards = {}; // 存储拖拽到放置区的卡牌
    let draggedCard = null; // 当前被拖拽的卡牌
    let isLoading = false; // 加载状态标志
    let currentFetchController = null; // 用于取消fetch请求的控制器

    // 日志级别控制 - 便于生产环境调试
    const LOG_LEVEL = { NONE: 0, ERROR: 1, WARN: 2, INFO: 3, DEBUG: 4 };
    const CURRENT_LOG_LEVEL = LOG_LEVEL.ERROR; // 生产环境使用ERROR级别

    // 配置常量
    const CACHE_KEY = 'tarot_cards_cache';
    const CACHE_EXPIRY_KEY = 'tarot_cards_cache_expiry';
    const CACHE_DURATION = 60 * 60 * 1000; // 60分钟缓存
    const DISPLAY_CARD_COUNT = 20;
    const THROTTLE_DELAY = 16; // 60fps拖拽流畅度
    const CARD_WIDTH = 70;
    const OVERLAP_RATIO = 0.5;
    const VISIBLE_WIDTH = CARD_WIDTH * (1 - OVERLAP_RATIO);
    const TOTAL_DISPLAY_WIDTH = VISIBLE_WIDTH * DISPLAY_CARD_COUNT;

    // 获取DOM元素的辅助函数
    const getElement = (id) => {
        const element = document.getElementById(id);
        if (!element) {
            log(LOG_LEVEL.WARN, `元素 ${id} 未找到`);
        }
        return element;
    };

    // 增强的日志函数，支持不同级别
    const log = function (level, message) {
        if (level > CURRENT_LOG_LEVEL) return;

        const logLevelNames = ['', 'ERROR', 'WARN', 'INFO', 'DEBUG'];
        const prefix = `[塔罗游戏][${logLevelNames[level]}]`;

        switch (level) {
            case LOG_LEVEL.ERROR:
                console.error(`${prefix} ${message}`);
                break;
            case LOG_LEVEL.WARN:
                console.warn(`${prefix} ${message}`);
                break;
            case LOG_LEVEL.INFO:
            case LOG_LEVEL.DEBUG:
                console.log(`${prefix} ${message}`);
                break;
        }
    };

    // 初始化阶段
    document.addEventListener('DOMContentLoaded', function () {
        showLoading(true);
        loadTarotCards().then(() => {
            startGame();
            showLoading(false);
        }).catch(error => {
            showLoading(false);
            showErrorMessage('游戏初始化失败，请刷新页面重试');
            log(LOG_LEVEL.ERROR, `初始化失败: ${error.message || error}`);
        });

        // 为再次占卜按钮绑定事件监听
        const againBtn = getElement('againBtn');
        if (againBtn) {
            againBtn.addEventListener('click', resetGame);
        }

        // 页面卸载时取消未完成的请求
        window.addEventListener('beforeunload', function () {
            cancelCurrentFetch();
        });
    });

    // 统一的请求取消工具函数
    function fetchWithCancel(url, options = {}) {
        // 取消之前的请求
        cancelCurrentFetch();

        // 创建新的AbortController
        currentFetchController = new AbortController();

        return fetch(url, {
            ...options,
            signal: currentFetchController.signal
        }).finally(() => {
            currentFetchController = null;
        });
    }

    // 取消当前fetch请求
    function cancelCurrentFetch() {
        if (currentFetchController) {
            currentFetchController.abort();
            currentFetchController = null;
            log(LOG_LEVEL.DEBUG, '取消了当前的fetch请求');
        }
    }

    // 简化节流函数
    function throttle(func, delay) {
        let inThrottle;

        return function () {
            const args = arguments;
            const context = this;

            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, delay);
            }
        }
    }

    // 显示加载指示器
    function showLoading(show, message) {
        const loadingIndicator = getElement('loadingIndicator');
        const loadingMessage = getElement('loadingMessage');

        if (!loadingIndicator) {
            log(LOG_LEVEL.WARN, '加载指示器元素未找到');
            return;
        }

        if (loadingMessage) {
            loadingMessage.textContent = message || '正在加载...';
        }

        if (show) {
            loadingIndicator.style.display = 'flex';
            loadingIndicator.classList.add('loading-active');
        } else {
            loadingIndicator.classList.remove('loading-active');
            setTimeout(() => {
                if (loadingIndicator) {
                    loadingIndicator.style.display = 'none';
                }
            }, 300);
        }
        isLoading = show;
    }

    // 统一的错误处理函数
    function showErrorMessage(message) {
        const errorMessage = getElement('errorMessage');
        if (errorMessage) {
            errorMessage.textContent = message;
            errorMessage.classList.add('show');

            // 3秒后自动隐藏
            setTimeout(() => {
                if (errorMessage) {
                    errorMessage.classList.remove('show');
                }
            }, 3000);
        } else {
            // 备用方案：使用alert
            alert(message);
        }

        log(LOG_LEVEL.ERROR, `错误消息显示: ${message}`);
    }

    // 简化缓存管理函数
    function getCachedCards() {
        try {
            const cached = localStorage.getItem(CACHE_KEY);
            const expiry = localStorage.getItem(CACHE_EXPIRY_KEY);

            if (cached && expiry && Date.now() < parseInt(expiry)) {
                const cards = JSON.parse(cached);
                if (cards && cards.length > 0) {
                    log(LOG_LEVEL.INFO, '从缓存加载' + cards.length + '张卡牌');
                    return cards;
                }
            }
        } catch (error) {
            log(LOG_LEVEL.WARN, '缓存读取失败:', error);
        }
        return null;
    }

    function setCachedCards(cards) {
        try {
            if (!cards || cards.length === 0) {
                return;
            }

            localStorage.setItem(CACHE_KEY, JSON.stringify(cards));
            localStorage.setItem(CACHE_EXPIRY_KEY, (Date.now() + CACHE_DURATION).toString());
            log(LOG_LEVEL.INFO, '缓存了' + cards.length + '张卡牌');
        } catch (error) {
            log(LOG_LEVEL.WARN, '缓存存储失败:', error);
        }
    }

    // 加载塔罗牌数据
    async function loadTarotCards() {
        try {
            // 先检查缓存
            const cachedCards = getCachedCards();
            if (cachedCards && cachedCards.length > 0) {
                allCards = cachedCards;
                return;
            }

            // 使用后端API获取塔罗牌数据
            // 获取当前应用的上下文路径
            const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)) || '';
            const response = await fetchWithCancel(`${contextPath}/api/cards`);

            if (response.ok) {
                const data = await response.json();

                if (data && data.success && data.major_arcana && data.minor_arcana) {
                    const majorArcanaCards = Array.isArray(data.major_arcana) ? data.major_arcana : [];
                    const minorArcanaCards = [];

                    // 合并所有小阿卡那牌
                    ['wands', 'cups', 'swords', 'pentacles'].forEach(suit => {
                        if (data.minor_arcana[suit]) {
                            minorArcanaCards.push(...data.minor_arcana[suit]);
                        }
                    });

                    // 合并大小阿卡那牌
                    allCards = [...majorArcanaCards, ...minorArcanaCards];

                    // 缓存数据
                    if (allCards.length >= 22) {
                        setCachedCards(allCards);
                    }
                } else {
                    showErrorMessage('获取的卡牌数据格式无效');
                    throw new Error('卡牌数据格式无效');
                }
            } else {
                showErrorMessage('无法从服务器加载塔罗牌数据');
                throw new Error('服务器数据加载失败');
            }
        } catch (error) {
            if (error.name !== 'AbortError') {
                showErrorMessage('加载塔罗牌数据失败，请刷新页面重试');
                throw error;
            }
        }
    }

    // 洗牌函数
    function shuffleCards(cards) {
        const shuffled = [...cards];
        for (let i = shuffled.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
        }
        return shuffled;
    }

    // 优化的卡牌数据更新函数 - 批量处理
    function updateCardData(cardElement, card) {
        // 检查是否需要更新，避免不必要的DOM操作
        if (cardElement.dataset.cardId === card.id) {
            return; // 数据相同，跳过更新
        }

        // 批量更新所有属性
        Object.assign(cardElement.dataset, {
            cardId: card.id,
            cardName: card.name,
            uprightMeaning: card.upright_meaning,
            reversedMeaning: card.reversed_meaning,
            description: card.description || ''
        });
    }

    // 简化开始游戏
    function startGame() {
        droppedCards = {};

        if (!allCards || allCards.length === 0) {
            showErrorMessage('无法获取卡牌数据，请确保后端服务正常运行。');
            return;
        }

        allCards = shuffleCards(allCards);
        generateCardDeck();
        initializeDropZones();
    }

    // 生成卡牌堆
    function generateCardDeck() {
        const cardDeck = getElement('cardDeck');

        if (!cardDeck) {
            log(LOG_LEVEL.ERROR, '卡牌堆元素未找到');
            return;
        }

        if (!allCards || allCards.length === 0) {
            showErrorMessage('没有可用的卡牌数据，请刷新页面重试');
            return;
        }

        // 清空现有卡牌
        cardDeck.innerHTML = '';

        // 直接取洗牌后的前DISPLAY_CARD_COUNT张卡牌
        const selectedCards = allCards.slice(0, DISPLAY_CARD_COUNT);

        // 使用DocumentFragment批量DOM操作
        const fragment = document.createDocumentFragment();

        // 预计算所有卡牌位置
        const baseX = -TOTAL_DISPLAY_WIDTH / 2;

        for (let i = 0; i < selectedCards.length; i++) {
            const card = selectedCards[i];
            const cardElement = document.createElement('div');
            cardElement.className = 'card-back';

            // 设置卡牌数据
            updateCardData(cardElement, card);

            // 计算水平位置
            const x = baseX + (i * VISIBLE_WIDTH);
            const translateX = `calc(50% + ${x}px - 50%)`;

            // 设置样式
            Object.assign(cardElement.style, {
                position: 'absolute',
                left: '50%',
                bottom: '20px', // 减小底部间距
                transform: `translateX(${translateX}) rotate(0deg)`,
                zIndex: i,
                animationDelay: (i * 0.05) + 's'
            });

            addDragEvents(cardElement);
            fragment.appendChild(cardElement);
        }

        // 一次性添加所有卡牌到DOM
        cardDeck.appendChild(fragment);
        updateSelectedCount();
    }

    // 精简拖拽功能实现
    function addDragEvents(cardElement) {
        let isDragging = false;
        let originalParent, originalNextSibling, originalStyles = {};

        cardElement.addEventListener('mousedown', function (e) {
            e.preventDefault();

            if (isDragging) {
                return;
            }

            isDragging = true;
            draggedCard = cardElement;

            // 保存原始状态
            originalParent = cardElement.parentNode;
            originalNextSibling = cardElement.nextSibling;
            const rect = cardElement.getBoundingClientRect();
            const computedStyle = getComputedStyle(cardElement);

            originalStyles = {
                position: cardElement.style.position || 'absolute',
                left: cardElement.style.left || computedStyle.left,
                top: cardElement.style.top || computedStyle.top,
                bottom: cardElement.style.bottom || computedStyle.bottom,
                transform: cardElement.style.transform || computedStyle.transform,
                zIndex: cardElement.style.zIndex || computedStyle.zIndex,
                transition: cardElement.style.transition || computedStyle.transition
            };

            // 设置拖拽样式
            cardElement.classList.add('dragging');
            Object.assign(cardElement.style, {
                position: 'fixed',
                left: rect.left + 'px',
                top: rect.top + 'px',
                bottom: 'auto',
                transform: 'scale(1.1)',
                zIndex: '1000',
                transition: 'none',
                pointerEvents: 'none'
            });

            // 移动到body
            document.body.appendChild(cardElement);

            // 绑定事件
            const handleMouseMove = throttle(function (e) {
                if (!isDragging || draggedCard !== cardElement) return;

                const rect = cardElement.getBoundingClientRect();
                const offsetX = rect.width / 2;
                const offsetY = rect.height / 2;

                // 计算新位置，添加边界约束（使用相对约束而非固定尺寸）
                let x = e.clientX - offsetX;
                let y = e.clientY - offsetY;

                // 使用视口尺寸进行约束，避免硬编码
                x = Math.max(0, Math.min(x, window.innerWidth - rect.width));
                y = Math.max(0, Math.min(y, window.innerHeight - rect.height));

                cardElement.style.transform = `translate(${x}px, ${y}px) scale(1.1)`;
                cardElement.style.left = '0px';
                cardElement.style.top = '0px';

                const dropZone = getDropZoneUnder(e.clientX, e.clientY);
                highlightDropZone(dropZone);
            }, THROTTLE_DELAY);

            const handleMouseUp = function (e) {
                if (!isDragging || draggedCard !== cardElement) return;

                isDragging = false;

                const dropZone = getDropZoneUnder(e.clientX, e.clientY);

                if (dropZone && !dropZone.classList.contains('filled')) {
                    dropCardInZone(cardElement, dropZone, originalStyles);
                } else {
                    cardElement.style.pointerEvents = '';
                    resetCardPosition(cardElement, originalParent, originalNextSibling, originalStyles);
                }

                // 清理
                document.removeEventListener('mousemove', handleMouseMove);
                document.removeEventListener('mouseup', handleMouseUp);
                clearDropZoneHighlights();
                draggedCard = null;
            };

            document.addEventListener('mousemove', handleMouseMove);
            document.addEventListener('mouseup', handleMouseUp);
        });
    }

    // 简化获取鼠标下方的放置区域
    function getDropZoneUnder(x, y) {
        if (draggedCard) {
            draggedCard.style.pointerEvents = 'none';
        }

        const elements = document.elementsFromPoint(x, y);

        for (let element of elements) {
            if (element.classList.contains('drop-zone')) {
                return element;
            }
        }

        return null;
    }

    // 高亮放置区域
    function highlightDropZone(dropZone) {
        clearDropZoneHighlights();
        if (dropZone && !dropZone.classList.contains('filled')) {
            dropZone.classList.add('drag-over');
        }
    }

    // 清除所有放置区域高亮
    function clearDropZoneHighlights() {
        document.querySelectorAll('.drop-zone').forEach(zone => {
            zone.classList.remove('drag-over');
        });
    }

    // 简化卡牌放置逻辑
    function dropCardInZone(cardElement, dropZone, originalStyles) {
        // 防止重复放置
        if (dropZone.classList.contains('filled')) {
            log(LOG_LEVEL.ERROR, '放置区域已被填充');
            return;
        }

        const position = parseInt(dropZone.dataset.position);
        const cardId = cardElement.dataset.cardId;
        const cardName = cardElement.dataset.cardName;

        // 随机决定正位或逆位
        const isReversed = Math.random() > 0.7;

        // 存储卡牌信息
        droppedCards[position] = {
            cardId,
            cardName,
            uprightMeaning: cardElement.dataset.uprightMeaning,
            reversedMeaning: cardElement.dataset.reversedMeaning,
            description: cardElement.dataset.description,
            isReversed: isReversed
        };

        // 隐藏原始卡牌
        cardElement.style.display = 'none';

        // 创建放置后卡牌元素
        const droppedCard = document.createElement('div');
        droppedCard.className = 'dropped-card-face dropped-card-base';
        droppedCard.dataset.position = position;

        // 构建卡牌内容，根据isReversed状态添加reversed类
        droppedCard.innerHTML = `
            <div class="card-face-content${isReversed ? ' reversed' : ''}">
                <div class="card-name">${cardName}</div>
                <div class="card-symbol">⭐</div>
            </div>
        `;

        dropZone.innerHTML = '';
        dropZone.appendChild(droppedCard);
        dropZone.classList.add('filled');

        updateSelectedCount();

        // 严格检查是否所有三个位置都有卡牌
        const hasAllPositions = [0, 1, 2].every(pos => droppedCards[pos] !== undefined);
        if (hasAllPositions) {
            setTimeout(showReading, 1000);
        }
    }

    // 重置卡片位置 - 改进还原逻辑
    function resetCardPosition(cardElement, originalParent, originalNextSibling, originalStyles) {
        cardElement.classList.remove('dragging');

        // 平滑过渡回原位置
        cardElement.style.transition = 'all 0.3s ease';

        // 恢复原始样式，确保准确还原
        Object.assign(cardElement.style, {
            position: originalStyles.position,
            left: originalStyles.left,
            top: originalStyles.top,
            bottom: originalStyles.bottom,
            transform: originalStyles.transform,
            zIndex: originalStyles.zIndex,
            pointerEvents: ''
        });

        // 将卡片放回原始位置
        if (originalNextSibling && originalNextSibling.parentNode === originalParent) {
            originalParent.insertBefore(cardElement, originalNextSibling);
        } else {
            originalParent.appendChild(cardElement);
        }

        // 清除过渡效果，避免后续操作受影响
        setTimeout(() => {
            if (cardElement && cardElement.style) {
                cardElement.style.transition = originalStyles.transition || '';
            }
        }, 300);
    }

    // 初始化放置区域
    function initializeDropZones() {
        const dropZones = document.querySelectorAll('.drop-zone');
        const labels = ['过去/根源', '现在/情况', '未来/建议'];

        dropZones.forEach((zone, index) => {
            const position = zone.dataset.position;
            const label = labels[index] || `第${parseInt(position) + 1}张牌`;
            zone.innerHTML = `
                <div class="drop-zone-label">${label}</div>
                <div class="drop-zone-placeholder">拖拽卡牌到此</div>
            `;
            zone.classList.remove('filled');
        });
    }

    // 更新已选卡牌数量
    function updateSelectedCount() {
        const selectedCount = Object.keys(droppedCards).length;
        const selectedCountElement = getElement('selectedCount');
        if (selectedCountElement) {
            selectedCountElement.textContent = `${selectedCount}`;
        }
    }

    // 简化解读结果显示
    function showReading() {
        // 严格验证：只有当确切选择了3张牌时才进行解读
        const validCardCount = 3;
        const actualCardCount = Object.keys(droppedCards).filter(pos => droppedCards[pos] !== undefined).length;
        
        if (actualCardCount !== validCardCount) {
            log(LOG_LEVEL.WARN, `卡牌数量不正确: 需要${validCardCount}张，实际${actualCardCount}张`);
            showErrorMessage(`请选择${validCardCount}张卡牌进行解读`);
            return;
        }

        // 准备UI元素
        const resultSection = getElement('resultSection');
        const readingContent = getElement('readingContent');
        const selectedCards = getElement('selectedCards');

        if (!resultSection || !readingContent || !selectedCards) {
            log(LOG_LEVEL.ERROR, '结果展示区域的必要元素未找到');
            return;
        }

        // 更新UI状态
        resultSection.style.display = 'block';
        readingContent.innerHTML = '<div class="loading">正在生成解读...</div>';
        selectedCards.innerHTML = '';

        const cardSelectionSection = getElement('cardSelectionSection');
        if (cardSelectionSection) {
            cardSelectionSection.style.display = 'none';
        }

        // 隐藏提示文字
        const instructionText = getElement('instructionText');
        if (instructionText) {
            instructionText.style.display = 'none';
        }

        // 在本地生成模拟解读结果，不发送HTTP请求
        setTimeout(() => {
            try {
                // 生成模拟解读数据
                const readingData = generateLocalReading();

                // 二次验证生成的数据
                if (!readingData.drawnCards || readingData.drawnCards.length !== validCardCount) {
                    throw new Error('生成的解读数据不完整');
                }

                // 更新解读内容
                const resultTitle = resultSection.querySelector('h2');
                if (resultTitle) {
                    resultTitle.textContent = readingData.spreadType || '解惑结果';
                }
                readingContent.innerHTML = readingData.overallReading;
                displaySelectedCards(readingData.drawnCards);
            } catch (error) {
                log(LOG_LEVEL.ERROR, '解读生成失败:', error);
                readingContent.innerHTML = '<div class="error">无法生成解读，请重试。</div>';
                // 重置到选择界面
                setTimeout(() => {
                    resetGame();
                }, 2000);
            }
        }, 500);
    }

    // 本地生成模拟解读结果
    function generateLocalReading() {
        const drawnCards = [];
        const positions = ['过去/根源', '现在/情况', '未来/建议'];
        const expectedPositions = [0, 1, 2]; // 期望的三个位置索引

        // 严格按预期位置顺序处理卡牌
        expectedPositions.forEach((positionIndex, index) => {
            const cardObj = droppedCards[positionIndex];
            
            if (cardObj) {
                // 确保类型匹配，将cardId转换为数字类型与allCards中的id比较
                let cardId = parseInt(cardObj.cardId);
                // 如果解析失败，保持原始字符串类型
                if (isNaN(cardId)) {
                    cardId = cardObj.cardId;
                }

                // 查找匹配的卡牌
                const card = allCards.find(c => c.id === cardId);

                if (card) {
                    drawnCards.push({
                        id: card.id,
                        name: card.name,
                        positionLabel: positions[index] || `位置 ${index + 1}`,
                        isReversed: cardObj.isReversed,
                        currentMeaning: cardObj.isReversed ? card.reversed_meaning : card.upright_meaning,
                        uprightMeaning: card.upright_meaning,
                        reversed_meaning: card.reversed_meaning,
                        description: card.description || ''
                    });
                } else {
                    // 如果找不到卡牌，添加一个占位符以保持数组长度
                    drawnCards.push({
                        id: null,
                        name: '未知卡牌',
                        positionLabel: positions[index] || `位置 ${index + 1}`,
                        isReversed: false,
                        currentMeaning: '无法获取卡牌解读',
                        uprightMeaning: '',
                        reversed_meaning: '',
                        description: ''
                    });
                }
            } else {
                // 如果位置上没有卡牌，添加占位符
                drawnCards.push({
                    id: null,
                    name: '未选择',
                    positionLabel: positions[index] || `位置 ${index + 1}`,
                    isReversed: false,
                    currentMeaning: '请在此位置选择一张卡牌',
                    uprightMeaning: '',
                    reversed_meaning: '',
                    description: ''
                });
            }
        });

        // 生成整体解读
        const overallReading = generateOverallReading(drawnCards);

        return {
            spreadType: '过去-现在-未来',
            overallReading: overallReading,
            drawnCards: drawnCards
        };
    }

    // 生成整体解读文本
    function generateOverallReading(cards) {
        if (!cards || cards.length === 0) {
            return '<p>请选择卡牌进行解读。</p>';
        }

        const intros = [
            '根据你选择的三张塔罗牌，我们可以看到你生活中的重要线索：',
            '从这组卡牌的排列中，我们能解读出关于你当前处境的重要信息：',
            '这三张卡牌揭示了你的过去、现在和未来的关键洞察：'
        ];

        const outro = '<p><br>请记住，塔罗牌是一种指引，最终的选择和行动取决于你自己。相信你的直觉，做出最适合自己的决定。</p>';

        // 随机选择一个引言
        const intro = intros[Math.floor(Math.random() * intros.length)];

        return `<p>${intro}</p>${outro}`;
    }

    // 显示选中的卡牌
    function displaySelectedCards(cards) {
        const selectedCards = getElement('selectedCards');
        if (!selectedCards) {
            log(LOG_LEVEL.ERROR, '选中卡牌容器未找到');
            return;
        }

        if (!cards || !cards.length) {
            return;
        }

        cards.forEach((card, index) => {
            // 卡牌图像
            const cardContainer = document.createElement('div');
            cardContainer.className = 'selected-card-container';

            const cardImage = document.createElement('div');
            // 根据isReversed状态添加reversed类
            cardImage.className = `selected-card-image${card.isReversed ? ' reversed' : ''}`;
            cardImage.innerHTML = `
                <div class="card-name">${card.name || '未知卡牌'}</div>
                <div class="card-position">${card.positionLabel || `位置 ${index + 1}`}</div>
                <div class="card-orientation">${card.isReversed ? '逆位' : '正位'}</div>
            `;

            cardContainer.appendChild(cardImage);
            selectedCards.appendChild(cardContainer);

            // 卡牌含义 - 直接添加到解读内容中
            const meaningElement = document.createElement('div');
            meaningElement.className = 'card-meaning';
            meaningElement.innerHTML = `
                <h4>${card.positionLabel || `位置 ${index + 1}`}: ${card.name || '未知卡牌'} (${card.isReversed ? '逆位' : '正位'})</h4>
                <p class="card-current-meaning">${card.currentMeaning || card.uprightMeaning || '暂无解读'}</p>
                <p class="card-description">${card.description || '暂无描述'}</p>
            `;

            const readingContent = getElement('readingContent');
            if (readingContent) {
                readingContent.appendChild(meaningElement);
            }
        });
    }

    // 优化的重置游戏函数 - 移除冗余逻辑
    function resetGame() {
        // 取消未完成的请求
        cancelCurrentFetch();

        // 清空选中卡牌记录
        droppedCards = {};

        // 重置放置区域
        initializeDropZones();

        // 更新UI状态
        const resultSection = getElement('resultSection');
        const cardSelectionSection = getElement('cardSelectionSection');

        if (resultSection) resultSection.style.display = 'none';
        if (cardSelectionSection) cardSelectionSection.style.display = 'block';

        // 重置游戏时显示提示文字
        const instructionText = getElement('instructionText');
        if (instructionText) {
            instructionText.style.display = 'block';
        }

        // 检查卡牌数据是否可用
        if (!allCards || allCards.length === 0) {
            showErrorMessage('卡牌数据不可用，请刷新页面重试');
            log(LOG_LEVEL.ERROR, '重置游戏失败：卡牌数据为空');
            return;
        }

        // 重新洗牌
        allCards = shuffleCards(allCards);

        // 获取卡牌堆元素
        const cardDeck = getElement('cardDeck');
        if (!cardDeck) {
            log(LOG_LEVEL.ERROR, '卡牌堆元素未找到');
            return;
        }

        // 获取现有卡牌元素
        const existingCards = cardDeck.querySelectorAll('.card-back');

        if (existingCards.length === DISPLAY_CARD_COUNT) {
            // 复用现有卡牌，只更新数据和样式
            const selectedCards = allCards.slice(0, DISPLAY_CARD_COUNT);

            // 批量更新卡牌数据，减少DOM操作
            existingCards.forEach((cardElement, i) => {
                const card = selectedCards[i];
                if (card) {
                    // 使用统一函数更新数据
                    updateCardData(cardElement, card);

                    // 重置样式和类名
                    cardElement.style.display = 'block';
                    cardElement.classList.remove('dragging');

                    // 更新特殊卡牌类
                    if (i % 7 === 0) {
                        cardElement.classList.add('special-card');
                    } else {
                        cardElement.classList.remove('special-card');
                    }
                }
            });
        } else {
            // 首次加载或卡牌数量不匹配，重新生成
            generateCardDeck();
        }

        // 更新选中数量
        updateSelectedCount();
    }

    // 仅对全局必需的函数进行暴露，用于外部调用（如果需要）
    window.resetTarotGame = resetGame;

})(); // IIFE 结束
