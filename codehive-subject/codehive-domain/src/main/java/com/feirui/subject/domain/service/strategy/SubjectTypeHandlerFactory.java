package com.feirui.subject.domain.service.strategy;

import com.feirui.subject.common.enums.SubjectTypeEnum;
import com.feirui.subject.domain.service.strategy.handler.SubjectTypeHandler;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SubjectTypeHandlerFactory implements InitializingBean {
    @Resource
    private List<SubjectTypeHandler> subjectTypeHandlerList;

    private static final Map<SubjectTypeEnum, SubjectTypeHandler> handlerMap = new HashMap<>();

    public SubjectTypeHandler getHandler(Integer subjectType) {
        SubjectTypeHandler handler = handlerMap.get(SubjectTypeEnum.getByCode(subjectType));
        Preconditions.checkNotNull(handler, "无效的题目类型");
        return handler;
    }

    @Override
    public void afterPropertiesSet() {
        for (SubjectTypeHandler subjectTypeHandler : subjectTypeHandlerList) {
            handlerMap.put(subjectTypeHandler.getHandlerType(), subjectTypeHandler);
        }
    }
}
