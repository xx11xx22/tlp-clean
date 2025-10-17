package tarot;

import tarot.model.TarotCard;
import tarot.service.TarotGameService;

import java.util.List;

/**
 * 塔罗牌游戏的主类
 * 用于测试TarotGameService的功能
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("====== 塔罗牌游戏测试程序 ======");

        try {
            // 初始化TarotGameService
            System.out.println("1. 初始化TarotGameService...");
            TarotGameService tarotGameService = new TarotGameService();
            System.out.println("   ✅ 服务初始化成功！");

            // 测试获取牌组大小
            System.out.println("\n2. 测试 - 获取牌组总数");
            int deckSize = tarotGameService.getDeckSize();
            System.out.println("   📊 牌组共有" + deckSize + "张牌");

            // 测试获取大阿卡那牌
            System.out.println("\n3. 测试 - 获取大阿卡那牌");
            List<TarotCard> majorArcanaCards = tarotGameService.getMajorArcanaCards();
            System.out.println("   🧙 大阿卡那牌共有" + majorArcanaCards.size() + "张");
            System.out.println("   📜 前3张大阿卡那牌：");
            for (int i = 0; i < Math.min(3, majorArcanaCards.size()); i++) {
                TarotCard card = majorArcanaCards.get(i);
                System.out.println("   " + (i + 1) + ". " + card.getId() + ": " + card.getName() + " (" + card.getEnglishName() + ")");
            }

            // 测试按ID获取卡牌
            System.out.println("\n4. 测试 - 按ID获取卡牌");
            try {
                String testCardId = "1";
                TarotCard card = tarotGameService.getCardById(testCardId);
                System.out.println("   🔍 ID为" + testCardId + "的卡牌：");
                System.out.println("   - 名称：" + card.getName());
                System.out.println("   - 英文名：" + card.getEnglishName());
                System.out.println("   - 卡牌类型：" + card.getCardType());
                System.out.println("   - 当前含义：" + card.getCurrentMeaning());
            } catch (Exception e) {
                System.out.println("   ❌ 查找卡牌失败：" + e.getMessage());
            }

            // 测试随机抽牌
            System.out.println("\n5. 测试 - 随机抽取1张牌");
            try {
                List<TarotCard> drawnCards = tarotGameService.drawCards(1);
                if (!drawnCards.isEmpty()) {
                    TarotCard drawnCard = drawnCards.get(0);
                    System.out.println("   🎴 抽到的牌：");
                    System.out.println("   - 名称：" + drawnCard.getName() + " (" + drawnCard.getEnglishName() + ")");
                    System.out.println("   - 逆位状态：" + (drawnCard.isReversed() ? "逆位" : "正位"));
                    System.out.println("   - 当前含义：" + drawnCard.getCurrentMeaning());
                }
            } catch (Exception e) {
                System.out.println("   ❌ 抽牌失败：" + e.getMessage());
            }

            // 测试获取所有卡牌
            System.out.println("\n6. 测试 - 获取所有卡牌");
            try {
                var allCards = tarotGameService.getAllCardsAsMap();
                var majorArcanaMap = (List<?>) allCards.get("major_arcana");
                var minorArcanaMap = (java.util.Map<String, List<?>>) allCards.get("minor_arcana");
                
                System.out.println("   📚 所有卡牌分布：");
                System.out.println("   - 大阿卡那牌：" + majorArcanaMap.size() + "张");
                
                int minorArcanaTotal = 0;
                for (var suitEntry : minorArcanaMap.entrySet()) {
                    int suitCount = suitEntry.getValue().size();
                    System.out.println("   - " + suitEntry.getKey() + "系列：" + suitCount + "张");
                    minorArcanaTotal += suitCount;
                }
                System.out.println("   - 小阿卡那牌总计：" + minorArcanaTotal + "张");
            } catch (Exception e) {
                System.out.println("   ❌ 获取所有卡牌失败：" + e.getMessage());
            }

            System.out.println("\n====== 测试完成 ======");

        } catch (Exception e) {
            System.err.println("❌ 程序执行出错：" + e.getMessage());
            e.printStackTrace();
        }
    }
}