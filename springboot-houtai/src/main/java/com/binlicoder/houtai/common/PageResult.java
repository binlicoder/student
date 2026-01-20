package com.binlicoder.houtai.common;

import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private List<T> data;
    private Integer total;
    private Integer page;
    private Integer pageSize;

    public PageResult(List<T> data, Integer total, Integer page, Integer pageSize) {
        this.data = data;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }
}
