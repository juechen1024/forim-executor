package cn.gov.forestry.common.domain.vo.base;

import lombok.Data;

import java.util.Map;

@Data
public class QueryPageVO<T extends QueryPageBaseParams> {
    private T params;
    private Map<String, Object> sort;
    private Map<String, Object> filter;

    public QueryPageVO(T params, Map<String, Object> sort, Map<String, Object> filter) {
        this.params = params;
        this.sort = sort;
        this.filter = filter;
    }
}
