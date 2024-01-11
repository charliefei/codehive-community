package com.feirui.subject.infra.basic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 判断题(SubjectJudge)表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectJudge implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //题目id
    private Long subjectId;
    //是否正确
    private Integer isCorrect;
    //创建人
    private String createdBy;
    //创建时间
    private Date createdTime;
    //更新人
    private String updateBy;
    //更新时间
    private Date updateTime;

    private Integer isDeleted;
}

