package com.feirui.subject.domain.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目点赞表 bo
 *
 * @author charliefei
 * @since 2025-01-10 00:37:45
 */
@Data
public class SubjectLikedBO implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 题目id
     */
    private Long subjectId;

    /**
     * 点赞人id
     */
    private String likeUserId;

    /**
     * 点赞状态 1点赞 0不点赞
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;

}

