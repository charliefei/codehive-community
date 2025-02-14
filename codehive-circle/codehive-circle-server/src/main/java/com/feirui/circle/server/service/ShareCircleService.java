package com.feirui.circle.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.circle.api.req.RemoveShareCircleReq;
import com.feirui.circle.api.req.SaveShareCircleReq;
import com.feirui.circle.api.req.UpdateShareCircleReq;
import com.feirui.circle.api.vo.ShareCircleVO;
import com.feirui.circle.server.entity.po.ShareCircle;

import java.util.List;

public interface ShareCircleService extends IService<ShareCircle> {

    /**
     * 圈子列表查询
     */
    List<ShareCircleVO> listResult();

    /**
     * 新增圈子
     */
    Boolean saveCircle(SaveShareCircleReq req);

    /**
     * 修改圈子
     */
    Boolean updateCircle(UpdateShareCircleReq req);

    /**
     * 删除圈子
     */
    Boolean removeCircle(RemoveShareCircleReq req);

}