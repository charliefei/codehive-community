package com.feirui.wx.handler;

import java.util.Map;

public interface WxMsgHandler {
    WxMsgTypeEnum getWxChatMsgType();

    String dealMsg(Map<String, String> messageMap);
}
