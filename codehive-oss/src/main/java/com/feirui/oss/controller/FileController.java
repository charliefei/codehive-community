package com.feirui.oss.controller;

import com.feirui.oss.entity.Result;
import com.feirui.oss.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FileController {
    @Resource
    private FileService fileService;

    @RequestMapping("/test")
    public List<String> test() {
        return fileService.getAllBucket();
    }

    @RequestMapping("/getUrl")
    public String getUrl(String bucketName, String objectName) {
        return fileService.getUrl(bucketName, objectName);
    }

    /**
     * 上传文件
     */
    @RequestMapping("/upload")
    public Result<String> upload(MultipartFile uploadFile, String bucket, String objectName) {
        String url = fileService.uploadFile(uploadFile, bucket, objectName);
        return Result.ok(url);
    }

    @RequestMapping("/download")
    public void download(String bucketName, String objectName, HttpServletResponse response) {
        fileService.download(bucketName, objectName, response);
    }
}
