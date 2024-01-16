package com.feirui.oss.controller;

import com.feirui.oss.service.FileService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class FileController {
    @Resource
    private FileService fileService;

    @RequestMapping("/test")
    public List<String> test() {
        return fileService.getAllBucket();
    }
}
