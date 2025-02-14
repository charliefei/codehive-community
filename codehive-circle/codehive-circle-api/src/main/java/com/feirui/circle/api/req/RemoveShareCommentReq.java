package com.feirui.circle.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class RemoveShareCommentReq implements Serializable {

    private Long id;

    /**
     * 回复类型 1评论 2回复
     */
    private Integer replyType;

}