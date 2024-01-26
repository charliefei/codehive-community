package com.feirui.auth.domain.service;

import com.feirui.auth.common.enums.AuthUserStatusEnum;
import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.domain.bo.AuthUserBO;
import com.feirui.auth.domain.convert.AuthUserBOConverter;
import com.feirui.auth.infra.basic.entity.AuthUser;
import com.feirui.auth.infra.basic.service.AuthUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AuthUserDomainService {
    @Resource
    private AuthUserService authUserService;

    public Boolean register(AuthUserBO authUserBO) {
        // 校验用户是否存在
        List<AuthUser> existUsers = authUserService.queryByUsername(authUserBO.getUserName());
        if (!CollectionUtils.isEmpty(existUsers)) {
            return Boolean.TRUE;
        }
        // 基本信息持久化入库
        AuthUser authUser = AuthUserBOConverter.INSTANCE.convert(authUserBO);
        authUser.setStatus(AuthUserStatusEnum.OPEN.getCode());
        authUser.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        boolean success = authUserService.save(authUser);
        // 建立一个用户与角色的关联

        // 根据roleId查权限

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
}
