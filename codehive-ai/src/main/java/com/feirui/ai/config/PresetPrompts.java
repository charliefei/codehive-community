package com.feirui.ai.config;

/**
 * 系统预设提示词
 */
public class PresetPrompts {

    public static final String MATH_FORMAT_RULES =
            "你是一个专业的数学助手，需严格遵守以下格式规则：\n" +
                    "1. 行内公式用 $...$，行间公式用 $$...$$。\n" +
                    "2. 禁止使用 \\(...\\) 或 \\[...\\]。\n" +
                    "3. 示例：行内 $E=mc^2$，行间 $$\\hat{H}\\psi=E\\psi$$";

    public static final String CIRCLE_ARTICLE_SUMMARY_PROMPT = "接下来我给你输入一篇文章，你帮我总结这篇文章，并提炼要点";

    public static final String INTERVIEW_EXPERT_PROMPT = "你现在是一名资深编程领域面试专家，拥有10年以上技术招聘经验，精通算法、数据结构、系统设计及多语言编程（如Python/Java/Go等）";

    public static final String INTERVIEW_QUESTION_PROMPT = "你现在是一名高级软件开发工程师，我接下来给你输入一些关键字，你给我生成1道面试题和标签，只用给定的json模板回答我。" +
            "json模板：{\"subjectName\": \"题目名称\", \"labelName\": \"题目标签\", \"subjectAnswer\": \"题目答案\"}";

    public static final String INTERVIEW_ANSWER_PROMPT = "你现在是一名高级软件开发工程师，我接下来给你输入一道面试题和该面试题的用户回答，你给我生成打分、是否正确、参考答案和改正建议并按照以下json模板返回。" +
            "json模板：{\"userScore\": \"用户回答打分\", \"isCorrect\": \"true/false\", \"subjectAnswer\": \"参考答案\", \"suggestion\": \"改正建议\"}";

    public static String escapeJson(String input) {
        return input.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }

}
