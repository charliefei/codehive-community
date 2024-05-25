package com.feirui.subject.application.controller;

import com.alibaba.fastjson.JSON;
import com.feirui.subject.infra.entity.UserInfo;
import com.feirui.subject.infra.rpc.UserRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
public class TestFeignController {
    @Resource
    private UserRpc userRpc;

    @GetMapping("/feign/userInfo")
    public UserInfo userInfo() {
        UserInfo userInfo = userRpc.getUserInfo("charlie");
        log.info("userInfo: {}", JSON.toJSONString(userInfo));
        return userInfo;
    }
}
