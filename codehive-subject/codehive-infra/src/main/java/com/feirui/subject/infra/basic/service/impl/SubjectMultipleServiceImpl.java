package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.infra.basic.dao.SubjectMultipleDao;
import com.feirui.subject.infra.basic.entity.SubjectMultiple;
import com.feirui.subject.infra.basic.service.SubjectMultipleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 多选题信息表(SubjectMultiple)表服务实现类
 *
 * @author makejava
 * @since 2023-12-29 17:24:07
 */
@Service("subjectMultipleService")
public class SubjectMultipleServiceImpl extends ServiceImpl<SubjectMultipleDao, SubjectMultiple> implements SubjectMultipleService {

    @Override
    public List<SubjectMultiple> getBySubjectId(Long subjectId) {
        return lambdaQuery()
                .eq(SubjectMultiple::getSubjectId, subjectId)
                .eq(SubjectMultiple::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getStatus())
                .list();
    }
}

