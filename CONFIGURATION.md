# 塔罗牌解惑游戏 - 技术配置文档

## 项目概述

本项目已配置为使用 **Apache Tomcat 11.0.11** 和 **OpenJDK 25** 的现代化 Java Web 应用程序。

**部署方式**：外联 Tomcat 服务器（推荐在 IntelliJ IDEA 中使用）

> ❗ **重要提示**：项目已移除嵌入式 Tomcat 依赖，仅支持外部 Tomcat 部署。请参考 [IDEA_TOMCAT_CONFIG.md](IDEA_TOMCAT_CONFIG.md) 配置 IDEA 开发环境。

## 环境配置

### Java 环境

-   **JDK 版本**: OpenJDK 25
-   **编译器**: javac 25
-   **编译参数**: 启用预览功能，包含未检查和弃用警告

### Web 服务器

-   **服务器**: Apache Tomcat 11.0.11
-   **协议**: Jakarta EE 6.0
-   **编码**: UTF-8
-   **访问地址**: http://localhost:8080/tarot/

### 构建工具

-   **Maven**: Apache Maven
-   **编码**: UTF-8
-   **打包格式**: WAR

## 项目结构

```
tlp/
├── src/main/
│   ├── java/tarot/           # Java源代码
│   │   ├── controller/       # Web控制器
│   │   ├── filter/           # 过滤器
│   │   ├── listener/         # 监听器
│   │   ├── model/            # 数据模型
│   │   └── service/          # 业务服务
│   └── webapp/               # Web资源
│       ├── WEB-INF/          # Web配置
│       ├── js/               # JavaScript文件
│       ├── css/              # CSS样式文件
│       ├── images/           # 图片资源
│       └── index.html        # 首页
├── target/                   # 构建输出
├── pom.xml                   # Maven配置
├── apache-tomcat-11.0.11/    # Tomcat服务器
└── README.md                 # 项目说明文档
```

## 关键配置文件

### 1. pom.xml - Maven 配置

-   Java 25 编译目标
-   Jakarta Servlet API 6.1.0
-   Jackson 2.18.0 JSON 处理
-   JUnit 5.11.2 测试框架
-   SLF4J 2.0.16 和 Logback 1.5.8 日志框架
-   优化的编译器参数（内存配置：初始 256m，最大 1024m）

### 2. web.xml - Web 应用配置

-   Jakarta EE 6.0 规范
-   应用初始化监听器配置
-   TarotGameController Servlet 配置（映射路径：/api/\*）
-   会话配置（30 分钟超时，HTTP-only Cookie）
-   MIME 类型配置
-   安全约束配置

## 构建配置

### Maven 编译器配置

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <source>25</source>
        <target>25</target>
        <release>25</release>
        <encoding>UTF-8</encoding>
        <compilerArgs>
            <arg>--enable-preview</arg>
            <arg>-Xlint:unchecked</arg>
            <arg>-Xlint:deprecation</arg>
        </compilerArgs>
        <fork>true</fork>
        <meminitial>256m</meminitial>
        <maxmem>1024m</maxmem>
    </configuration>
</plugin>
```

## 部署流程

### 方式一：IntelliJ IDEA 开发环境（推荐）

1. **配置 IDEA + Tomcat**

    - 详细步骤请查看：[IDEA_TOMCAT_CONFIG.md](IDEA_TOMCAT_CONFIG.md)
    - 添加本地 Tomcat 11.0.11 服务器
    - 配置 Run/Debug Configuration
    - 部署 war exploded 工件

2. **运行项目**
    - 点击 IDEA 右上角的运行按钮
    - 等待自动部署和浏览器打开

### 方式二：命令行部署

1. **构建应用**

    ```bash
    mvn clean package
    ```

2. **部署应用**

    - 构建成功后，WAR 文件将生成在 `target/tarot.war`
    - 可以手动复制到 Tomcat 的 webapps 目录

3. **启动 Tomcat**

    - 使用 Tomcat 自带的启动脚本：

    ```bash
    apache-tomcat-11.0.11\bin\startup.bat
    ```

4. **访问应用**

    - 浏览器打开: http://localhost:8080/tarot/

5. **停止 Tomcat**
    ```bash
    apache-tomcat-11.0.11\bin\shutdown.bat
    ```

## 技术特性

### Jakarta EE 6.0

-   现代化的 Servlet API 6.0
-   增强的安全性配置
-   标准化的 Web 应用架构

### 主要依赖框架

-   **Jackson 2.18.0**: 高性能 JSON 处理库，支持 Java 8+ 日期时间 API
-   **JUnit 5.11.2**: 现代化的 Java 测试框架
-   **SLF4J 2.0.16 + Logback 1.5.8**: 灵活的日志管理解决方案

## 开发指南

### Maven 构建命令

```bash
mvn clean compile          # 编译
mvn clean package          # 打包WAR
mvn clean install          # 安装到本地仓库
```

### 调试配置

在 Tomcat 的 `setenv.bat` 文件中添加以下参数（如果不存在，可在 bin 目录创建）：

```bash
set CATALINA_OPTS=%CATALINA_OPTS% -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005
```

### 日志管理

-   **Tomcat 日志**: 位于 `apache-tomcat-11.0.11/logs/` 目录
-   **应用日志**: 使用 SLF4J + Logback 框架，可通过 `logback.xml` 配置

## 性能优化建议

1. **Maven 构建优化**

    - 项目已配置优化的编译器内存参数（初始 256m，最大 1024m）
    - 使用增量编译提高开发效率

2. **Tomcat 服务器优化**

    - 调整 `server.xml` 中的线程池配置
    - 根据实际负载配置连接超时参数

3. **应用安全建议**
    - 生产环境中启用 HTTPS
    - 修改 `web.xml` 中的 `<secure>true</secure>` 配置
    - 定期更新依赖库以修复安全漏洞

## 故障排除

### 常见问题解决

1. **端口冲突**

    - 修改 `apache-tomcat-11.0.11/conf/server.xml` 中的端口配置

2. **内存不足**

    - 调整 Maven 编译器插件的内存参数
    - 配置 Tomcat 的 JVM 内存参数

3. **HTTP 错误**
    - 查看 Tomcat 日志获取详细错误信息
    - 检查 URL 路径和应用代码

### 日志检查命令

```bash
# 查看 Tomcat 启动日志
type apache-tomcat-11.0.11\logs\catalina.out

# 查看应用访问日志
type apache-tomcat-11.0.11\logs\localhost_access_log.*.txt
```

---

**最后更新时间**: 2024-10-03  
**配置版本**: v1.0  
**项目维护者**: 开发团队
