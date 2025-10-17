package tarot.service;

import tarot.model.TarotCard;
import tarot.model.CardReading;
import tarot.data.TarotCardData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 塔罗牌游戏服务类
 */
public class TarotGameService {

    private List<TarotCard> tarotDeck;
    private Random random;
    private static final Logger logger = LoggerFactory.getLogger(TarotGameService.class);

    public TarotGameService() {
        this.random = new Random();
        this.tarotDeck = new ArrayList<>();
        loadTarotCards();

        // 记录服务初始化信息
        logger.info("TarotGameService 初始化完成");
        logger.info("加载塔罗牌数量: {}", tarotDeck.size());
    }

    /**
     * 从TarotCardData类加载塔罗牌数据
     */
    private void loadTarotCards() {
        try {
            logger.info("开始加载塔罗牌数据...");

            // 从TarotCardData类加载大阿卡纳牌
            List<TarotCard> majorArcana = TarotCardData.getMajorArcana();
            tarotDeck.addAll(majorArcana);
            logger.info("已加载大阿卡纳牌数量: {}", majorArcana.size());

            // 从TarotCardData类加载小阿卡纳牌
            Map<String, List<TarotCard>> minorArcana = TarotCardData.getMinorArcana();
            int minorArcanaCount = 0;
            
            // 加载各花色的小阿卡纳牌
            for (String suit : Arrays.asList("wands", "cups", "swords", "pentacles")) {
                List<TarotCard> suitCards = minorArcana.get(suit);
                if (suitCards != null) {
                    tarotDeck.addAll(suitCards);
                    minorArcanaCount += suitCards.size();
                    logger.debug("已加载{}系列卡牌数量: {}", suit, suitCards.size());
                }
            }
            
            logger.info("已加载小阿卡纳牌数量: {}", minorArcanaCount);
            logger.info("塔罗牌加载完成，共 {} 张牌", tarotDeck.size());

        } catch (Exception e) {
            logger.error("加载塔罗牌数据时发生错误: {}", e.getMessage(), e);
            throw new RuntimeException("塔罗牌游戏初始化失败", e);
        }
    }

    // 由于从TarotCardData类直接获取已创建的TarotCard对象，不再需要这些方法
    // 移除loadMinorArcanaSuit和createTarotCardFromJson方法

    /**
     * 洗牌
     */
    public void shuffleDeck() {
        Collections.shuffle(tarotDeck, random);
        for (TarotCard card : tarotDeck) {
            card.randomizeOrientation();
        }
    }

    /**
     * 抽取指定数量的牌
     */
    public List<TarotCard> drawCards(int numberOfCards) {
        if (numberOfCards <= 0 || numberOfCards > tarotDeck.size()) {
            throw new IllegalArgumentException("抽牌数量必须在1到" + tarotDeck.size() + "之间");
        }

        shuffleDeck();

        // 使用Stream API简化代码
        return tarotDeck.stream()
                .limit(numberOfCards)
                .map(this::copyTarotCard)
                .toList();
    }

    /**
     * 复制塔罗牌对象
     */
    private TarotCard copyTarotCard(TarotCard original) {
        TarotCard copy = new TarotCard();
        copy.setId(original.getId());
        copy.setName(original.getName());
        copy.setEnglishName(original.getEnglishName());
        copy.setUprightMeaning(original.getUprightMeaning());
        copy.setReversedMeaning(original.getReversedMeaning());
        copy.setDescription(original.getDescription());
        copy.setCardType(original.getCardType());
        copy.setSuit(original.getSuit());
        copy.setReversed(original.isReversed());
        return copy;
    }

    /**
     * 进行三张牌占卜
     * 
     * @return 包含解读结果的CardReading对象
     */
    public CardReading performReading() {
        logger.info("开始执行三张牌占卜");

        List<TarotCard> drawnCards = drawCards(3);
        CardReading reading = new CardReading(drawnCards, "过去-现在-未来");
        reading.generateOverallReading();

        logger.info("占卜完成，已生成解读，抽取卡牌数量: {}", drawnCards.size());
        return reading;
    }

    /**
     * 获取所有牌的Map格式数据
     */
    public Map<String, Object> getAllCardsAsMap() {
        // 使用更简洁的Stream API进行分组
        List<Map<String, Object>> majorArcana = tarotDeck.stream()
                .filter(card -> card.getCardType() == TarotCard.CardType.MAJOR_ARCANA)
                .map(this::convertCardToMap)
                .toList();

        // 转换小阿卡纳为花色分组的Map列表
        Map<String, List<Map<String, Object>>> minorArcana = tarotDeck.stream()
                .filter(card -> card.getCardType() == TarotCard.CardType.MINOR_ARCANA && card.getSuit() != null)
                .collect(Collectors.groupingBy(
                        TarotCard::getSuit,
                        Collectors.mapping(this::convertCardToMap, Collectors.toList())));

        // 确保所有花色都有对应的列表
        for (String suit : Arrays.asList("wands", "cups", "swords", "pentacles")) {
            minorArcana.putIfAbsent(suit, new ArrayList<>());
        }

        return Map.of(
                "major_arcana", majorArcana,
                "minor_arcana", minorArcana);
    }

    /**
     * 将TarotCard对象转换为Map格式
     */
    private Map<String, Object> convertCardToMap(TarotCard card) {
        return Map.of(
                "id", card.getId(),
                "name", card.getName(),
                "english_name", card.getEnglishName(),
                "upright_meaning", card.getUprightMeaning(),
                "reversed_meaning", card.getReversedMeaning(),
                "description", card.getDescription());
    }

    /**
     * 获取牌组总数
     */
    public int getDeckSize() {
        return tarotDeck.size();
    }

    /**
     * 获取所有大阿卡纳牌
     */
    public List<TarotCard> getMajorArcanaCards() {
        return tarotDeck.stream()
                .filter(card -> card.getCardType() == TarotCard.CardType.MAJOR_ARCANA)
                .map(this::copyTarotCard)
                .toList();
    }

    /**
     * 获取指定ID的塔罗牌
     */
    public TarotCard getCardById(String id) {
        return tarotDeck.stream()
                .filter(card -> card.getId().equals(id))
                .map(this::copyTarotCard)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未找到指定ID的卡牌"));
    }
}