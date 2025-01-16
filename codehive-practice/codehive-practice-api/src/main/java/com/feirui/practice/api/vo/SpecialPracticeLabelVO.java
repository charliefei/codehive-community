package com.feirui.practice.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpecialPracticeLabelVO implements Serializable {

    private Long id;

    /**
     * 格式：分类id-标签id
     */
    private String assembleId;

    private String labelName;

}
