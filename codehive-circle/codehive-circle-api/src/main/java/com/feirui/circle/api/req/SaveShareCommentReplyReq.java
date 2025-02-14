package com.feirui.circle.api.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SaveShareCommentReplyReq implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 原始动态ID
     */
    private Long momentId;

    /**
     * 回复类型 1评论 2回复
     */
    private Integer replyType;

    /**
     * 评论目标id 评论则是动态ID 回复则是评论内容ID
     */
    private Long targetId;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片内容
     */
    private List<String> picUrlList;

}