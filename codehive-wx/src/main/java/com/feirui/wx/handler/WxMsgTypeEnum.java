package com.feirui.wx.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 微信xml消息的消息类型枚举
 */
@Getter
@AllArgsConstructor
public enum WxMsgTypeEnum {
    SUBSCRIBE("event.subscribe", "用户关注事件"),
    CHAT_TEXT("text", "接收用户文本消息");

    private final String msgType;
    private final String msgDesc;

    public static WxMsgTypeEnum getByMsgType(String msgType) {
        for (WxMsgTypeEnum wxMsgTypeEnum : WxMsgTypeEnum.values()) {
            if (wxMsgTypeEnum.msgType.equals(msgType)) {
                return wxMsgTypeEnum;
            }
        }
        return null;
    }
}
