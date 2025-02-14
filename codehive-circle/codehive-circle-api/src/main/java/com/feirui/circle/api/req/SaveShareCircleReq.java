package com.feirui.circle.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class SaveShareCircleReq implements Serializable {

    /**
     * 圈子名称
     */
    private String circleName;

    /**
     * 圈子图标
     */
    private String icon;

    /**
     * -1为一级,分类ID
     */
    private Long parentId = -1L;

}