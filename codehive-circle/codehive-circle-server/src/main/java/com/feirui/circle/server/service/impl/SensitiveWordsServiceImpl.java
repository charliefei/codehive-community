package com.feirui.circle.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feirui.circle.server.dao.SensitiveWordsMapper;
import com.feirui.circle.server.entity.po.SensitiveWords;
import com.feirui.circle.server.service.SensitiveWordsService;
import org.springframework.stereotype.Service;

/**
 * 敏感词表 服务实现类
 */
@Service
public class SensitiveWordsServiceImpl extends ServiceImpl<SensitiveWordsMapper, SensitiveWords> implements SensitiveWordsService {

}
