package com.feirui.subject.infra.basic.entity;

import com.feirui.subject.common.entity.PageInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class SubjectInfoEs extends PageInfo implements Serializable {

    /**
     * 题目id
     */
    private Long subjectId;

    /**
     * es文档id
     */
    private Long docId;

    /**
     * 题目名称
     */
    private String subjectName;

    /**
     * 题目答案
     */
    private String subjectAnswer;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 题目类型
     */
    private Integer subjectType;

    /**
     * es搜索关键字
     */
    private String keyWord;

    /**
     * es搜索分数
     */
    private BigDecimal score;

}
