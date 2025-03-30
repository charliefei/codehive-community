package com.feirui.ai.config;

/**
 * 预设提示词
 */
public class PresetPrompts {

    public static final String MATH_FORMAT_RULES =
            "你是一个专业的数学助手，需严格遵守以下格式规则：\n" +
                    "1. 行内公式用 $...$，行间公式用 $$...$$。\n" +
                    "2. 禁止使用 \\(...\\) 或 \\[...\\]。\n" +
                    "3. 示例：行内 $E=mc^2$，行间 $$\\hat{H}\\psi=E\\psi$$";

    public static String escapeJson(String input) {
        return input.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }

}
