package com.feirui.auth;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.alibaba.fastjson.JSON;
import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.domain.constants.AuthConstant;
import com.feirui.auth.domain.redis.RedisUtil;
import com.feirui.auth.domain.service.AuthUserDomainService;
import com.feirui.auth.infra.basic.entity.AuthPermission;
import com.feirui.auth.infra.basic.entity.AuthRole;
import com.feirui.auth.infra.basic.entity.AuthRolePermission;
import com.feirui.auth.infra.basic.entity.AuthUserRole;
import com.feirui.auth.infra.basic.service.AuthPermissionService;
import com.feirui.auth.infra.basic.service.AuthRolePermissionService;
import com.feirui.auth.infra.basic.service.AuthRoleService;
import com.feirui.auth.infra.basic.service.AuthUserRoleService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SpringBootTest
public class AuthTests {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AuthUserDomainService authUserDomainService;
    @Resource
    private AuthRoleService authRoleService;
    @Resource
    private AuthPermissionService authPermissionService;
    @Resource
    private AuthRolePermissionService authRolePermissionService;
    @Resource
    private AuthUserRoleService authUserRoleService;

    @Test
    public void testLogin() {
        String validateCode = "123";
        String validateCodeKey = redisUtil.buildKey("loginCode", validateCode);
        redisUtil.setNx(validateCodeKey, "oYA4HtxySdme-hGYlWGx-g3TktZE", 5L, TimeUnit.DAYS);
        // SaTokenInfo tokenInfo = authUserDomainService.doLogin(validateCode);
        // {"isLogin":true,"loginDevice":"default-device","loginId":"oYA4HtxySdme-hGYlWGx-g3TktZE","loginType":"login","sessionTimeout":2592000,"tokenActiveTimeout":-1,"tokenName":"satoken","tokenSessionTimeout":-2,"tokenTimeout":2592000,"tokenValue":"BTUEFIZsSXNjiNGEoB18gyHHRrAtoI0i"}
        // System.out.println(JSON.toJSONString(tokenInfo));
    }

    @Test
    public void testInitAuth() {
        // 建立一个初步的用户与角色(NORMAL_USER)的关联，入库
        AuthRole authRole = authRoleService.queryByRoleKey(AuthConstant.ADMIN_USER);
        AuthUserRole authUserRole = new AuthUserRole();
        authUserRole.setUserId(1L);
        authUserRole.setRoleId(authRole.getId());
        authUserRole.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        authUserRoleService.save(authUserRole);

        // 将该用户所拥有的角色，写入缓存
        String roleCacheKey = redisUtil.buildKey("auth.role", "oYA4HtxySdme-hGYlWGx-g3TktZE");
        List<AuthRole> roles = new LinkedList<>();
        roles.add(authRole);
        redisUtil.set(roleCacheKey, new Gson().toJson(roles));

        // 将该用户所拥有的权限，写入缓存
        List<Long> permissionIds = authRolePermissionService.queryByRoleId(authRole.getId())
                .stream()
                .map(AuthRolePermission::getPermissionId)
                .collect(Collectors.toList());
        List<AuthPermission> authPermissions = authPermissionService.queryByIds(permissionIds);
        String permissionCacheKey = redisUtil.buildKey("auth.permission", "oYA4HtxySdme-hGYlWGx-g3TktZE");
        redisUtil.set(permissionCacheKey, new Gson().toJson(authPermissions));
    }

}
