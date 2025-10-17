package tarot.listener;

import tarot.service.TarotGameService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 游戏初始化监听器
 * 在应用启动时初始化TarotGameService并将其放入ServletContext中
 */
@WebListener
public class GameInitializationListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(GameInitializationListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("开始初始化塔罗牌游戏应用...");

        ServletContext context = sce.getServletContext();

        try {
            // 创建TarotGameService实例
            TarotGameService tarotGameService = new TarotGameService();

            // 将服务实例放入ServletContext中
            context.setAttribute("tarotGameService", tarotGameService);

            logger.info("TarotGameService初始化成功，已放入ServletContext");
            logger.info("塔罗牌游戏应用初始化完成！");

        } catch (Exception e) {
            logger.error("初始化TarotGameService时发生错误: {}", e.getMessage(), e);
            throw new RuntimeException("应用初始化失败", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("塔罗牌游戏应用正在关闭...");

        ServletContext context = sce.getServletContext();

        // 清理资源
        TarotGameService service = (TarotGameService) context.getAttribute("tarotGameService");
        if (service != null) {
            context.removeAttribute("tarotGameService");
            logger.info("TarotGameService已从ServletContext中移除");
        }

        logger.info("塔罗牌游戏应用关闭完成");
    }
}