package com.feirui.auth.infra.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feirui.auth.infra.basic.entity.AuthUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (AuthUser)表数据库访问层
 *
 * @author makejava
 * @since 2024-01-24 15:23:41
 */
public interface AuthUserDao extends BaseMapper<AuthUser> {

    List<AuthUser> listUserInfoByIds(@Param("userNameList") List<String> userNameList);

}

