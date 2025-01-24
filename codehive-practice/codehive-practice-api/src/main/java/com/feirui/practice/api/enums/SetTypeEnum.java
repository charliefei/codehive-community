package com.feirui.practice.api.enums;

import lombok.Getter;

@Getter
public enum SetTypeEnum {

    /**
     * 实时生成
     */
    REAL(1, "实时生成"),

    /**
     * 预设套题
     */
    PRESET(2, "预设套题");

    final private int code;
    final private String desc;

    SetTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}