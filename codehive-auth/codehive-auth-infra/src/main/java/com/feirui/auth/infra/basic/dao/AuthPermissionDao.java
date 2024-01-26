package com.feirui.auth.infra.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feirui.auth.infra.basic.entity.AuthPermission;

/**
 * (AuthPermission)表数据库访问层
 */
public interface AuthPermissionDao extends BaseMapper<AuthPermission> {
    /**
     * 新增数据
     *
     * @param authPermission 实例对象
     * @return 影响行数
     */
    int insert(AuthPermission authPermission);
}

