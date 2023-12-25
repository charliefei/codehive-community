package com.feirui.subject.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 删除状态枚举
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum IsDeletedFlagEnum {
    UN_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    private Integer status;
    private String desc;
}

