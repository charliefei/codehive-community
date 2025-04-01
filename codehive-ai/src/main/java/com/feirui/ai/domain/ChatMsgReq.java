package com.feirui.ai.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatMsgReq implements Serializable {

    private String query;

    private String history;

}
