package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.infra.basic.dao.SubjectMappingDao;
import com.feirui.subject.infra.basic.entity.SubjectMapping;
import com.feirui.subject.infra.basic.service.SubjectMappingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题目分类关系表(SubjectMapping)表服务实现类
 */
@Service("subjectMappingService")
public class SubjectMappingServiceImpl extends ServiceImpl<SubjectMappingDao, SubjectMapping> implements SubjectMappingService {
    @Resource
    private SubjectMappingDao subjectMappingDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SubjectMapping queryById(Long id) {
        return this.subjectMappingDao.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param subjectMapping 实例对象
     * @return 实例对象
     */
    @Override
    public SubjectMapping insert(SubjectMapping subjectMapping) {
        this.subjectMappingDao.insert(subjectMapping);
        return subjectMapping;
    }

    /**
     * 修改数据
     *
     * @param subjectMapping 实例对象
     * @return 实例对象
     */
    @Override
    public SubjectMapping update(SubjectMapping subjectMapping) {
        this.subjectMappingDao.update(subjectMapping);
        return this.queryById(subjectMapping.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.subjectMappingDao.deleteById(id) > 0;
    }

    /**
     * 根据分类id查询分类与标签映射关系
     * @param categoryId 分类id
     */
    @Override
    public List<SubjectMapping> queryMappingsByCategoryId(Long categoryId) {
        return this.lambdaQuery()
                .eq(SubjectMapping::getCategoryId, categoryId)
                .eq(SubjectMapping::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getStatus())
                .list();
    }

    /**
     * 根据题目id查询分类与标签映射关系
     * @param subjectId 题目id
     */
    @Override
    public List<SubjectMapping> queryMappingsBySubjectId(Long subjectId) {
        return this.lambdaQuery()
                .eq(SubjectMapping::getSubjectId, subjectId)
                .eq(SubjectMapping::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getStatus())
                .list();
    }
}
