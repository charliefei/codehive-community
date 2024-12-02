package com.feirui.subject.application.interceptor;

import com.feirui.subject.common.context.LoginContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;

/**
 * feign请求拦截器：rpc某一服务时，往请求头中设置loginId等参数，携带给服务提供者
 */
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String loginId = LoginContextHolder.getLoginId();
        if (StringUtils.hasLength(loginId)) {
            requestTemplate.header("loginId", loginId);
        }
    }
}
