package com.feirui.subject.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类类型枚举
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CategoryTypeEnum {
    PRIMARY(1, "一级分类"),
    SECOND(2, "二级分类");

    private Integer type;
    private String desc;

    public static List<Integer> types() {
        return Arrays.stream(CategoryTypeEnum.values())
                .map(CategoryTypeEnum::getType)
                .collect(Collectors.toList());
    }
}
