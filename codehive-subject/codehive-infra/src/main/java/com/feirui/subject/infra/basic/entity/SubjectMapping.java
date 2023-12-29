package com.feirui.subject.infra.basic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目分类关系表(SubjectMapping)实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("subject_mapping")
public class SubjectMapping implements Serializable {
    private static final long serialVersionUID = 377976821443036401L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 题目id
     */
    @TableField("subject_id")
    private Long subjectId;
    /**
     * 分类id
     */
    @TableField("category_id")
    private Long categoryId;
    /**
     * 标签id
     */
    @TableField("label_id")
    private Long labelId;
    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;
    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;
    /**
     * 修改人
     */
    @TableField("update_by")
    private String updateBy;
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField("is_deleted")
    private Integer isDeleted;
}

