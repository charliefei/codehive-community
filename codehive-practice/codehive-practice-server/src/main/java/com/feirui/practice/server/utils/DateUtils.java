package com.feirui.practice.server.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtils {

    /**
     * 字符串转时间
     */
    public static Date parseStrToDate(String timestamp) {
        try {
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sm.parse(timestamp);
        } catch (Exception e) {
            log.error("parseDate异常 arg = {}, error = {}", timestamp, e.getMessage(), e);
            return null;
        }
    }

}