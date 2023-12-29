package com.feirui.subject.infra.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.subject.infra.basic.entity.SubjectLabel;

import java.util.List;

/**
 * 题目标签表(SubjectLabel)表服务接口
 */
public interface SubjectLabelService extends IService<SubjectLabel> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SubjectLabel queryById(Long id);

    /**
     * 新增数据
     *
     * @param subjectLabel 实例对象
     * @return 实例对象
     */
    SubjectLabel insert(SubjectLabel subjectLabel);

    /**
     * 修改数据
     *
     * @param subjectLabel 实例对象
     * @return 实例对象
     */
    SubjectLabel update(SubjectLabel subjectLabel);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 根据标签id集合查询数据
     * @param labelIdList 标签id集合
     * @return 标签集合
     */
    List<SubjectLabel> queryLabelsByIds(List<Long> labelIdList);
}
