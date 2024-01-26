package com.feirui.auth.infra.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.auth.infra.basic.dao.AuthUserRoleDao;
import com.feirui.auth.infra.basic.entity.AuthUserRole;
import com.feirui.auth.infra.basic.service.AuthUserRoleService;
import org.springframework.stereotype.Service;

/**
 * (AuthUserRole)表服务实现类
 */
@Service("authUserRoleService")
public class AuthUserRoleServiceImpl extends ServiceImpl<AuthUserRoleDao, AuthUserRole> implements AuthUserRoleService {

}

