package com.feirui.oss.service;

import com.feirui.oss.adapter.OssAdapter;
import org.springframework.stereotype.Service;

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
}
