package com.feirui.oss.service;

import com.feirui.oss.adapter.OssAdapter;
import com.feirui.oss.utils.LocalFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Service
@Slf4j
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
        if (StringUtils.hasLength(objectName))
            objectName = objectName + "/" + uploadFile.getOriginalFilename();
        else
            objectName = uploadFile.getOriginalFilename();
        String url = ossAdapter.getUrl(bucket, objectName);
        log.info("oss upload file success! url: {}", url);
        return url;
    }

    /**
     * 下载文件
     */
    public void download(String bucketName, String objectName, HttpServletResponse response) {
        try {
            InputStream inputStream = ossAdapter.downloadFile(bucketName, objectName);
            // 设置响应头
            response.setContentType("application/octet-stream");
            // 下面的设置是为了让浏览器识别为附件（即提示用户下载）
            String fileName = objectName.substring(objectName.lastIndexOf("/"));
            response.setHeader("Content-Disposition", "attachment; filename=\"" +
                    URLEncoder.encode(fileName, "UTF-8") + "\"");
            LocalFileUtil.copyFile(inputStream, response.getOutputStream());
        } catch (IOException e) {
            log.error("oss download file error! url: {}", e.getMessage(), e);
        }
    }
}
