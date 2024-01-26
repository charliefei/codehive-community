package com.feirui.auth.domain.service;

import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.domain.bo.AuthPermissionBO;
import com.feirui.auth.domain.convert.AuthPermissionBOConverter;
import com.feirui.auth.domain.redis.RedisUtil;
import com.feirui.auth.infra.basic.entity.AuthPermission;
import com.feirui.auth.infra.basic.service.AuthPermissionService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthPermissionDomainService {
    private static final String authPermissionPrefix = "auth.permission";
    @Resource
    private AuthPermissionService authPermissionService;
    @Resource
    private RedisUtil redisUtil;

    public Boolean add(AuthPermissionBO permissionBO) {
        AuthPermission authPermission = AuthPermissionBOConverter.INSTANCE.convert(permissionBO);
        authPermission.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        return authPermissionService.insert(authPermission) > 0;
    }

    public Boolean update(AuthPermissionBO permissionBO) {
        AuthPermission authPermission = AuthPermissionBOConverter.INSTANCE.convert(permissionBO);
        return authPermissionService.updateById(authPermission);
    }

    public Boolean delete(AuthPermissionBO authPermissionBO) {
        AuthPermission authPermission = new AuthPermission();
        authPermission.setId(authPermissionBO.getId());
        authPermission.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
        return authPermissionService.updateById(authPermission);
    }

    public List<String> getPermission(String userName) {
        String permissionKey = redisUtil.buildKey(authPermissionPrefix, userName);
        String permissionValue = redisUtil.get(permissionKey);
        if (StringUtils.isBlank(permissionValue)) {
            return Collections.emptyList();
        }
        List<AuthPermission> permissionList = new Gson().fromJson(permissionValue,
                new TypeToken<List<AuthPermission>>() {
                }.getType());
        return permissionList.stream()
                .map(AuthPermission::getPermissionKey)
                .collect(Collectors.toList());
    }
}
