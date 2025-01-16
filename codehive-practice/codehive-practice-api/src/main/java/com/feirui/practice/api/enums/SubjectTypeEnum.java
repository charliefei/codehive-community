package com.feirui.practice.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 题目类型枚举
 * 1单选 2多选 3判断 4简答
 */
@Getter
@AllArgsConstructor
public enum SubjectTypeEnum {
    RADIO(1,"单选"),
    MULTIPLE(2,"多选"),
    JUDGE(3,"判断"),
    BRIEF(4,"简答"),
    ;

    private final int code;
    private final String desc;

    public static SubjectTypeEnum getByCode(int codeVal){
        for(SubjectTypeEnum resultCodeEnum : SubjectTypeEnum.values()){
            if(resultCodeEnum.code == codeVal){
                return resultCodeEnum;
            }
        }
        return null;
    }
}
