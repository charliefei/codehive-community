package com.feirui.practice.api.req;

import com.feirui.practice.api.common.PageInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class GetPreSetReq implements Serializable {

    /**
     * 排序类型 1默认 2最新 3最热
     */
    private Integer orderType;

    /**
     * 分页信息
     */
    private PageInfo pageInfo;

    /**
     * 套题名称
     */
    private String setName;

}