package cn.gov.forestry.common.domain.vo.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryPageBaseParams extends QueryBaseParams {
    //当前页数
    private Integer current = 1;
    //每页条数
    private Integer pageSize = 10;
}
