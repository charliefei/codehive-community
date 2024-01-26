package com.feirui.gateway.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResultCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(500, "失败")
    ;

    private Integer code;
    private String desc;
}
