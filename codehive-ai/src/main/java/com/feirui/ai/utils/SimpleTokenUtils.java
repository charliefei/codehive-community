package com.feirui.ai.utils;

public class SimpleTokenUtils {

    // 基于经验值估算（1个token ≈ 4个英文字符 ≈ 1.5个中文字符）
    public static int countTokens(String text) {
        // 中文按字拆分（每个汉字约计1.5个token）
        int chineseChars = text.replaceAll("[^\\u4E00-\\u9FA5]", "").length();
        // 英文按空格分割单词
        int englishTokens = text.replaceAll("[\\u4E00-\\u9FA5]", "").split("\\s+").length;
        // 符号和数字按字符计算（约4字符=1token）
        int symbols = text.replaceAll("[\\u4E00-\\u9FA5a-zA-Z\\s]", "").length();

        return (int) Math.ceil(
            chineseChars * 1.5 +
            englishTokens +
            symbols / 4.0
        );
    }

}