package tarot;

import tarot.model.TarotCard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 简单的塔罗牌测试类
 * 不依赖外部库，用于基本功能测试
 */
public class SimpleTarotTest {

    public static void main(String[] args) {
        System.out.println("====== 简单塔罗牌测试程序 ======");
        
        try {
            // 创建测试数据
            System.out.println("1. 创建测试数据...");
            List<TarotCard> testCards = createTestTarotCards();
            System.out.println("   ✅ 测试数据创建成功，共" + testCards.size() + "张牌");
            
            // 测试1: 获取所有牌
            System.out.println("\n2. 测试 - 显示所有测试卡牌");
            for (int i = 0; i < testCards.size(); i++) {
                TarotCard card = testCards.get(i);
                System.out.println("   " + (i + 1) + ". " + card.getId() + ": " + card.getName() + " (" + card.getEnglishName() + ")");
                System.out.println("      卡牌类型: " + card.getCardType() + ", 当前含义: " + card.getCurrentMeaning());
            }
            
            // 测试2: 查找特定ID的牌
            System.out.println("\n3. 测试 - 按ID查找卡牌");
            String testId = "1";
            TarotCard foundCard = findCardById(testCards, testId);
            if (foundCard != null) {
                System.out.println("   🔍 找到ID为" + testId + "的卡牌：");
                System.out.println("   - 名称：" + foundCard.getName());
                System.out.println("   - 英文名：" + foundCard.getEnglishName());
                System.out.println("   - 卡牌类型：" + foundCard.getCardType());
                System.out.println("   - 当前含义：" + foundCard.getCurrentMeaning());
            } else {
                System.out.println("   ❌ 未找到ID为" + testId + "的卡牌");
            }
            
            // 测试3: 随机抽一张牌
            System.out.println("\n4. 测试 - 随机抽取1张牌");
            TarotCard randomCard = drawRandomCard(testCards);
            if (randomCard != null) {
                System.out.println("   🎴 抽到的牌：");
                System.out.println("   - 名称：" + randomCard.getName() + " (" + randomCard.getEnglishName() + ")");
                System.out.println("   - 逆位状态：" + (randomCard.isReversed() ? "逆位" : "正位"));
                System.out.println("   - 当前含义：" + randomCard.getCurrentMeaning());
            }
            
            // 测试4: 显示大阿卡那牌
            System.out.println("\n5. 测试 - 显示所有大阿卡那牌");
            List<TarotCard> majorArcanaCards = getMajorArcanaCards(testCards);
            System.out.println("   🧙 大阿卡那牌共有" + majorArcanaCards.size() + "张");
            for (TarotCard card : majorArcanaCards) {
                System.out.println("   - " + card.getId() + ": " + card.getName() + " (" + card.getEnglishName() + ")");
            }
            
            System.out.println("\n====== 测试完成 ======");
            
        } catch (Exception e) {
            System.err.println("❌ 程序执行出错：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // 创建测试塔罗牌数据
    private static List<TarotCard> createTestTarotCards() {
        List<TarotCard> cards = new ArrayList<>();
        
        // 创建大阿卡那牌
        cards.add(createCard("1", "魔术师", "The Magician", "代表创造力和行动能力", 
                "正面时代表自信和行动力", 
                "反面时代表缺乏自信和行动力", 
                TarotCard.CardType.MAJOR_ARCANA, null));
                
        cards.add(createCard("2", "女祭司", "The High Priestess", "代表直觉和神秘", 
                "正面时代表直觉力强和智慧", 
                "反面时代表忽视直觉和隐秘", 
                TarotCard.CardType.MAJOR_ARCANA, null));
                
        cards.add(createCard("3", "女皇", "The Empress", "代表丰饶和母性", 
                "正面时代表丰收和创造力", 
                "反面时代表缺乏耐心和控制力", 
                TarotCard.CardType.MAJOR_ARCANA, null));
                
        // 创建小阿卡那牌
        cards.add(createCard("101", "权杖一", "Ace of Wands", "代表新的开始和创意", 
                "正面时代表新机会和创造力", 
                "反面时代表缺乏方向和行动力", 
                TarotCard.CardType.MINOR_ARCANA, "wands"));
                
        cards.add(createCard("201", "圣杯一", "Ace of Cups", "代表情感和爱", 
                "正面时代表新感情和爱", 
                "反面时代表情感阻塞和冷漠", 
                TarotCard.CardType.MINOR_ARCANA, "cups"));
                
        return cards;
    }
    
    // 创建单个卡牌对象
    private static TarotCard createCard(String id, String name, String englishName, String description,
                                       String uprightMeaning, String reversedMeaning,
                                       TarotCard.CardType cardType, String suit) {
        TarotCard card = new TarotCard();
        card.setId(id);
        card.setName(name);
        card.setEnglishName(englishName);
        card.setDescription(description);
        card.setUprightMeaning(uprightMeaning);
        card.setReversedMeaning(reversedMeaning);
        card.setCardType(cardType);
        card.setSuit(suit);
        
        // 随机设置逆位状态
        Random random = new Random();
        card.setReversed(random.nextDouble() < 0.3);
        
        return card;
    }
    
    // 按ID查找卡牌
    private static TarotCard findCardById(List<TarotCard> cards, String id) {
        for (TarotCard card : cards) {
            if (card.getId().equals(id)) {
                return card;
            }
        }
        return null;
    }
    
    // 随机抽取一张牌
    private static TarotCard drawRandomCard(List<TarotCard> cards) {
        if (cards.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(cards.size());
        TarotCard card = cards.get(index);
        
        // 重新随机设置逆位状态
        card.setReversed(random.nextDouble() < 0.3);
        
        return card;
    }
    
    // 获取所有大阿卡那牌
    private static List<TarotCard> getMajorArcanaCards(List<TarotCard> cards) {
        List<TarotCard> result = new ArrayList<>();
        for (TarotCard card : cards) {
            if (card.getCardType() == TarotCard.CardType.MAJOR_ARCANA) {
                result.add(card);
            }
        }
        return result;
    }
}