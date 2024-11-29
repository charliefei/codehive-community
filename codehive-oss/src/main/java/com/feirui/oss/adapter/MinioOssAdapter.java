package com.feirui.oss.adapter;

import com.feirui.oss.entity.FileInfo;
import com.feirui.oss.utils.MinioUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
public class MinioOssAdapter implements OssAdapter {
    @Resource
    private MinioUtil minioUtil;

    /**
     * minioUrl
     */
    @Value("${minio.url}")
    private String url;

    @Override
    @SneakyThrows
    public void createBucket(String bucket) {
        minioUtil.createBucket(bucket);
    }

    @Override
    public void uploadFile(MultipartFile uploadFile, String bucket, String objectName) {
        try {
            minioUtil.createBucket(bucket);
            if (StringUtils.hasLength(objectName)) {
                minioUtil.uploadFile(uploadFile.getInputStream(),
                        bucket, objectName + "/" + uploadFile.getOriginalFilename());
            } else {
                minioUtil.uploadFile(uploadFile.getInputStream(),
                        bucket, uploadFile.getOriginalFilename());
            }
        } catch (Exception e) {
            log.error("minio upload file error: {}", e.getMessage(), e);
        }
    }

    @Override
    @SneakyThrows
    public List<String> getAllBucket() {
        return minioUtil.getAllBucket();
    }

    @Override
    @SneakyThrows
    public String getUrl(String bucket, String objectName) {
        return url + "/" + bucket + "/" + objectName;
    }

    @Override
    @SneakyThrows
    public List<FileInfo> getAllFile(String bucket) {
        return minioUtil.getAllFile(bucket);
    }

    @Override
    @SneakyThrows
    public InputStream downloadFile(String bucket, String objectName) {
        return minioUtil.downloadFile(bucket, objectName);
    }

    @Override
    @SneakyThrows
    public void deleteBucket(String bucket) {
        minioUtil.deleteBucket(bucket);
    }

    @Override
    @SneakyThrows
    public void deleteFile(String bucket, String objectName) {
        minioUtil.deleteFile(bucket, objectName);
    }
}
