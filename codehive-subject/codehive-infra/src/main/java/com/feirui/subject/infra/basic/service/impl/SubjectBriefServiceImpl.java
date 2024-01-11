package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.infra.basic.dao.SubjectBriefDao;
import com.feirui.subject.infra.basic.entity.SubjectBrief;
import com.feirui.subject.infra.basic.service.SubjectBriefService;
import org.springframework.stereotype.Service;

/**
 * 简答题(SubjectBrief)表服务实现类
 */
@Service("subjectBriefService")
public class SubjectBriefServiceImpl extends ServiceImpl<SubjectBriefDao, SubjectBrief> implements SubjectBriefService {

}

