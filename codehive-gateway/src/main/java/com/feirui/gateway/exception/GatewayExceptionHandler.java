package com.feirui.gateway.exception;

import cn.dev33.satoken.exception.SaTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feirui.gateway.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关全局异常处理
 */
@Component
@Slf4j
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        ServerHttpResponse response = serverWebExchange.getResponse();
        int code;
        String message;
        if (throwable instanceof SaTokenException) {
            code = 401;
            message = "用户无权限~";
        } else {
            code = 500;
            message = "系统繁忙，请稍后再试~";
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            byte[] result;
            try {
                result = objectMapper.writeValueAsBytes(Result.fail(code, message));
            } catch (JsonProcessingException e) {
                log.error("GatewayExceptionHandler: {}", e.getMessage(), e);
                throw new RuntimeException(e);
            }
            return dataBufferFactory.wrap(result);
        }));
    }
}
