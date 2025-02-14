package com.feirui.auth.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.auth.common.enums.IsDeletedFlagEnum;
import com.feirui.auth.infra.basic.dao.AuthUserDao;
import com.feirui.auth.infra.basic.entity.AuthUser;
import com.feirui.auth.infra.basic.service.AuthUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (AuthUser)表服务实现类
 */
@Service("authUserService")
public class AuthUserServiceImpl extends ServiceImpl<AuthUserDao, AuthUser> implements AuthUserService {

    @Resource
    private AuthUserDao authUserDao;

    @Override
    public List<AuthUser> queryByUsername(String userName) {
        return lambdaQuery()
                .eq(AuthUser::getUserName, userName)
                .eq(AuthUser::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .list();
    }

    @Override
    public void updateByUsername(AuthUser authUser) {
        lambdaUpdate()
                .eq(AuthUser::getUserName, authUser.getUserName())
                .update(authUser);
    }

    @Override
    public List<AuthUser> listUserInfoByIds(List<String> userNameList) {
        return authUserDao.listUserInfoByIds(userNameList);
    }

}

