package tarot.model;

/**
 * 塔罗牌实体类
 */
public class TarotCard {
    private String id;
    private String name;
    private String englishName;
    private String uprightMeaning;
    private String reversedMeaning;
    private String description;
    private boolean isReversed;
    private CardType cardType;
    private String suit; // 仅小阿卡纳使用

    // 定义逆位概率常量，便于维护
    private static final double REVERSED_PROBABILITY = 0.3;

    public enum CardType {
        MAJOR_ARCANA, // 大阿卡纳
        MINOR_ARCANA // 小阿卡纳
    }

    // 默认构造函数
    public TarotCard() {
    }

    /**
     * 获取当前牌的含义（根据正逆位）
     */
    public String getCurrentMeaning() {
        return isReversed ? reversedMeaning : uprightMeaning;
    }

    /**
     * 随机决定正逆位
     */
    public void randomizeOrientation() {
        this.isReversed = Math.random() < REVERSED_PROBABILITY;
    }

    // Getter 和 Setter 方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getUprightMeaning() {
        return uprightMeaning;
    }

    public void setUprightMeaning(String uprightMeaning) {
        this.uprightMeaning = uprightMeaning;
    }

    public String getReversedMeaning() {
        return reversedMeaning;
    }

    public void setReversedMeaning(String reversedMeaning) {
        this.reversedMeaning = reversedMeaning;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isReversed() {
        return isReversed;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return "TarotCard{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", englishName='" + englishName + '\'' +
                ", isReversed=" + isReversed +
                ", cardType=" + cardType +
                ", suit='" + suit + '\'' +
                '}';
    }
}