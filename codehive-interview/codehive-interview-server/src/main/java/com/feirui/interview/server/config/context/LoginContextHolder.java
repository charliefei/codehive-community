package com.feirui.interview.server.config.context;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登陆上下文容器
 */
public class LoginContextHolder {

    private static final InheritableThreadLocal<Map<String, Object>> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> contextMap = getContextMap();
        contextMap.put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> contextMap = getContextMap();
        return contextMap.get(key);
    }

    public static String getLoginId() {
        return (String) get("loginId");
    }

    public static void remove() {
        CONTEXT_HOLDER.remove();
    }

    private static Map<String, Object> getContextMap() {
        Map<String, Object> contextMap = CONTEXT_HOLDER.get();
        if (Objects.isNull(contextMap)) {
            contextMap = new ConcurrentHashMap<>();
            CONTEXT_HOLDER.set(contextMap);
        }
        return contextMap;
    }

}
