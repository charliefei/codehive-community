package com.feirui.auth.infra.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.auth.infra.basic.entity.AuthUser;

import java.util.List;

/**
 * (AuthUser)表服务接口
 */
public interface AuthUserService extends IService<AuthUser> {

    List<AuthUser> queryByUsername(String userName);

    void updateByUsername(AuthUser authUser);
}

