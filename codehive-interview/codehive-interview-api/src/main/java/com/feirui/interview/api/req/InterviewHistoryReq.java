package com.feirui.interview.api.req;

import com.feirui.interview.api.common.PageInfo;
import lombok.Data;

import java.io.Serializable;

@Data
public class InterviewHistoryReq implements Serializable {

    /**
     * 分页信息
     */
    private PageInfo pageInfo;

}