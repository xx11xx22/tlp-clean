package tarot.model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * 塔罗牌读解结果类
 */
public class CardReading {
    private List<TarotCard> drawnCards;
    private String spreadType;
    private String overallReading;
    private LocalDateTime readingTime;
    
    public CardReading() {
        this.drawnCards = new ArrayList<>();
        this.readingTime = LocalDateTime.now();
    }
    
    public CardReading(List<TarotCard> drawnCards, String spreadType) {
        this();
        this.drawnCards = drawnCards;
        this.spreadType = spreadType;
    }

    /**
     * 生成综合解读
     */
    public void generateOverallReading() {
        if (drawnCards == null || drawnCards.isEmpty()) {
            this.overallReading = "无法进行解读，请先抽取塔罗牌。";
            return;
        }

        StringBuilder reading = new StringBuilder();
        reading.append("【塔罗解读】\n\n");

        for (int i = 0; i < drawnCards.size(); i++) {
            TarotCard card = drawnCards.get(i);
            String position = getPositionLabel(i);
            reading.append("🔮 **").append(position).append("**: ").append(card.getName())
                    .append(card.isReversed() ? "（逆位）" : "（正位）").append("\n");
            reading.append(card.getCurrentMeaning()).append("\n");
            reading.append(card.getDescription()).append("\n\n");
        }

        reading.append("📝 **综合提示**: 每张牌都带来了重要的信息，请仔细思考它们之间的联系和对你生活的指导意义。");

        this.overallReading = reading.toString();
    }

    /**
     * 获取位置标签
     * @param position 卡牌位置索引
     * @return 位置标签文本
     */
    public String getPositionLabel(int position) {
        String[] labels = { "过去/根源", "现在/情况", "未来/建议" };
        return position < labels.length ? labels[position] : "第" + (position + 1) + "张牌";
    }

    public String getOverallReading() {
        return overallReading;
    }

    public void setOverallReading(String overallReading) {
        this.overallReading = overallReading;
    }

    public List<TarotCard> getDrawnCards() {
        return drawnCards;
    }

    public void setDrawnCards(List<TarotCard> drawnCards) {
        this.drawnCards = drawnCards;
    }

    public String getSpreadType() {
        return spreadType;
    }

    public void setSpreadType(String spreadType) {
        this.spreadType = spreadType;
    }

    public LocalDateTime getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(LocalDateTime readingTime) {
        this.readingTime = readingTime;
    }
}