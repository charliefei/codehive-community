package com.feirui.oss.entity;

import lombok.Data;

@Data
public class FileInfo {
    private String fileName;
    private Boolean directoryFlag;
    private String etag;
}
