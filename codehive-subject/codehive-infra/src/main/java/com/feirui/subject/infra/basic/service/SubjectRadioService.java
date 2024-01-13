package com.feirui.subject.infra.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.subject.infra.basic.entity.SubjectRadio;

import java.util.List;

/**
 * 单选题信息表(SubjectRadio)表服务接口
 *
 * @author makejava
 * @since 2023-12-29 17:24:46
 */
public interface SubjectRadioService extends IService<SubjectRadio> {

    List<SubjectRadio> getBySubjectId(Long subjectId);
}

