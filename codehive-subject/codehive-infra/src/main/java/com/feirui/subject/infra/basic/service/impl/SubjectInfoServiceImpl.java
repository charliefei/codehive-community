package com.feirui.subject.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.subject.infra.basic.dao.SubjectInfoDao;
import com.feirui.subject.infra.basic.entity.SubjectInfo;
import com.feirui.subject.infra.basic.service.SubjectInfoService;
import org.springframework.stereotype.Service;

/**
 * 题目信息表(SubjectInfo)表服务实现类
 */
@Service("subjectInfoService")
public class SubjectInfoServiceImpl extends ServiceImpl<SubjectInfoDao, SubjectInfo> implements SubjectInfoService {

}

