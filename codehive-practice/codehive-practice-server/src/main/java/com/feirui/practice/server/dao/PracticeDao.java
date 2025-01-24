package com.feirui.practice.server.dao;

import com.feirui.practice.server.entity.po.PracticePO;

public interface PracticeDao {

    /**
     * 根据练题id获取详情
     */
    PracticePO selectById(Long id);

    /**
     * 新增
     */
    int insert(PracticePO practicePO);

    /**
     * 更新练习详情
     */
    int update(PracticePO practicePO);

    /**
     * 删除练习
     */
    int deleteById(Long id);

}