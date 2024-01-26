package com.feirui.auth.application.convert;

import com.feirui.auth.application.dto.AuthPermissionDTO;
import com.feirui.auth.domain.bo.AuthPermissionBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 权限dto转换器
 */
@Mapper
public interface AuthPermissionDTOConverter {

    AuthPermissionDTOConverter INSTANCE = Mappers.getMapper(AuthPermissionDTOConverter.class);

    AuthPermissionBO convert(AuthPermissionDTO authPermissionDTO);

}
