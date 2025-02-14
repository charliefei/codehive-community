package com.feirui.circle.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feirui.circle.server.entity.po.ShareMoment;
import org.apache.ibatis.annotations.Param;

public interface ShareMomentMapper extends BaseMapper<ShareMoment> {

    void incrReplyCount(@Param("id") Long id, @Param("count") int count);

}