package com.feirui.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Sa-Token权限认证配置类
 */
@Configuration
public class SaTokenConfigure {
    /**
     * 注册 Sa-Token全局过滤器
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    /* 拦截全部path */
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    System.out.println("------- 前端访问path：" + SaHolder.getRequest().getRequestPath());
                    // 登录校验 -- 拦截所有路由，并排除/auth/user/doLogin用于开放登录
                    SaRouter.match("/auth/**",
                            "/auth/user/doLogin",
                            r -> StpUtil.checkRole("admin"));
                    // oss文件上传接口
                    SaRouter.match("/oss/**",
                            r -> StpUtil.checkLogin());
                    // 刷题模块题目上传接口
                    SaRouter.match("/subject/subject/add",
                            r -> StpUtil.checkPermission("subject:add"));
                    SaRouter.match("/subject/**",
                            r -> StpUtil.checkLogin());
                });
    }
}
