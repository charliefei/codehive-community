package com.feirui.interview.server.service.impl;

import com.feirui.interview.api.enums.EngineEnum;
import com.feirui.interview.api.req.InterviewReq;
import com.feirui.interview.api.req.InterviewSubmitReq;
import com.feirui.interview.api.req.StartReq;
import com.feirui.interview.api.vo.InterviewQuestionVO;
import com.feirui.interview.api.vo.InterviewResultVO;
import com.feirui.interview.api.vo.InterviewVO;
import com.feirui.interview.server.dao.SubjectDao;
import com.feirui.interview.server.entity.po.SubjectLabel;
import com.feirui.interview.server.service.InterviewEngine;
import com.feirui.interview.server.service.InterviewService;
import com.feirui.interview.server.utils.PDFUtil;
import com.feirui.interview.server.utils.keyword.KeyWordUtil;
import com.google.common.base.Preconditions;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewServiceImpl implements InterviewService, ApplicationContextAware {

    private static final Map<String, InterviewEngine> engineMap = new HashMap<>();

    @Resource
    private SubjectDao subjectLabelDao;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Collection<InterviewEngine> engines = applicationContext.getBeansOfType(InterviewEngine.class).values();
        for (InterviewEngine engine : engines) {
            engineMap.put(engine.engineType().name(), engine);
        }
    }

    @Override
    public InterviewVO analyse(InterviewReq req) {
        List<String> keyWords = buildKeyWords(req.getUrl());
        InterviewEngine engine = engineMap.get(req.getEngine());
        Preconditions.checkArgument(!Objects.isNull(engine), "引擎不能为空！");
        return engine.analyse(keyWords);
    }

    @Override
    public InterviewVO analyseV2(InterviewReq req) {
        if (!req.getEngine().equals(EngineEnum.DEEPSEEK.name())) {
            throw new IllegalArgumentException("必须要是DEEPSEEK");
        }
        String pdfText = PDFUtil.getPdfText(req.getUrl());
        List<String> keyWords = buildKeyWords(req.getUrl());
        keyWords.add(0, pdfText);
        InterviewEngine engine = engineMap.get(req.getEngine());
        return engine.analyse(keyWords);
    }

    @Override
    public InterviewQuestionVO start(StartReq req) {
        InterviewEngine engine = engineMap.get(req.getEngine());
        Preconditions.checkArgument(!Objects.isNull(engine), "引擎不能为空！");
        return engine.start(req);
    }

    @Override
    public InterviewResultVO submit(InterviewSubmitReq req) {
        InterviewEngine engine = engineMap.get(req.getEngine());
        Preconditions.checkArgument(!Objects.isNull(engine), "引擎不能为空！");
        return engine.submit(req);
    }

    private List<String> buildKeyWords(String url) {
        String pdfText = PDFUtil.getPdfText(url);
        if (!KeyWordUtil.isInit()) {
            List<String> list = subjectLabelDao.listAllLabel().stream().map(SubjectLabel::getLabelName).collect(Collectors.toList());
            KeyWordUtil.addWord(list);
        }
        return KeyWordUtil.buildKeyWordsLists(pdfText);
    }

}
