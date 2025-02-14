package com.feirui.auth.domain.convert;

import com.feirui.auth.domain.bo.AuthUserBO;
import com.feirui.auth.infra.basic.entity.AuthUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户bo转换器
 */
@Mapper
public interface AuthUserBOConverter {

    AuthUserBOConverter INSTANCE = Mappers.getMapper(AuthUserBOConverter.class);

    AuthUser convert(AuthUserBO authUserBO);

    AuthUserBO convert(AuthUser authUser);

    List<AuthUserBO> convert(List<AuthUser> userList);

}
