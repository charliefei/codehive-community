package com.feirui.circle.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feirui.circle.api.req.RemoveShareCommentReq;
import com.feirui.circle.api.req.SaveShareCommentReplyReq;
import com.feirui.circle.server.entity.po.ShareCommentReply;

public interface ShareCommentReplyService extends IService<ShareCommentReply> {

    Boolean saveComment(SaveShareCommentReplyReq req);

    Boolean removeComment(RemoveShareCommentReq req);

}