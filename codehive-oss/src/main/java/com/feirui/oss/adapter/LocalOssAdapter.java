package com.feirui.oss.adapter;

import com.feirui.oss.config.LocalConfig;
import com.feirui.oss.entity.FileInfo;
import com.feirui.oss.utils.LocalEncryptUtil;
import com.feirui.oss.utils.LocalFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static com.feirui.oss.utils.LocalFileUtil.LEFT_SLASH;

@Slf4j
public class LocalOssAdapter implements OssAdapter {

    @Resource
    private LocalConfig localConfig;

    @Override
    public void createBucket(String folder) {
        if (!LocalFileUtil.doesPathExist(folder)) {
            LocalFileUtil.createPath(folder);
        }
    }

    @Override
    public void uploadFile(MultipartFile uploadFile, String folder, String objectName) {
        try {
            String outPath = buildPath(folder, objectName) + LEFT_SLASH + uploadFile.getOriginalFilename();
            InputStream in = getFileInputStream(uploadFile.getInputStream());
            LocalFileUtil.copyFile(in, outPath);
        } catch (Exception e) {
            log.error("local upload file error: {}", e.getMessage(), e);
        }
    }

    private InputStream getFileInputStream(InputStream inputStream) {
        return localConfig.getPasswordSwitch() ? LocalEncryptUtil.aesEncryptToStream(inputStream) : inputStream;
    }

    private String buildPath(String folder, String objectName) {
        StringBuilder sb = new StringBuilder();
        sb.append(localConfig.getBasePath()).append(LEFT_SLASH).append(folder);
        if (StringUtils.hasLength(objectName)) {
            sb.append(LEFT_SLASH).append(objectName);
        }
        if (!LocalFileUtil.doesPathExist(sb.toString())) {
            LocalFileUtil.createPath(sb.toString());
        }
        return sb.toString();
    }

    @Override
    public List<String> getAllBucket() {
        return Collections.emptyList();
    }

    @Override
    public String getUrl(String folder, String objectName) {
        return "http://" + localConfig.getUrl() + "/download?bucketName=" + folder + "&objectName=" + objectName;
    }

    @Override
    public List<FileInfo> getAllFile(String folder) {
        return Collections.emptyList();
    }

    @Override
    public InputStream downloadFile(String folder, String objectName) {
        String path = buildPath(folder, objectName);
        InputStream in = null;
        try {
            in = Files.newInputStream(Paths.get(path));
            if (localConfig.getPasswordSwitch()) {
                in = LocalEncryptUtil.aesDecryptToStream(in);
            }
        } catch (Exception e) {
            log.error("local download file error: {}", e.getMessage(), e);
        }
        return in;
    }

    @Override
    public void deleteBucket(String folder) {
        String path = buildPath(folder, "");
        LocalFileUtil.forceDeleteFile(new File(path));
    }

    @Override
    public void deleteFile(String folder, String objectName) {
        String path = buildPath(folder, objectName);
        LocalFileUtil.forceDeleteFile(new File(path));
    }

}
