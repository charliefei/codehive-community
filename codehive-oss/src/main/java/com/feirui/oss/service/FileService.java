package com.feirui.oss.service;

import com.feirui.oss.adapter.OssAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileService {
    private final OssAdapter ossAdapter;

    public FileService(OssAdapter ossAdapter) {
        this.ossAdapter = ossAdapter;
    }

    public List<String> getAllBucket() {
        return ossAdapter.getAllBucket();
    }

    /**
     * 获取文件路径
     */
    public String getUrl(String bucketName, String objectName) {
        return ossAdapter.getUrl(bucketName, objectName);
    }

    /**
     * 上传文件
     */
    public String uploadFile(MultipartFile uploadFile, String bucket, String objectName) {
        ossAdapter.uploadFile(uploadFile, bucket, objectName);
        objectName = objectName + "/" + uploadFile.getOriginalFilename();
        return ossAdapter.getUrl(bucket, objectName);
    }
}
