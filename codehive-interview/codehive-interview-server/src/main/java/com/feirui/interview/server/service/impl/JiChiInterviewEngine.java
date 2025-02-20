package com.feirui.interview.server.service.impl;

import com.feirui.interview.api.enums.EngineEnum;
import com.feirui.interview.api.vo.InterviewVO;
import com.feirui.interview.server.dao.SubjectDao;
import com.feirui.interview.server.entity.po.SubjectCategory;
import com.feirui.interview.server.entity.po.SubjectLabel;
import com.feirui.interview.server.service.InterviewEngine;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JiChiInterviewEngine implements InterviewEngine {

    private List<SubjectLabel> labels;
    private Map<Long, SubjectCategory> categoryMap;
    @Resource
    private SubjectDao subjectDao;

    @PostConstruct
    public void init() {
        labels = subjectDao.listAllLabel();
        categoryMap = subjectDao.listAllCategory().stream().collect(Collectors.toMap(SubjectCategory::getId, Function.identity()));
    }

    @Override
    public EngineEnum engineType() {
        return EngineEnum.JI_CHI;
    }

    @Override
    public InterviewVO analyse(List<String> KeyWords) {
        if (CollectionUtils.isEmpty(KeyWords)) {
            return new InterviewVO();
        }
        List<InterviewVO.Interview> views = this.labels.stream().filter(item -> KeyWords.contains(item.getLabelName())).map(item -> {
            InterviewVO.Interview interview = new InterviewVO.Interview();
            SubjectCategory subjectCategory = categoryMap.get(item.getCategoryId());
            if (Objects.nonNull(subjectCategory)) {
                interview.setKeyWord(String.format("%s-%s", subjectCategory.getCategoryName(), item.getLabelName()));
            } else {
                interview.setKeyWord(item.getLabelName());
            }
            interview.setCategoryId(item.getCategoryId());
            interview.setLabelId(item.getId());
            return interview;
        }).collect(Collectors.toList());

        InterviewVO vo = new InterviewVO();
        vo.setQuestionList(views);
        return vo;
    }

}
