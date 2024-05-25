package com.feirui.subject.infra.rpc;

import com.feirui.auth.api.UserFeignService;
import com.feirui.auth.entity.AuthUserDTO;
import com.feirui.auth.entity.Result;
import com.feirui.subject.infra.entity.UserInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserRpc {
    @Resource
    private UserFeignService userFeignService;

    public UserInfo getUserInfo(String userName) {
        AuthUserDTO authUserDTO = new AuthUserDTO();
        authUserDTO.setUserName(userName);
        Result<AuthUserDTO> result = userFeignService.getUserInfo(authUserDTO);
        if (!result.getSuccess()) {
            return new UserInfo();
        }
        return UserInfo.builder()
                .userName(result.getData().getUserName())
                .nickName(result.getData().getNickName())
                .build();
    }
}
