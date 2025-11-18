package cn.gov.forestry.common.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageInfo<T> {
    private List<T> data;
    private Integer page;
    private Long total;

    public PageInfo() {
        this.data = new ArrayList<>();
        this.page = 1;
        this.total = 0L;
    }

    public PageInfo(List<T> data, Integer page, Long total) {
        this.data = data;
        this.page = page;
        this.total = total;
    }
}
