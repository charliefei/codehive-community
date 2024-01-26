package com.feirui.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import com.feirui.gateway.redis.RedisUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    private static final String authPermissionPrefix = "auth.permission";
    private static final String authRolePrefix = "auth.role";
    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return getAuthWithLoginId(authPermissionPrefix, loginId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return getAuthWithLoginId(authRolePrefix, loginId);
    }

    private List<String> getAuthWithLoginId(String authRolePrefix, Object loginId) {
        String key = redisUtil.buildKey(authRolePrefix, loginId.toString());
        String value = redisUtil.get(key);
        if (!StringUtils.hasLength(value)) {
            return Collections.emptyList();
        }
        // 返回此 loginId 拥有的角色列表
        return new Gson().fromJson(value, List.class);
    }
}
