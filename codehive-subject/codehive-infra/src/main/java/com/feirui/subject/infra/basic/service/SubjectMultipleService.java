package com.feirui.subject.infra.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.subject.infra.basic.entity.SubjectMultiple;

import java.util.List;

/**
 * 多选题信息表(SubjectMultiple)表服务接口
 *
 * @author makejava
 * @since 2023-12-29 17:24:07
 */
public interface SubjectMultipleService extends IService<SubjectMultiple> {

    List<SubjectMultiple> getBySubjectId(Long subjectId);
}

