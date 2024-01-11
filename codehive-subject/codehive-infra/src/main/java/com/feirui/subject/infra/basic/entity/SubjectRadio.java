package com.feirui.subject.infra.basic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 单选题信息表(SubjectRadio)表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRadio implements Serializable {
    private static final long serialVersionUID = 1L;
    //主键
    private Long id;
    //题目id
    private Long subjectId;
    //a,b,c,d
    private Integer optionType;
    //选项内容
    private String optionContent;
    //是否正确
    private Integer isCorrect;
    //创建人
    private String createdBy;
    //创建时间
    private Date createdTime;
    //修改人
    private String updateBy;
    //修改时间
    private Date updateTime;

    private Integer isDeleted;
}

