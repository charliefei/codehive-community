package com.feirui.wx.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WxMsgHandlerFactory implements InitializingBean {
    private static final Map<WxMsgTypeEnum, WxMsgHandler> handlerMap = new HashMap<>();
    @Resource
    private List<WxMsgHandler> handlers;

    public WxMsgHandler getHandlerByMsgType(String msgType) {
        return handlerMap.get(WxMsgTypeEnum.getByMsgType(msgType));
    }

    @Override
    public void afterPropertiesSet() {
        handlers.forEach(handler -> handlerMap.put(handler.getWxChatMsgType(), handler));
    }
}
