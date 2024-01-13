package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.common.enums.IsDeletedFlagEnum;
import com.feirui.subject.infra.basic.dao.SubjectJudgeDao;
import com.feirui.subject.infra.basic.entity.SubjectJudge;
import com.feirui.subject.infra.basic.service.SubjectJudgeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 判断题(SubjectJudge)表服务实现类
 *
 * @author makejava
 * @since 2023-12-29 17:23:10
 */
@Service("subjectJudgeService")
public class SubjectJudgeServiceImpl extends ServiceImpl<SubjectJudgeDao, SubjectJudge> implements SubjectJudgeService {

    @Override
    public List<SubjectJudge> getBySubjectId(Long subjectId) {
        return lambdaQuery()
                .eq(SubjectJudge::getSubjectId, subjectId)
                .eq(SubjectJudge::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getStatus())
                .list();
    }
}

