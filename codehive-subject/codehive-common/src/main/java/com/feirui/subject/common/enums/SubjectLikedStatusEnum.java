package com.feirui.subject.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 题目点赞枚举
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SubjectLikedStatusEnum {

    LIKED(1, "点赞"),
    UN_LIKED(0, "取消点赞");

    private int code;
    private String desc;

}
