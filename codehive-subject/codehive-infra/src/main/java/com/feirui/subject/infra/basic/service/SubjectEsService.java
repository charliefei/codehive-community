package com.feirui.subject.infra.basic.service;

import com.feirui.subject.common.entity.PageResult;
import com.feirui.subject.infra.basic.entity.SubjectInfoEs;

public interface SubjectEsService {

    boolean insert(SubjectInfoEs subjectInfoEs);

    PageResult<SubjectInfoEs> querySubjectList(SubjectInfoEs req);

}
