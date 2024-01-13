package com.feirui.subject.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目答案dto：
 *  对不同类型题目统一抽象出来的对象
 */
@Data
public class SubjectAnswerDTO implements Serializable {

    /**
     * 答案选项标识
     */
    private Integer optionType;

    /**
     * 答案
     */
    private String optionContent;

    /**
     * 是否正确
     */
    private Integer isCorrect;

}

