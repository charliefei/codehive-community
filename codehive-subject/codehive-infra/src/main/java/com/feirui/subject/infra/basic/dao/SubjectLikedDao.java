package com.feirui.subject.infra.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feirui.subject.infra.basic.entity.SubjectLiked;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目点赞表(SubjectLiked)表数据库访问层
 *
 * @author charliefei
 * @since 2025-01-10 00:24:47
 */
public interface SubjectLikedDao extends BaseMapper<SubjectLiked> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SubjectLiked> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SubjectLiked> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SubjectLiked> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SubjectLiked> entities);

    List<SubjectLiked> queryPage(@Param("entity") SubjectLiked subjectLiked, @Param("start") int start, @Param("pageSize") Integer pageSize);
}

