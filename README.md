# 🔮 塔罗牌解惑游戏

一个基于 Java Web 技术栈开发的塔罗牌占卜游戏，提供美观的图形化界面和专业的塔罗牌解读功能。

## 🌟 项目特色

### 🎮 游戏功能

-   **⏳ 时间之流占卜**: 过去-现在-未来三张牌解读
-   **🧘 身心灵占卜**: 身体-心理-精神三个层面分析
-   **🎯 情况分析占卜**: 情况-行动-结果的完整指导
-   **🌟 每日一卡**: 每日随机抽取指引卡片

### 🃏 塔罗牌系统

-   **完整 78 张牌**: 包含 22 张大阿卡纳和 56 张小阿卡纳
-   **正逆位解读**: 每张牌都有正位和逆位的详细含义
-   **专业解释**: 基于传统塔罗理论的牌义解读
-   **智能洗牌**: 随机决定牌面正逆位，模拟真实占卜

### 🎨 界面设计

-   **神秘风格**: 深邃的渐变背景和神秘色调
-   **响应式设计**: 完美适配桌面和移动设备
-   **动画效果**: 丰富的 CSS 动画提升用户体验
-   **粒子背景**: 营造神秘氛围的动态粒子效果

## 🛠️ 技术架构

### 后端技术

-   **Java 25**: 最新版本 Java 运行环境
-   **Jakarta Servlet 6.1**: 现代化的 Servlet API
-   **Apache Tomcat 11.0.11**: 高性能 Web 容器
-   **Jackson 2.17**: JSON 数据处理
-   **Maven 3.9**: 项目构建和依赖管理

### 前端技术

-   **HTML5**: 语义化标记结构
-   **CSS3**: 现代样式和动画效果
-   **JavaScript ES6+**: 现代化前端交互
-   **Fetch API**: 异步数据请求
-   **响应式设计**: 适配多种设备屏幕

### 架构模式

-   **MVC 架构**: 清晰的模型-视图-控制器分离
-   **RESTful API**: 标准化的 HTTP 接口设计
-   **组件化开发**: 模块化的代码组织结构
-   **单页应用**: 流畅的用户交互体验

## 📁 项目结构

```
tlp/
├── src/main/
│   ├── java/tarot/
│   │   ├── controller/          # Web控制器
│   │   │   └── TarotGameController.java
│   │   ├── service/             # 业务逻辑服务
│   │   │   └── TarotGameService.java
│   │   ├── model/               # 数据模型
│   │   │   ├── TarotCard.java
│   │   │   └── CardReading.java
│   │   ├── filter/              # 过滤器
│   │   │   ├── CORSFilter.java
│   │   │   └── CharacterEncodingFilter.java
│   │   └── listener/            # 监听器
│   │       └── GameInitializationListener.java
│   ├── webapp/
│   │   ├── WEB-INF/
│   │   │   └── web.xml          # Web应用配置
│   │   ├── css/
│   │   │   └── tarot-game.css   # 主样式文件
│   │   ├── js/
│   │   │   └── tarot-game.js    # 主脚本文件
│   │   ├── error/               # 错误页面
│   │   │   ├── 404.html
│   │   │   └── 500.html
│   │   ├── index.html           # 主页面
│   │   └── welcome.html         # 欢迎页面
│   └── resources/
│       └── tarot_cards.json     # 塔罗牌数据
├── apache-tomcat-11.0.11/       # Tomcat服务器
├── pom.xml                      # Maven配置
└── deploy-verify.bat            # 部署验证脚本
```

## 🚀 快速开始

### 环境要求

-   **JDK 25**: OpenJDK 或 Oracle JDK
-   **Maven 3.6+**: 用于项目构建
-   **Apache Tomcat 11.0.11**: 已包含在项目中

### 部署方式

#### 方式一：IntelliJ IDEA 开发环境（推荐）

1. **导入项目**

    - 使用 IDEA 打开项目目录
    - 等待 Maven 自动导入依赖

2. **配置 Tomcat**

    - 详细配置步骤请参考：[IDEA_TOMCAT_CONFIG.md](IDEA_TOMCAT_CONFIG.md)
    - 配置本地 Tomcat 11.0.11 服务器
    - 添加 war exploded 部署方式

3. **运行项目**
    - 点击运行/调试按钮
    - 自动部署并打开浏览器

#### 方式二：外部 Tomcat 部署

1. **编译打包**

    ```bash
    mvn clean package
    ```

2. **部署到 Tomcat**

    ```bash
    copy target\tarot.war apache-tomcat-11.0.11\webapps\
    ```

3. **启动服务器**

    ```bash
    apache-tomcat-11.0.11\bin\startup.bat
    ```

4. **访问应用**
    - 游戏地址: http://localhost:8080/tarot/
    - API 测试: http://localhost:8080/tarot/api/cards

## 🎯 API 接口

### 游戏接口

-   `POST /api/reading` - 进行塔罗牌占卜

### 系统接口

-   `GET /api/cards` - 获取所有卡片
-   `GET /api/major-arcana` - 获取所有大阿卡纳卡片
-   `GET /api/card/{id}` - 根据 ID 获取特定卡片

### 请求示例

```javascript
// 获取所有卡片
fetch("/tarot/api/cards")
    .then((response) => response.json())
    .then((data) => console.log(data));

// 进行塔罗牌占卜
const formData = new FormData();
formData.append("spreadType", "three_card");
fetch("/tarot/api/reading", {
    method: "POST",
    body: formData,
})
    .then((response) => response.json())
    .then((data) => console.log(data));
```

## 🔧 配置说明

### Tomcat 配置

-   **端口**: 8080 (可在 server.xml 中修改)
-   **部署路径**: webapps/tarot
-   **会话超时**: 30 分钟
-   **字符编码**: UTF-8

### 应用配置

-   **API 路径**: /api/\*
-   **错误页面**: 自定义 404/500 页面
-   **CORS 支持**: 允许跨域请求
-   **日志级别**: INFO

## 🎨 自定义开发

### 添加新牌阵

1. 在`TarotGameService`中添加新的占卜方法
2. 在`TarotGameController`中添加对应的 API 端点
3. 在前端添加新的界面和逻辑

### 修改牌面数据

编辑`src/main/resources/tarot_cards.json`文件，按照现有格式添加或修改牌面信息。

### 自定义样式

修改`src/main/webapp/css/tarot-game.css`文件，调整颜色、动画等视觉效果。

## 🐛 故障排除

### 常见问题

**Q: 无法访问游戏页面**
A: 检查 Tomcat 是否启动，端口 8080 是否被占用

**Q: API 请求失败**
A: 确认应用已正确部署，检查网络连接和防火墙设置

**Q: 中文显示乱码**
A: 确保字符编码过滤器正常工作，检查浏览器编码设置

**Q: 编译失败**
A: 确认 JDK 25 已正确安装，Maven 配置正确

### 日志查看

-   **应用日志**: apache-tomcat-11.0.11/logs/catalina.out
-   **访问日志**: apache-tomcat-11.0.11/logs/localhost_access_log.txt
-   **错误日志**: apache-tomcat-11.0.11/logs/catalina.err

## 📄 许可证

本项目仅供学习和研究使用。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request 来改进这个项目！

---

🔮 **愿塔罗之光指引你的代码之路** 🔮
