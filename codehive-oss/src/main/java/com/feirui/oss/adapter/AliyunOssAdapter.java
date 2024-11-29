package com.feirui.oss.adapter;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;
import com.feirui.oss.config.AliyunConfig;
import com.feirui.oss.entity.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AliyunOssAdapter implements OssAdapter {
    @Resource
    private OSSClient ossClient;

    @Resource
    private AliyunConfig aliyunConfig;

    @Override
    public void createBucket(String bucket) {
        if (!ossClient.doesBucketExist(bucket)) {
            ossClient.createBucket(bucket);
        }
    }

    @Override
    public void uploadFile(MultipartFile uploadFile, String bucket, String objectName) {
        try {
            createBucket(bucket);
            if (StringUtils.hasLength(objectName)) {
                ossClient.putObject(aliyunConfig.getBucketName(),
                        objectName + "/" + uploadFile.getOriginalFilename(),
                        new ByteArrayInputStream(uploadFile.getBytes()));
            } else {
                ossClient.putObject(aliyunConfig.getBucketName(),
                        uploadFile.getOriginalFilename(),
                        new ByteArrayInputStream(uploadFile.getBytes()));
            }
        } catch (Exception e) {
            log.error("aliyun upload file error: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<String> getAllBucket() {
        return ossClient.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
    }

    @Override
    public String getUrl(String bucket, String objectName) {
        return "https://" +
                aliyunConfig.getBucketName() +
                "." +
                aliyunConfig.getEndpoint() +
                "/" +
                objectName;
    }

    @Override
    public List<FileInfo> getAllFile(String bucket) {
        return Collections.emptyList();
    }

    @Override
    public InputStream downloadFile(String bucket, String objectName) {
        return null;
    }

    @Override
    public void deleteBucket(String bucket) {
        ossClient.deleteBucket(bucket);
    }

    @Override
    public void deleteFile(String bucket, String objectName) {
        ossClient.deleteObject(bucket, objectName);
    }
}
