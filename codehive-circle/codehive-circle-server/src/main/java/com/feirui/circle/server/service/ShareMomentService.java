package com.feirui.circle.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.circle.api.common.PageResult;
import com.feirui.circle.api.req.GetShareMomentReq;
import com.feirui.circle.api.req.RemoveShareMomentReq;
import com.feirui.circle.api.req.SaveMomentCircleReq;
import com.feirui.circle.api.vo.ShareMomentVO;
import com.feirui.circle.server.entity.po.ShareMoment;

/**
 * 动态信息 服务类
 */
public interface ShareMomentService extends IService<ShareMoment> {

    Boolean saveMoment(SaveMomentCircleReq req);

    PageResult<ShareMomentVO> getMoments(GetShareMomentReq req);

    Boolean removeMoment(RemoveShareMomentReq req);

    void incrReplyCount(Long id, int count);

}
