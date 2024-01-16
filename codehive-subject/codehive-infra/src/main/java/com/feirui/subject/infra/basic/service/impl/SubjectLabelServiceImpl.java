package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.infra.basic.dao.SubjectLabelDao;
import com.feirui.subject.infra.basic.entity.SubjectLabel;
import com.feirui.subject.infra.basic.service.SubjectLabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题目标签表(SubjectLabel)表服务实现类
 */
@Service("subjectLabelService")
public class SubjectLabelServiceImpl extends ServiceImpl<SubjectLabelDao, SubjectLabel> implements SubjectLabelService {
    @Resource
    private SubjectLabelDao subjectLabelDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SubjectLabel queryById(Long id) {
        return this.subjectLabelDao.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param subjectLabel 实例对象
     * @return 实例对象
     */
    @Override
    public SubjectLabel insert(SubjectLabel subjectLabel) {
        this.subjectLabelDao.insert(subjectLabel);
        return subjectLabel;
    }

    /**
     * 修改数据
     *
     * @param subjectLabel 实例对象
     * @return 实例对象
     */
    @Override
    public SubjectLabel update(SubjectLabel subjectLabel) {
        this.subjectLabelDao.update(subjectLabel);
        return this.queryById(subjectLabel.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.subjectLabelDao.deleteById(id) > 0;
    }

    @Override
    public List<SubjectLabel> queryLabelsByIds(List<Long> labelIdList) {
        return this.lambdaQuery()
                .in(SubjectLabel::getId, labelIdList)
                .eq(SubjectLabel::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getStatus())
                .list();
    }

    @Override
    public List<SubjectLabel> queryLabelsByCategoryId(Long categoryId) {
        return lambdaQuery()
                .eq(SubjectLabel::getCategoryId, categoryId)
                .eq(SubjectLabel::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getStatus())
                .list();
    }
}
