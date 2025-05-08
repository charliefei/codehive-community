package com.feirui.auth.domain.service;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.feirui.auth.common.enums.AuthUserStatusEnum;
import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.domain.bo.AuthUserBO;
import com.feirui.auth.domain.constants.AuthConstant;
import com.feirui.auth.domain.convert.AuthUserBOConverter;
import com.feirui.auth.domain.redis.RedisUtil;
import com.feirui.auth.infra.basic.entity.*;
import com.feirui.auth.infra.basic.service.*;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthUserDomainService {
    private static final String authPermissionPrefix = "auth.permission";
    private static final String authRolePrefix = "auth.role";
    private static final String LOGIN_PREFIX = "loginCode";
    @Resource
    private AuthUserService authUserService;
    @Resource
    private AuthUserRoleService authUserRoleService;
    @Resource
    private AuthRoleService authRoleService;
    @Resource
    private AuthRolePermissionService authRolePermissionService;
    @Resource
    private AuthPermissionService authPermissionService;
    @Resource
    private RedisUtil redisUtil;

    @Transactional(rollbackFor = Exception.class)
    public Boolean register(AuthUserBO authUserBO) {
        // 校验用户是否存在
        List<AuthUser> existUsers = authUserService.queryByUsername(authUserBO.getUserName());
        if (!CollectionUtils.isEmpty(existUsers)) {
            return Boolean.TRUE;
        }

        // 基本信息持久化入库
        AuthUser authUser = AuthUserBOConverter.INSTANCE.convert(authUserBO);
        if (StringUtils.isNotBlank(authUser.getPassword())) {
            authUser.setPassword(SaSecureUtil.md5BySalt(authUser.getPassword(), "charlie"));
        }
        if (StringUtils.isBlank(authUser.getAvatar())) {
            authUser.setAvatar("http://117.72.10.84:9000/user/icon/微信图片_20231203153718(1).png");
        }
        if (StringUtils.isBlank(authUser.getNickName())) {
            authUser.setNickName("用户" + authUser.getUserName());
        }
        authUser.setStatus(AuthUserStatusEnum.OPEN.getCode());
        authUser.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        boolean success = authUserService.save(authUser);

        // 建立一个初步的用户与角色(NORMAL_USER)的关联，入库
        AuthRole authRole = authRoleService.queryByRoleKey(AuthConstant.NORMAL_USER);
        AuthUserRole authUserRole = new AuthUserRole();
        authUserRole.setUserId(authUser.getId());
        authUserRole.setRoleId(authRole.getId());
        authUserRole.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        authUserRoleService.save(authUserRole);

        // 将该用户所拥有的角色，写入缓存
        String roleCacheKey = redisUtil.buildKey(authRolePrefix, authUser.getUserName());
        List<AuthRole> roles = new LinkedList<>();
        roles.add(authRole);
        redisUtil.set(roleCacheKey, new Gson().toJson(roles));

        // 将该用户所拥有的权限，写入缓存
        List<Long> permissionIds = authRolePermissionService.queryByRoleId(authRole.getId())
                .stream()
                .map(AuthRolePermission::getPermissionId)
                .collect(Collectors.toList());
        List<AuthPermission> authPermissions = new LinkedList<>();
        if (!CollectionUtils.isEmpty(permissionIds)) {
            authPermissions = authPermissionService.queryByIds(permissionIds);
        }
        String permissionCacheKey = redisUtil.buildKey(authPermissionPrefix, authUser.getUserName());
        redisUtil.set(permissionCacheKey, new Gson().toJson(authPermissions));
        return success;
    }

    public void update(AuthUserBO authUserBO) {
        AuthUser authUser = AuthUserBOConverter.INSTANCE.convert(authUserBO);
        authUserService.updateByUsername(authUser);
        // 有任何的更新，都要与缓存进行同步的修改
    }

    public void delete(AuthUserBO authUserBO) {
        AuthUser authUser = new AuthUser();
        authUser.setId(authUserBO.getId());
        authUser.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        authUserService.updateById(authUser);
        // 有任何的更新，都要与缓存进行同步的修改
    }

    public void changeStatus(AuthUserBO authUserBO) {
        AuthUser authUser = new AuthUser();
        authUser.setId(authUserBO.getId());
        authUser.setStatus(authUserBO.getStatus());
        authUserService.updateById(authUser);
        // 有任何的更新，都要与缓存进行同步的修改
    }

    public AuthUserBO getUserInfo(AuthUserBO authUserBO) {
        List<AuthUser> userList = authUserService.queryByUsername(authUserBO.getUserName());
        if (CollectionUtils.isEmpty(userList)) {
            return new AuthUserBO();
        }
        AuthUser user = userList.get(0);
        return AuthUserBOConverter.INSTANCE.convert(user);
    }

    public List<AuthUserBO> listUserInfoByIds(List<String> userNameList) {
        List<AuthUser> userList = authUserService.listUserInfoByIds(userNameList);
        if (CollectionUtils.isEmpty(userList)) {
            return Collections.emptyList();
        }
        return AuthUserBOConverter.INSTANCE.convert(userList);
    }

    @Transactional(rollbackFor = Exception.class)
    public SaTokenInfo doLogin(String validCode) {
        String loginKey = redisUtil.buildKey(LOGIN_PREFIX, validCode);
        // loginId实际就是微信扫码用户的openId
        String loginId = redisUtil.get(loginKey);
        if (StringUtils.isBlank(loginId)) {
            return null;
        }
        AuthUserBO authUserBO = new AuthUserBO();
        // 微信给的openId就是satoken的loginId，loginId又对应数据库用户表里的username字段
        authUserBO.setUserName(loginId);
        // 扫码的同时未注册的新用户自动完成注册操作
        register(authUserBO);
        // satoken登录，传入loginId用户获取用户相关的角色和权限
        StpUtil.login(loginId);
        return StpUtil.getTokenInfo();
    }
}
