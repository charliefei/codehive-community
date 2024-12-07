package com.feirui.gateway.filter;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 响应式的全局登录过滤器：获取请求头中携带的token
 */
@Component
@Slf4j
public class LoginFilter implements GlobalFilter {

    @Override
    @SneakyThrows
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        log.info("LoginFilter.filter.url:{}", url);
        // 请求登录接口，直接放行
        if ("/auth/user/doLogin".equals(url)) {
            return chain.filter(exchange);
        }
        // satoken解析token，得到loginId->对应用户表中的userName->微信扫码的openid
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        log.info("LoginFilter.filter.tokenInfo:{}", new Gson().toJson(tokenInfo));
        String loginId = (String) tokenInfo.getLoginId();
        // 将loginId放入header中传递给微服务使用
        ServerHttpRequest.Builder mutate = request.mutate();
        mutate.header("loginId", loginId);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

}
