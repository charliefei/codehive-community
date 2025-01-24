package com.feirui.practice.server.dao;

import com.feirui.practice.server.entity.po.PracticeSetPO;

public interface PracticeSetDao {

    /**
     * 新增套题
     */
    int add(PracticeSetPO po);

    PracticeSetPO selectById(Long setId);

}