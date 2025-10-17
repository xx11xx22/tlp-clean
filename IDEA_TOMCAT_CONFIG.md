# IntelliJ IDEA + 外联 Tomcat 配置指南

## 📋 前置条件

-   ✅ IntelliJ IDEA Ultimate 版（社区版不支持 Tomcat 集成）
-   ✅ Apache Tomcat 11.0.11（位于项目目录：`apache-tomcat-11.0.11`）
-   ✅ JDK 25

## 🚀 配置步骤

### 1. 配置 Tomcat 服务器

1. 打开 **File → Settings → Build, Execution, Deployment → Application Servers**
2. 点击 **+** 添加新服务器，选择 **Tomcat Server**
3. **Tomcat Home** 设置为：
    ```
    C:\Users\35600\Desktop\tlp\apache-tomcat-11.0.11
    ```
4. IDEA 会自动识别 Tomcat 版本，点击 **OK**

### 2. 配置 Run/Debug Configuration

1. 点击右上角 **Add Configuration** 或 **Edit Configurations**
2. 点击 **+** 添加 **Tomcat Server → Local**
3. 配置如下：

#### Server 选项卡

-   **Name**: Tomcat 11 - Tarot Game
-   **Application server**: 选择刚才添加的 Tomcat 11.0.11
-   **Open browser**: 勾选
    -   URL: `http://localhost:8080/tarot/`
-   **HTTP port**: 8080
-   **JMX port**: 1099

#### Deployment 选项卡

1. 点击 **+** → **Artifact**
2. 选择 **tarot:war exploded**（如果没有，选择 **tarot:war**）
3. **Application context** 设置为：`/tarot`

### 3. 配置项目结构

1. **File → Project Structure → Modules**
2. 选择你的模块，确保 **Web** 面板已添加
3. **Web Resource Directories** 应包含：
    ```
    src\main\webapp
    ```
4. **Web.xml** 路径：
    ```
    src\main\webapp\WEB-INF\web.xml
    ```

### 4. 配置 Artifact（如果没有自动生成）

1. **File → Project Structure → Artifacts**
2. 点击 **+** → **Web Application: Exploded → From Modules**
3. 选择你的模块
4. 确保输出目录类似：
    ```
    target\tarot
    ```
5. **Available Elements** 中应包含：
    - 编译后的类文件
    - WEB-INF
    - lib（包含依赖 jar）

## ▶️ 运行项目

### 方式一：使用 IDEA 运行

1. 选择配置好的 **Tomcat 11 - Tarot Game**
2. 点击 **▶️ Run** 或 **🐛 Debug**
3. 等待部署完成
4. 浏览器自动打开 `http://localhost:8080/tarot/`

### 方式二：使用外部 Tomcat

1. 构建 WAR 包：
    ```bash
    mvn clean package
    ```
2. 复制 WAR 到 Tomcat：
    ```bash
    copy target\tarot.war apache-tomcat-11.0.11\webapps\
    ```
3. 启动 Tomcat：
    ```bash
    apache-tomcat-11.0.11\bin\startup.bat
    ```

## 🔍 验证部署

访问以下 URL 验证：

-   主页：http://localhost:8080/tarot/
-   API 测试：http://localhost:8080/tarot/api/cards

## ⚠️ 常见问题

### 问题 1: IDEA 找不到 Tomcat

**解决**: 确保使用的是 **IntelliJ IDEA Ultimate** 版本，社区版不支持。

### 问题 2: 端口 8080 被占用

**解决**:

1. 修改 `apache-tomcat-11.0.11\conf\server.xml`
2. 找到 `<Connector port="8080"` 修改端口号
3. 同步修改 IDEA 配置中的端口

### 问题 3: 部署后找不到类

**解决**:

1. **Build → Rebuild Project**
2. 确保 **Artifacts** 包含了所有依赖
3. 检查 **WEB-INF/lib** 下是否有 Jackson 等 jar 包

### 问题 4: 中文乱码

**解决**:

1. 检查 **Settings → Editor → File Encodings** 全部设置为 **UTF-8**
2. **Help → Edit Custom VM Options** 添加：
    ```
    -Dfile.encoding=UTF-8
    ```

### 问题 5: 热部署不生效

**解决**:

1. **Run/Debug Configuration → Server → On 'Update' action** 设置为 **Update classes and resources**
2. **On frame deactivation** 设置为 **Update classes and resources**

## 📝 开发提示

### 快捷键

-   **Ctrl + F10**: 更新运行的应用（热部署）
-   **Shift + F10**: 运行
-   **Shift + F9**: 调试

### Maven 命令

```bash
# 清理编译
mvn clean compile

# 打包（跳过测试）
mvn package -DskipTests

# 查看依赖树
mvn dependency:tree
```

### 日志位置

-   Tomcat 日志：`apache-tomcat-11.0.11\logs\catalina.out`
-   应用日志：根据 logback 配置
-   IDEA 控制台：实时输出

## 🎯 优化建议

### 开发模式优化

1. 使用 **exploded** 格式部署（支持热部署）
2. 启用 **On 'Update' action** 自动更新
3. 配置 **Debug** 模式断点调试

### 生产环境部署

1. 使用 **war** 格式打包
2. 移除开发工具依赖
3. 优化 Tomcat JVM 参数

---

**配置完成后，您就可以在 IDEA 中方便地开发、调试和部署项目了！** 🎉
