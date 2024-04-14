package com.feirui.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import com.feirui.gateway.entity.AuthPermission;
import com.feirui.gateway.entity.AuthRole;
import com.feirui.gateway.redis.RedisUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        return getAuthWithLoginId(authPermissionPrefix, loginId.toString());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return getAuthWithLoginId(authRolePrefix, loginId.toString());
    }

    private List<String> getAuthWithLoginId(String prefix, String loginId) {
        String key = redisUtil.buildKey(prefix, loginId);
        String value = redisUtil.get(key);
        if (!StringUtils.hasLength(value)) {
            return Collections.emptyList();
        }
        List<String> authList = new LinkedList<>();
        if (authRolePrefix.equals(prefix)) {
            List<AuthRole> authRoleList = new Gson()
                    .fromJson(value, new TypeToken<List<AuthRole>>() {
                    }.getType());
            authList = authRoleList.stream().map(AuthRole::getRoleKey).collect(Collectors.toList());
        } else if (authPermissionPrefix.equals(prefix)) {
            List<AuthPermission> authPermissionList = new Gson()
                    .fromJson(value, new TypeToken<List<AuthPermission>>() {
                    }.getType());
            authList = authPermissionList.stream().map(AuthPermission::getPermissionKey).collect(Collectors.toList());
        }
        // 返回此 loginId 拥有的角色/权限列表
        return authList;
    }
}
