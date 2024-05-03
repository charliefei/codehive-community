package com.feirui.wx.controller;

import com.feirui.wx.handler.WxMsgHandler;
import com.feirui.wx.handler.WxMsgHandlerFactory;
import com.feirui.wx.utils.MessageUtil;
import com.feirui.wx.utils.SHA1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@Slf4j
public class WxController {
    private static final String token = "wx_token_fr";
    @Resource
    private WxMsgHandlerFactory wxMsgHandlerFactory;

    @GetMapping("callback")
    public String callback(@RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("echostr") String echostr) {
        log.info("get验签请求参数：signature:{}，timestamp:{}，nonce:{}，echostr:{}",
                signature, timestamp, nonce, echostr);
        // 根据sha1加密算法算出signature与微信给的signature进行比对验签
        String shaStr = SHA1.getSHA1(token, timestamp, nonce, "");
        if (signature.equals(shaStr)) {
            return echostr;
        }
        return "unknown";
    }

    @PostMapping(value = "callback", produces = "application/xml;charset=UTF-8")
    public String callback(
            @RequestBody String requestBody,
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam(value = "msg_signature", required = false) String msgSignature) {
        // 收发消息
        // <xml>
        //   <ToUserName><![CDATA[toUser]]></ToUserName>
        //   <FromUserName><![CDATA[fromUser]]></FromUserName>
        //   <CreateTime>1348831860</CreateTime>
        //   <MsgType><![CDATA[text]]></MsgType>
        //   <Content><![CDATA[具体的消息内容]]></Content>
        // </xml>
        // 事件推送
        // <xml>
        //   <ToUserName><![CDATA[toUser]]></ToUserName>
        //   <FromUserName><![CDATA[FromUser]]></FromUserName>
        //   <CreateTime>123456789</CreateTime>
        //   <MsgType><![CDATA[event]]></MsgType>
        //   <Event><![CDATA[subscribe]]></Event>
        // </xml>
        log.info("接收到微信xml消息：requestBody：\n{}", requestBody);
        Map<String, String> msgMap = MessageUtil.parseXml(requestBody);
        String msgType = msgMap.get("MsgType");
        String event = Optional.ofNullable(msgMap.get("Event")).orElse("");
        log.info("msgType:{},event:{}", msgType, event);

        StringBuilder sb = new StringBuilder();
        sb.append(msgType); // event/text/voice/video/image/music
        if (StringUtils.hasLength(event)) {
            sb.append(".");
            sb.append(event); // subscribe/SCAN/LOCATION/CLICK
        }
        String msgTypeKey = sb.toString();

        WxMsgHandler handler = wxMsgHandlerFactory.getHandlerByMsgType(msgTypeKey);
        if (Objects.isNull(handler)) return "unknown";
        String replyContent = handler.dealMsg(msgMap);
        log.info("replyContent:{}", replyContent);
        return replyContent;
    }
}
