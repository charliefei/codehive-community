package com.feirui.auth;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.alibaba.fastjson.JSON;
import com.feirui.auth.domain.redis.RedisUtil;
import com.feirui.auth.domain.service.AuthUserDomainService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class AuthTests {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AuthUserDomainService authUserDomainService;

    @Test
    public void testLogin() {
        String validateCode = "123456";
        String validateCodeKey = redisUtil.buildKey("loginCode", validateCode);
        redisUtil.setNx(validateCodeKey, "1", 5L, TimeUnit.MINUTES);
        SaTokenInfo tokenInfo = authUserDomainService.doLogin(validateCode);
        System.out.println(JSON.toJSONString(tokenInfo));
    }

}
