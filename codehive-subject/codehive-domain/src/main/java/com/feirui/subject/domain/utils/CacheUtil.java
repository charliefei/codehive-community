package com.feirui.subject.domain.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class CacheUtil<V> {
    private static final Cache<String, String> localCache = CacheBuilder.newBuilder()
            .maximumSize(5000)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    public List<V> getCacheValue(String cacheKey, Class<V> resultType, Supplier<List<V>> supplier) {
        List<V> result;
        String cacheValue = localCache.getIfPresent(cacheKey);
        if (StringUtils.hasLength(cacheValue)) {
            result = JSON.parseArray(cacheValue, resultType);
        } else {
            result = supplier.get();
            if (!CollectionUtils.isEmpty(result)) {
                localCache.put(cacheKey, JSON.toJSONString(result));
            }
        }
        return result;
    }
}
