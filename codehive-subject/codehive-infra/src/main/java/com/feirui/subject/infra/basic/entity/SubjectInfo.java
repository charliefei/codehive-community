package com.feirui.subject.infra.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 题目信息表(SubjectInfo)表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //题目名称
    private String subjectName;
    //题目难度
    private Integer subjectDifficult;
    //出题人名
    private String settleName;
    //题目类型 1单选 2多选 3判断 4简答
    private Integer subjectType;
    //题目分数
    private Integer subjectScore;
    //题目解析
    private String subjectParse;
    //创建人
    private String createdBy;
    //创建时间
    private Date createdTime;
    //修改人
    private String updateBy;
    //修改时间
    private Date updateTime;

    private Integer isDeleted;

    @TableField(exist = false)
    private Integer subjectCount;
}

