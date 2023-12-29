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
 * 题目标签表(SubjectLabel)实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("subject_label")
public class SubjectLabel implements Serializable {
    private static final long serialVersionUID = 839742290439552064L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 标签分类
     */
    @TableField("label_name")
    private String labelName;
    /**
     * 排序
     */
    @TableField("sort_num")
    private Integer sortNum;

    @TableField("category_id")
    private Long categoryId;
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
     * 更新人
     */
    @TableField("update_by")
    private String updateBy;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 删除标记
     */
    @TableField("is_deleted")
    private Integer isDeleted;
}

