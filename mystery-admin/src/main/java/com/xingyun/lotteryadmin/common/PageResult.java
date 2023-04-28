package com.xingyun.lotteryadmin.common;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {

    private Long total;

    private List<T> records;


    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
