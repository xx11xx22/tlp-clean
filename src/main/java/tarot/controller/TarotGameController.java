package tarot.controller;

import tarot.model.TarotCard;
import tarot.model.CardReading;
import tarot.service.TarotGameService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 塔罗牌游戏控制器
 * 处理核心游戏功能的HTTP请求
 */
public class TarotGameController extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(TarotGameController.class);
    // 直接创建ObjectMapper实例
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("TarotGameController 初始化完成");
    }

    /**
     * 从应用上下文中获取TarotGameService实例
     */
    private TarotGameService getTarotGameService() {
        ServletContext context = getServletContext();
        TarotGameService service = (TarotGameService) context.getAttribute("tarotGameService");

        // 如果服务实例不存在，则创建新实例并放入上下文
        if (service == null) {
            logger.warn("从应用上下文获取TarotGameService失败，创建新实例");
            service = new TarotGameService();
            context.setAttribute("tarotGameService", service);
            logger.info("已创建并存储新的TarotGameService实例");
        }

        return service;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * 统一处理GET和POST请求，避免重复的异常处理逻辑
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json;charset=UTF-8");

        try {
            // 根据HTTP方法和路径处理不同的请求
            if ("GET".equals(request.getMethod())) {
                if ("/cards".equals(pathInfo)) {
                    handleGetAllCards(response);
                } else if ("/major-arcana".equals(pathInfo)) {
                    handleGetMajorArcana(response);
                } else if (pathInfo != null && pathInfo.startsWith("/card/")) {
                    // 处理获取单张卡牌的请求: /api/card/{id}
                    String cardId = pathInfo.substring(6);
                    handleGetCardById(response, cardId);
                } else {
                    sendErrorResponse(response, 404, "API端点未找到");
                }
            } else if ("POST".equals(request.getMethod())) {
                if ("/reading".equals(pathInfo)) {
                    handleReading(request, response);
                } else {
                    sendErrorResponse(response, 404, "API端点未找到");
                }
            }
        } catch (IllegalArgumentException e) {
            // 处理参数错误
            sendErrorResponse(response, 400, "参数错误: " + e.getMessage());
        } catch (Exception e) {
            // 处理其他未预期的异常
            logger.error("处理请求时发生错误: {}", e.getMessage(), e);
            sendErrorResponse(response, 500, "服务器内部错误");
        }
    }

    /**
     * 处理获取所有大阿卡纳牌请求
     */
    private void handleGetMajorArcana(HttpServletResponse response) throws IOException {
        List<TarotCard> majorArcanaCards = getTarotGameService().getMajorArcanaCards();
        Map<String, Object> result = Map.of(
                "success", true,
                "major_arcana", majorArcanaCards);
        sendJsonResponse(response, result);
    }

    /**
     * 处理获取单张卡牌的请求
     */
    private void handleGetCardById(HttpServletResponse response, String cardId) throws IOException {
        TarotCard card = getTarotGameService().getCardById(cardId);
        Map<String, Object> result = Map.of(
                "success", true,
                "card", card);
        sendJsonResponse(response, result);
    }

    /**
     * 处理获取所有卡牌请求
     */
    private void handleGetAllCards(HttpServletResponse response) throws IOException {
        Map<String, Object> allCards = getTarotGameService().getAllCardsAsMap();
        Map<String, Object> result = Map.of(
                "success", true,
                "major_arcana", allCards.get("major_arcana"),
                "minor_arcana", allCards.get("minor_arcana"));
        sendJsonResponse(response, result);
    }

    /**
     * 处理占卜请求
     */
    protected void handleReading(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            CardReading reading = getTarotGameService().performReading();
            logger.info("成功生成塔罗解读");

            // 使用HashMap而非不可变Map，确保更好的序列化兼容性
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);

            // 使用HashMap构建reading对象
            Map<String, Object> readingMap = new HashMap<>();
            readingMap.put("overallReading", reading.getOverallReading());
            readingMap.put("spreadType", reading.getSpreadType());
            readingMap.put("readingTime", reading.getReadingTime().toString()); // 已转换为字符串

            // 转换drawnCards为更简单的Map列表，避免序列化复杂对象
            List<Map<String, Object>> cardsList = new ArrayList<>();
            for (int i = 0; i < reading.getDrawnCards().size(); i++) {
                TarotCard card = reading.getDrawnCards().get(i);
                Map<String, Object> cardMap = new HashMap<>();
                cardMap.put("id", card.getId());
                cardMap.put("name", card.getName());
                cardMap.put("englishName", card.getEnglishName());
                cardMap.put("uprightMeaning", card.getUprightMeaning());
                cardMap.put("reversedMeaning", card.getReversedMeaning());
                cardMap.put("description", card.getDescription());
                cardMap.put("isReversed", card.isReversed());
                cardMap.put("cardType", card.getCardType().name()); // 转换枚举为字符串
                cardMap.put("suit", card.getSuit());
                // 添加当前含义和位置标签
                cardMap.put("currentMeaning", card.getCurrentMeaning());
                cardMap.put("positionLabel", reading.getPositionLabel(i));
                cardsList.add(cardMap);
            }
            readingMap.put("drawnCards", cardsList);
            result.put("reading", readingMap);

            sendJsonResponse(response, result);
        } catch (Exception e) {
            sendErrorResponse(response, 500, "处理占卜请求时发生错误: " + e.getMessage());
        }
    }

    /**
     * 发送JSON响应
     */
    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        try (PrintWriter out = response.getWriter()) {

            if (data == null) {
                out.print("{\"error\":\"响应数据为空\"}");
                out.flush();
                return;
            }

            logger.info("准备序列化响应数据，数据类型: {}", data.getClass().getName());
            String jsonResponse = objectMapper.writeValueAsString(data);

            logger.info("JSON序列化成功，响应长度: {}", jsonResponse.length());
            out.print(jsonResponse);
            out.flush();

            logger.info("响应已发送");
        } catch (Exception e) {
            logger.error("JSON序列化或响应发送失败: {}", e.getMessage(), e);
            throw new IOException("无法序列化响应数据: " + e.getMessage(), e);
        }
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message)
            throws IOException {
        response.setStatus(statusCode);

        Map<String, Object> error = Map.of(
                "success", false,
                "error", message,
                "statusCode", statusCode);

        sendJsonResponse(response, error);
    }
}