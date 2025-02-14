package com.feirui.auth.application.convert;

import com.feirui.auth.domain.bo.AuthUserBO;
import com.feirui.auth.entity.AuthUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户dto转换器
 */
@Mapper
public interface AuthUserDTOConverter {
    AuthUserDTOConverter INSTANCE = Mappers.getMapper(AuthUserDTOConverter.class);

    AuthUserBO convert(AuthUserDTO authUserDTO);

    AuthUserDTO convert(AuthUserBO userInfo);

    List<AuthUserDTO> convert(List<AuthUserBO> authUserBO);
}
