package com.feirui.practice.api.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetPracticeSubjectsReq implements Serializable {

    /**
     * 套题id
     */
    private Long setId;

}