package com.feirui.oss.controller;

import com.feirui.oss.utils.MinioUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class FileController {
    @Resource
    private MinioUtil minioUtil;

    @RequestMapping("/test")
    public List<String> test() throws Exception {
        return minioUtil.getAllBucket();
    }
}
