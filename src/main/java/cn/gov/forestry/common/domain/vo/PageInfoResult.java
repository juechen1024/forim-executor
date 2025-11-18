package cn.gov.forestry.common.domain.vo;

import cn.gov.forestry.common.domain.bo.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageInfoResult<T> {
    private List<T> data;
    private Integer page;
    private Long total;
    private Boolean success;
    private String code;
    private String msg;

    public static <T> PageInfoResult<T> ok(List<T> data, Integer page, Long total) {
        return new PageInfoResult<>(data, page, total, Boolean.TRUE, ResultCodeEnum.SUCCESS.getResultCode(), ResultCodeEnum.SUCCESS.getResultMsg());
    }

    public static <T> PageInfoResult<T> ok(PageInfo<T> pageInfo) {
        return new PageInfoResult<>(pageInfo.getData(), pageInfo.getPage(), pageInfo.getTotal(), Boolean.TRUE, ResultCodeEnum.SUCCESS.getResultCode(), ResultCodeEnum.SUCCESS.getResultMsg());
    }

    public static <T> PageInfoResult<T> fail(String resultCode, String resultMsg, List<T> data) {
        return new PageInfoResult<>(data, null, null, Boolean.FALSE, resultCode, resultMsg);
    }
    public static <T> PageInfoResult<T> fail(ResultCodeEnum resultCodeEnum,List<T> data) {
        return new PageInfoResult<>(data,null, null, Boolean.FALSE, resultCodeEnum.getResultCode(), resultCodeEnum.getResultMsg());
    }
    public static <T> PageInfoResult<T> fail(ResultCodeEnum resultCodeEnum) {
        return new PageInfoResult<>(null,null, null, Boolean.FALSE, resultCodeEnum.getResultCode(), resultCodeEnum.getResultMsg());
    }
}
