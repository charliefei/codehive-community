package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.infra.basic.dao.SubjectInfoDao;
import com.feirui.subject.infra.basic.entity.SubjectInfo;
import com.feirui.subject.infra.basic.service.SubjectInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 题目信息表(SubjectInfo)表服务实现类
 */
@Service("subjectInfoService")
public class SubjectInfoServiceImpl extends ServiceImpl<SubjectInfoDao, SubjectInfo> implements SubjectInfoService {
    @Resource
    private SubjectInfoDao subjectInfoDao;

    @Override
    public int countByCondition(SubjectInfo subjectInfo, Long categoryId, Long labelId) {
        return subjectInfoDao.countByCondition(subjectInfo, categoryId, labelId);
    }

    @Override
    public List<SubjectInfo> queryPage(SubjectInfo subjectInfo, Long categoryId, Long labelId, int start, int pageSize) {
        return subjectInfoDao.queryPage(subjectInfo, categoryId, labelId, start, pageSize);
    }
}

