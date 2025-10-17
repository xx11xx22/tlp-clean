package tarot.data;

import tarot.model.TarotCard;
import java.util.*;

/**
 * 塔罗牌数据类，包含所有塔罗牌的硬编码数据
 * 用于替代JSON文件存储
 */
public class TarotCardData {
    
    // 大阿卡纳牌数据
    private static final List<TarotCard> MAJOR_ARCANA = List.of(
        createMajorArcanaCard("0", "愚人", "The Fool", 
            "新的开始，冒险，自由，纯真，机遇", 
            "鲁莽，天真，缺乏责任感，迷失", 
            "代表新的旅程，充满无限可能和冒险精神。愚人象征着纯粹的灵魂，愿意拥抱未知而不畏惧。"),
        createMajorArcanaCard("1", "魔术师", "The Magician", 
            "创造，自信，技能，意志力，操控力", 
            "操纵，欺骗，缺乏自信，技能不足", 
            "代表无限的创造力和将想法转化为现实的能力。魔术师掌握着四种元素的力量，象征着潜能的实现。"),
        createMajorArcanaCard("2", "女祭司", "The High Priestess", 
            "直觉，神秘，潜意识，内在知识，精神指导", 
            "隐藏，困惑，忽视直觉，情绪波动", 
            "象征着内在的智慧和直觉力。女祭司是精神世界的守护者，代表着神秘和未知的力量。"),
        createMajorArcanaCard("3", "女皇", "The Empress", 
            "丰饶，母性，创造力，富足，滋养", 
            "过度依赖，懒惰，物质主义，忽视实际", 
            "代表丰饶和母性的力量。女皇象征着创造力、富足和生命的延续。"),
        createMajorArcanaCard("4", "皇帝", "The Emperor", 
            "权威，结构，控制，保护，领导力", 
            "专制，控制欲强，缺乏灵活性，僵化", 
            "象征着权威和结构化的力量。皇帝代表着领导力、保护和稳定的结构。"),
        createMajorArcanaCard("5", "教皇", "The Hierophant", 
            "传统，指导，教育，精神指导，社会规范", 
            "教条，盲目追随，限制，缺乏独立思考", 
            "代表着精神指导和传统价值观。教皇象征着社会规范和精神成长的引导。"),
        createMajorArcanaCard("6", "恋人", "The Lovers", 
            "爱，选择，和谐，关系，统一", 
            "冲突，错误的选择，不和谐，分离", 
            "象征着爱与选择。恋人代表着重要的决定和情感关系的发展。"),
        createMajorArcanaCard("7", "战车", "The Chariot", 
            "胜利，控制，决心，成功，意志力", 
            "缺乏控制，冲突，挫败，固执", 
            "代表着意志力和成功。战车象征着通过决心和自律克服障碍。"),
        createMajorArcanaCard("8", "力量", "Strength", 
            "勇气，力量，耐心，控制，勇气", 
            "软弱，失控，缺乏耐心，恐惧", 
            "象征着内在的力量和勇气。力量代表着通过爱和耐心克服挑战。"),
        createMajorArcanaCard("9", "隐者", "The Hermit", 
            "内省，孤独，智慧，引导，沉思", 
            "孤立，缺乏社交，过度思考，迷失", 
            "代表着内省和寻求智慧。隐者象征着独处和自我发现的旅程。"),
        createMajorArcanaCard("10", "命运之轮", "Wheel of Fortune", 
            "命运，变化，周期，机会，好运", 
            "命运无常，变化剧烈，不幸，阻力", 
            "象征着命运和生命周期的循环。命运之轮代表着生活中的起伏和变化。"),
        createMajorArcanaCard("11", "正义", "Justice", 
            "正义，公平，平衡，因果，真相", 
            "不公平，偏见，失衡，不诚实", 
            "代表着公平和平衡。正义象征着因果关系和真相的显现。"),
        createMajorArcanaCard("12", "倒吊人", "The Hanged Man", 
            "牺牲，视角转变，等待，暂停，启蒙", 
            "牺牲过度，停滞，犹豫不决，固执", 
            "象征着牺牲和视角的转变。倒吊人代表着通过不同角度看待问题而获得的智慧。"),
        createMajorArcanaCard("13", "死神", "Death", 
            "结束，转变，重生，新开始，释放", 
            "抵抗变化，恐惧，停滞，死亡焦虑", 
            "代表着结束和新的开始。死神象征着重大的转变和旧模式的终结。"),
        createMajorArcanaCard("14", "节制", "Temperance", 
            "平衡，调和，耐心，适度，融合", 
            "失衡，过度，不耐烦，冲突", 
            "象征着平衡和调和。节制代表着不同力量的和谐融合。"),
        createMajorArcanaCard("15", "恶魔", "The Devil", 
            "束缚，欲望，诱惑，物质主义，限制", 
            "解放，摆脱束缚，克服欲望，自由", 
            "象征着束缚和诱惑。恶魔代表着我们被物质欲望或消极模式所困的状态。"),
        createMajorArcanaCard("16", "高塔", "The Tower", 
            "突然变化，崩溃，觉醒，释放，灾难", 
            "抵抗变化，恐惧，崩溃，毁灭", 
            "象征着突然的变革和崩溃。高塔代表着旧结构的瓦解和新视角的出现。"),
        createMajorArcanaCard("17", "星星", "The Star", 
            "希望，灵感，信仰，指引，灵性", 
            "绝望，失去信仰，缺乏灵感，迷茫", 
            "象征着希望和灵感。星星代表着在困难时期保持信仰和希望。"),
        createMajorArcanaCard("18", "月亮", "The Moon", 
            "潜意识，情绪，幻觉，恐惧，神秘", 
            "困惑，焦虑，噩梦，欺骗", 
            "象征着潜意识和情绪世界。月亮代表着内心的恐惧和直觉的力量。"),
        createMajorArcanaCard("19", "太阳", "The Sun", 
            "成功，喜悦，活力，真相，温暖", 
            "悲观，缺乏活力，真相扭曲，不快乐", 
            "象征着成功和喜悦。太阳代表着活力、真相和积极的能量。"),
        createMajorArcanaCard("20", "审判", "Judgement", 
            "重生，觉醒，评估，转变，召唤", 
            "逃避，拒绝改变，内疚，停滞", 
            "象征着重生和觉醒。审判代表着对过去的评估和新的开始。"),
        createMajorArcanaCard("21", "世界", "The World", 
            "完成，成功，圆满，旅行，成就", 
            "未完成，停滞，不满足，缺乏方向", 
            "象征着完成和圆满。世界代表着一个旅程的结束和新旅程的开始。")
    );
    
    // 小阿卡纳牌数据 - 权杖系列
    private static final List<TarotCard> WANDS = List.of(
        createMinorArcanaCard("wands_ace", "权杖首牌", "Ace of Wands", 
            "新的开始，创造力，热情，能量，行动", 
            "创造力受阻，缺乏热情，能量不足，犹豫", 
            "象征着新的创意和能量的开始。权杖首牌代表着热情和行动的火花。", 
            "wands"),
        createMinorArcanaCard("wands_2", "权杖二", "Two of Wands", 
            "规划，决策，未来展望，探索，扩展", 
            "犹豫不决，缺乏规划，限制，冲突", 
            "代表着对未来的规划和决策。权杖二象征着探索新的可能性。", 
            "wands"),
        createMinorArcanaCard("wands_3", "权杖三", "Three of Wands", 
            "远见，进展，扩张，团队合作，探索", 
            "进展缓慢，缺乏远见，孤立，停滞", 
            "象征着通过合作和远见取得进展。权杖三代表着扩张和探索。", 
            "wands"),
        createMinorArcanaCard("wands_4", "权杖四", "Four of Wands", 
            "稳定，庆祝，和谐，完成，家庭", 
            "不稳定，冲突，不满足，孤立", 
            "代表着稳定和庆祝。权杖四象征着家庭和谐和成就的认可。", 
            "wands"),
        createMinorArcanaCard("wands_5", "权杖五", "Five of Wands", 
            "冲突，竞争，挑战，分歧，竞争", 
            "合作，和谐，解决冲突，团队精神", 
            "象征着冲突和竞争。权杖五代表着在竞争环境中保持自己立场的挑战。", 
            "wands"),
        createMinorArcanaCard("wands_6", "权杖六", "Six of Wands", 
            "胜利，成功，认可，成就，信心", 
            "失败，缺乏认可，信心不足，阻碍", 
            "代表着成功和胜利。权杖六象征着努力得到认可和赞赏。", 
            "wands"),
        createMinorArcanaCard("wands_7", "权杖七", 
            "Seven of Wands", 
            "坚持，防御，挑战，勇气，抵抗", 
            "放弃，软弱，妥协，撤退", 
            "象征着面对挑战的勇气和坚持。权杖七代表着捍卫自己立场的决心。", 
            "wands"),
        createMinorArcanaCard("wands_8", "权杖八", "Eight of Wands", 
            "行动，速度，进展，旅行，消息", 
            "拖延，停滞，缓慢，延迟", 
            "代表着快速的行动和进展。权杖八象征着事情顺利进行。", 
            "wands"),
        createMinorArcanaCard("wands_9", "权杖九", "Nine of Wands", 
            "防御，警惕，耐心，坚持，准备", 
            "放松警惕，过度防御，疲惫，放弃", 
            "象征着警惕和防御。权杖九代表着在压力下保持警惕和坚持。", 
            "wands"),
        createMinorArcanaCard("wands_10", "权杖十", "Ten of Wands", 
            "负担，责任，压力，过度承诺，挑战", 
            "释放，优先级，支持，平衡", 
            "象征着负担和责任。权杖十代表着承担过多责任的压力。", 
            "wands"),
        createMinorArcanaCard("wands_page", "权杖侍从", "Page of Wands", 
            "探索，新想法，热情，好奇心，冒险", 
            "冲动，缺乏方向，分散注意力，不专注", 
            "象征着新想法和探索的热情。权杖侍从代表着对新事物的好奇和冒险精神。", 
            "wands"),
        createMinorArcanaCard("wands_knight", "权杖骑士", "Knight of Wands", 
            "行动，热情，冒险，勇气，速度", 
            "冲动，鲁莽，缺乏耐心，不负责任", 
            "象征着热情和行动。权杖骑士代表着快速行动和追求目标的决心。", 
            "wands"),
        createMinorArcanaCard("wands_queen", "权杖王后", "Queen of Wands", 
            "自信，热情，创造力，领导力，魅力", 
            "自负，缺乏热情，创造力受阻，控制欲", 
            "象征着自信和热情的领导力。权杖王后代表着充满魅力和创造力的女性特质。", 
            "wands"),
        createMinorArcanaCard("wands_king", "权杖国王", "King of Wands", 
            "领导力，创造力，自信，权威，热情", 
            "独裁，缺乏创造力，过度自信，冷漠", 
            "象征着热情和创造力的领导。权杖国王代表着自信和有远见的权威。", 
            "wands")
    );
    
    // 小阿卡纳牌数据 - 圣杯系列
    private static final List<TarotCard> CUPS = List.of(
        createMinorArcanaCard("cups_ace", "圣杯首牌", "Ace of Cups", 
            "爱，情感，新关系，直觉，创造力", 
            "情感阻塞，孤独，缺乏创造力，关系问题", 
            "象征着新的情感开始和爱的流动。圣杯首牌代表着情感的丰富和直觉的开启。", 
            "cups"),
        createMinorArcanaCard("cups_2", "圣杯二", "Two of Cups", 
            "合作，和谐，爱情，友谊，连接", 
            "冲突，不和谐，分离，误解", 
            "代表着和谐的关系和合作。圣杯二象征着情感上的连接和相互理解。", 
            "cups"),
        createMinorArcanaCard("cups_3", "圣杯三", "Three of Cups", 
            "庆祝，友谊，喜悦，社交，团队合作", 
            "孤独，社交孤立，冲突，不满足", 
            "象征着庆祝和友谊。圣杯三代表着社交聚会和共享的喜悦。", 
            "cups"),
        createMinorArcanaCard("cups_4", "圣杯四", "Four of Cups", 
            "思考，反思，不满，机会，内省", 
            "开放，接受，满足，行动", 
            "象征着思考和反思。圣杯四代表着对现状的不满和对新机会的思考。", 
            "cups"),
        createMinorArcanaCard("cups_5", "圣杯五", "Five of Cups", 
            "悲伤，失落，遗憾，失望，内省", 
            "接受，希望，新开始，释放", 
            "象征着悲伤和失落。圣杯五代表着面对失去的情感挑战和从中学习的机会。", 
            "cups"),
        createMinorArcanaCard("cups_6", "圣杯六", "Six of Cups", 
            "怀旧，友谊，礼物，快乐，回忆", 
            "遗憾，不切实际，拒绝成长，孤立", 
            "象征着怀旧和友谊。圣杯六代表着童年的回忆和纯真的友谊。", 
            "cups"),
        createMinorArcanaCard("cups_7", "圣杯七", "Seven of Cups", 
            "选择，幻想，可能性，梦想，不确定性", 
            "混乱，优柔寡断，不切实际，迷失", 
            "象征着选择和可能性。圣杯七代表着面对多个选择时的思考和梦想。", 
            "cups"),
        createMinorArcanaCard("cups_8", "圣杯八", "Eight of Cups", 
            "放弃，新开始，寻找，成长，不满", 
            "坚持，满足，停留，避免改变", 
            "象征着放弃和新的开始。圣杯八代表着放弃不满足的状况去寻找更好的机会。", 
            "cups"),
        createMinorArcanaCard("cups_9", "圣杯九", "Nine of Cups", 
            "满足，幸福，成功，实现，情感丰富", 
            "不满，不快乐，未实现，失望", 
            "象征着满足和幸福。圣杯九代表着情感和物质上的满足。", 
            "cups"),
        createMinorArcanaCard("cups_10", "圣杯十", "Ten of Cups", 
            "家庭，和谐，幸福，满足，完美", 
            "冲突，不和谐，家庭问题，不满足", 
            "象征着家庭和谐和幸福。圣杯十代表着完美的家庭关系和情感满足。", 
            "cups"),
        createMinorArcanaCard("cups_page", "圣杯侍从", "Page of Cups", 
            "情感，直觉，创造力，新想法，敏感", 
            "冷漠，缺乏创造力，情感封闭，冲动", 
            "象征着情感和直觉的开始。圣杯侍从代表着敏感和富有创造力的特质。", 
            "cups"),
        createMinorArcanaCard("cups_knight", "圣杯骑士", "Knight of Cups", 
            "浪漫，情感，创意，敏感，理想主义", 
            "不切实际，情感不稳定，缺乏行动，冷漠", 
            "象征着浪漫和情感。圣杯骑士代表着理想主义和对情感的追求。", 
            "cups"),
        createMinorArcanaCard("cups_queen", "圣杯王后", "Queen of Cups", 
            "情感，直觉，同理心，关怀，创造力", 
            "情感不稳定，冷漠，缺乏同理心，情绪波动", 
            "象征着情感和直觉的力量。圣杯王后代表着同理心和关怀的女性特质。", 
            "cups"),
        createMinorArcanaCard("cups_king", "圣杯国王", "King of Cups", 
            "情感稳定，智慧，领导力，同理心，平衡", 
            "情感冷漠，缺乏同理心，不平衡，不智慧", 
            "象征着情感稳定和智慧的领导。圣杯国王代表着平衡情感和理性的能力。", 
            "cups")
    );
    
    // 小阿卡纳牌数据 - 宝剑系列
    private static final List<TarotCard> SWORDS = List.of(
        createMinorArcanaCard("swords_ace", "宝剑首牌", "Ace of Swords", 
            "思想，真相，清晰度，新想法，决定", 
            "混乱，困惑，谎言，优柔寡断，冲突", 
            "象征着清晰的思想和真相的力量。宝剑首牌代表着新的想法和决定性的行动。", 
            "swords"),
        createMinorArcanaCard("swords_2", "宝剑二", "Two of Swords", 
            "决策，平衡，选择，僵局，和平", 
            "优柔寡断，冲突，不平衡，混乱", 
            "代表着需要做出决策的时刻。宝剑二象征着在两难选择中寻找平衡。", 
            "swords"),
        createMinorArcanaCard("swords_3", "宝剑三", "Three of Swords", 
            "悲伤，心碎，痛苦，背叛，冲突", 
            "治愈，和解，希望，释放，原谅", 
            "象征着情感上的痛苦和悲伤。宝剑三代表着心碎和背叛带来的痛苦。", 
            "swords"),
        createMinorArcanaCard("swords_4", "宝剑四", "Four of Swords", 
            "休息，冥想，恢复，和平，反思", 
            "过度活跃，压力，不休息，焦虑", 
            "象征着休息和恢复。宝剑四代表着在压力后需要休息和反思。", 
            "swords"),
        createMinorArcanaCard("swords_5", "宝剑五", "Five of Swords", 
            "冲突，胜利，竞争，分歧，不和谐", 
            "合作，和谐，妥协，和平", 
            "象征着冲突和竞争。宝剑五代表着通过冲突获得表面胜利的代价。", 
            "swords"),
        createMinorArcanaCard("swords_6", "宝剑六", "Six of Swords", 
            "旅程，和平，过渡，进展，离开", 
            "停滞，冲突，延迟，不进展", 
            "象征着和平的旅程和过渡。宝剑六代表着从困难时期迈向平静的过程。", 
            "swords"),
        createMinorArcanaCard("swords_7", "宝剑七", "Seven of Swords", 
            "策略，欺骗，秘密，智取，谨慎", 
            "诚实，直接，信任，公开", 
            "象征着策略和谨慎。宝剑七代表着通过智慧和策略解决问题。", 
            "swords"),
        createMinorArcanaCard("swords_8", "宝剑八", "Eight of Swords", 
            "限制，束缚，恐惧，困境，自我限制", 
            "自由，释放，勇气，突破，行动", 
            "象征着限制和束缚。宝剑八代表着自我限制和恐惧带来的困境。", 
            "swords"),
        createMinorArcanaCard("swords_9", "宝剑九", "Nine of Swords", 
            "焦虑，恐惧，噩梦，担忧，失眠", 
            "平静，勇气，释放，信任，安心", 
            "象征着焦虑和恐惧。宝剑九代表着内心的担忧和精神压力。", 
            "swords"),
        createMinorArcanaCard("swords_10", "宝剑十", "Ten of Swords", 
            "结束，痛苦，失败，绝望，转变", 
            "希望，新开始，释放，恢复，重生", 
            "象征着痛苦的结束和新的开始。宝剑十代表着极端痛苦后的转变。", 
            "swords"),
        createMinorArcanaCard("swords_page", "宝剑侍从", "Page of Swords", 
            "好奇，学习，沟通，新想法，调查", 
            "无知，缺乏沟通，误解，鲁莽", 
            "象征着好奇和学习的精神。宝剑侍从代表着对知识的追求和新想法的探索。", 
            "swords"),
        createMinorArcanaCard("swords_knight", "宝剑骑士", "Knight of Swords", 
            "行动，逻辑，挑战，直率，快速思考", 
            "冲动，鲁莽，缺乏考虑，冲突", 
            "象征着快速行动和逻辑思维。宝剑骑士代表着直率和挑战性的沟通。", 
            "swords"),
        createMinorArcanaCard("swords_queen", "宝剑王后", "Queen of Swords", 
            "智慧，独立，沟通，清晰，公正", 
            "苛刻，冷漠，不公正，模糊", 
            "象征着智慧和独立的思考。宝剑王后代表着清晰的沟通和公正的判断。", 
            "swords"),
        createMinorArcanaCard("swords_king", "宝剑国王", "King of Swords", 
            "权威，逻辑，智慧，公正，领导力", 
            "独裁，不公正，缺乏情感，僵化", 
            "象征着逻辑和智慧的领导。宝剑国王代表着公正和权威的判断。", 
            "swords")
    );
    
    // 小阿卡纳牌数据 - 星币系列
    private static final List<TarotCard> PENTACLES = List.of(
        createMinorArcanaCard("pentacles_ace", "星币首牌", "Ace of Pentacles", 
            "繁荣，新机会，物质成功，财务，稳定", 
            "财务困难，缺乏机会，不稳定，失败", 
            "象征着新的物质和财务机会。星币首牌代表着繁荣和稳定的开始。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_2", "星币二", "Two of Pentacles", 
            "平衡，适应，多任务，管理，灵活性", 
            "不平衡，压力，无法应对，混乱", 
            "代表着平衡和适应能力。星币二象征着在多个责任之间保持平衡。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_3", "星币三", "Three of Pentacles", 
            "合作，技能，团队合作，学习，专业", 
            "孤立，技能不足，冲突，缺乏合作", 
            "象征着合作和技能的发展。星币三代表着通过团队合作实现目标。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_4", "星币四", "Four of Pentacles", 
            "储蓄，安全，控制，占有，物质主义", 
            "挥霍，不安全，失控，慷慨", 
            "象征着储蓄和安全。星币四代表着对物质财富的控制和占有。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_5", "星币五", "Five of Pentacles", 
            "贫困，困难，失业，缺乏，忽视", 
            "繁荣，帮助，支持，恢复，关怀", 
            "象征着物质上的困难和贫困。星币五代表着经济困难和需要帮助的时期。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_6", "星币六", "Six of Pentacles", 
            "给予，接收，慈善，公平，分享", 
            "自私，不公平，贪婪，拒绝帮助", 
            "象征着给予和接收。星币六代表着慈善和公平的交换。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_7", "星币七", "Seven of Pentacles", 
            "耐心，投资，成长，收获，评估", 
            "急躁，不当投资，停滞，缺乏耐心", 
            "象征着耐心和投资。星币七代表着对长期投资的耐心等待和评估。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_8", "星币八", "Eight of Pentacles", 
            "工作，技能，专注，勤奋，进步", 
            "懒惰，技能不足，分心，缺乏进步", 
            "象征着专注和勤奋的工作。星币八代表着通过努力和专注提高技能。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_9", "星币九", "Nine of Pentacles", 
            "成功，独立，富足，优雅，自信", 
            "失败，依赖，贫困，不满足", 
            "象征着成功和独立。星币九代表着通过努力获得的物质和精神上的富足。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_10", "星币十", "Ten of Pentacles", 
            "财富，家庭，遗产，安全，成功", 
            "贫困，家庭问题，不稳定，失败", 
            "象征着财富和家庭的稳定。星币十代表着长期积累的财富和家庭传统。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_page", "星币侍从", "Page of Pentacles", 
            "学习，新机会，实践，耐心，成长", 
            "急躁，缺乏学习，不实践，不专注", 
            "象征着学习和实践的开始。星币侍从代表着对实际技能和知识的追求。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_knight", "星币骑士", "Knight of Pentacles", 
            "勤奋，实用，稳定，可靠，耐心", 
            "懒惰，不切实际，不稳定，不可靠", 
            "象征着勤奋和实用的态度。星币骑士代表着稳定和可靠的行动。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_queen", "星币王后", "Queen of Pentacles", 
            "富足，关怀，实用，母性，稳定", 
            "贫困，冷漠，不实用，不稳定", 
            "象征着富足和关怀。星币王后代表着实用和母性的关怀。", 
            "pentacles"),
        createMinorArcanaCard("pentacles_king", "星币国王", "King of Pentacles", 
            "成功，权威，商业，稳定，实践", 
            "失败，不权威，不稳定，不实用", 
            "象征着成功和实用的领导。星币国王代表着商业智慧和物质上的成功。", 
            "pentacles")
    );
    
    /**
     * 创建大阿卡纳卡牌对象
     */
    private static TarotCard createMajorArcanaCard(String id, String name, String englishName, 
                                             String uprightMeaning, String reversedMeaning, String description) {
        TarotCard card = new TarotCard();
        card.setId(id);
        card.setName(name);
        card.setEnglishName(englishName);
        card.setUprightMeaning(uprightMeaning);
        card.setReversedMeaning(reversedMeaning);
        card.setDescription(description);
        card.setCardType(TarotCard.CardType.MAJOR_ARCANA);
        return card;
    }
    
    /**
     * 创建小阿卡纳卡牌对象
     */
    private static TarotCard createMinorArcanaCard(String id, String name, String englishName, 
                                             String uprightMeaning, String reversedMeaning, String description, 
                                             String suit) {
        TarotCard card = new TarotCard();
        card.setId(id);
        card.setName(name);
        card.setEnglishName(englishName);
        card.setUprightMeaning(uprightMeaning);
        card.setReversedMeaning(reversedMeaning);
        card.setDescription(description);
        card.setCardType(TarotCard.CardType.MINOR_ARCANA);
        card.setSuit(suit);
        return card;
    }
    
    /**
     * 获取所有塔罗牌
     */
    public static List<TarotCard> getAllTarotCards() {
        List<TarotCard> allCards = new ArrayList<>();
        allCards.addAll(MAJOR_ARCANA);
        allCards.addAll(WANDS);
        allCards.addAll(CUPS);
        allCards.addAll(SWORDS);
        allCards.addAll(PENTACLES);
        return allCards;
    }
    
    /**
     * 获取所有大阿卡纳牌
     */
    public static List<TarotCard> getMajorArcana() {
        return MAJOR_ARCANA;
    }
    
    /**
     * 获取所有小阿卡纳牌（按花色分组）
     */
    public static Map<String, List<TarotCard>> getMinorArcana() {
        Map<String, List<TarotCard>> minorArcana = new HashMap<>();
        minorArcana.put("wands", WANDS);
        minorArcana.put("cups", CUPS);
        minorArcana.put("swords", SWORDS);
        minorArcana.put("pentacles", PENTACLES);
        return minorArcana;
    }
    
    /**
     * 获取所有小阿卡纳牌（合并列表）
     */
    public static List<TarotCard> getAllMinorArcana() {
        List<TarotCard> allMinorArcana = new ArrayList<>();
        allMinorArcana.addAll(WANDS);
        allMinorArcana.addAll(CUPS);
        allMinorArcana.addAll(SWORDS);
        allMinorArcana.addAll(PENTACLES);
        return allMinorArcana;
    }
}