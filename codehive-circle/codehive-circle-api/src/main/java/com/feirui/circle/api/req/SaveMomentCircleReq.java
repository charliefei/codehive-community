package com.feirui.circle.api.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaveMomentCircleReq implements Serializable {

    /**
     * 圈子ID
     */
    private Long circleId;

    /**
     * 动态内容
     */
    private String content;

    /**
     * 动态图片内容
     */
    private List<String> picUrlList;

}