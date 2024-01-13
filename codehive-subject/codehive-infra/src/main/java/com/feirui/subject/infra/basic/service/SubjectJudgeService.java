package com.feirui.subject.infra.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.subject.infra.basic.entity.SubjectJudge;

import java.util.List;

/**
 * 判断题(SubjectJudge)表服务接口
 *
 * @author makejava
 * @since 2023-12-29 17:23:10
 */
public interface SubjectJudgeService extends IService<SubjectJudge> {

    List<SubjectJudge> getBySubjectId(Long subjectId);
}

