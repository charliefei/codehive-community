package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.infra.basic.dao.SubjectRadioDao;
import com.feirui.subject.infra.basic.entity.SubjectRadio;
import com.feirui.subject.infra.basic.service.SubjectRadioService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单选题信息表(SubjectRadio)表服务实现类
 *
 * @author makejava
 * @since 2023-12-29 17:24:46
 */
@Service("subjectRadioService")
public class SubjectRadioServiceImpl extends ServiceImpl<SubjectRadioDao, SubjectRadio> implements SubjectRadioService {

    @Override
    public List<SubjectRadio> getBySubjectId(Long subjectId) {
        return lambdaQuery()
                .eq(SubjectRadio::getSubjectId, subjectId)
                .eq(SubjectRadio::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getStatus())
                .list();
    }
}

