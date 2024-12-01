package com.feirui.subject.common.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页返回实体
 */
@Data
public class PageResult<T> implements Serializable {
    /**
     * 页码
     */
    private Integer pageNo = 1;
    /**
     * 每页条数
     */
    private Integer pageSize = 20;
    /**
     * 总条数
     */
    private Integer total = 0;
    /**
     * 总页数
     */
    private Integer totalPages = 0;
    /**
     * 每页的具体数据
     */
    @Setter(AccessLevel.NONE)
    private List<T> result = Collections.emptyList();
    /**
     * 每页的开始数据
     */
    private Integer start = 1;
    /**
     * 每页的结束数据
     */
    private Integer end = 0;

    public void setRecords(List<T> result) {
        this.result = result;
        if (result != null && result.size() > 0) {
            setTotal(result.size());
        }
    }

    public void setTotal(Integer total) {
        // 设置总条数
        this.total = total;
        if (this.pageSize > 0) {
            // 设置总页数
            this.totalPages = (total / this.pageSize) + (total % this.pageSize == 0 ? 0 : 1);
        } else {
            this.totalPages = 0;
        }
        this.start = (this.pageSize > 0 ? (this.pageNo - 1) * this.pageSize : 0) + 1;
        this.end = this.start - 1 + this.pageSize * (this.pageNo > 0 ? 1 : 0);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
