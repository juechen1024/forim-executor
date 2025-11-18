package cn.gov.forestry.common.domain.vo;

import cn.gov.forestry.common.domain.bo.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 类名称：CommonResult<br>
 * 类描述：<br>
 * 创建时间：2021年11月01日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
public class CommonResult<T> {
    private T data;
    private Boolean success;
    private String code;
    private String msg;

    public static <T> CommonResult<T> ok(T data) {
        return new CommonResult<>(data, Boolean.TRUE, ResultCodeEnum.SUCCESS.getResultCode(), ResultCodeEnum.SUCCESS.getResultMsg());
    }
    public static <T> CommonResult<T> fail(String resultCode, String resultMsg, T data) {
        return new CommonResult<>(data, Boolean.FALSE, resultCode, resultMsg);
    }
    public static <T> CommonResult<T> fail(ResultCodeEnum resultCodeEnum,T data) {
        return new CommonResult<>(data, Boolean.FALSE, resultCodeEnum.getResultCode(), resultCodeEnum.getResultMsg());
    }
    public static <T> CommonResult<T> fail(ResultCodeEnum resultCodeEnum) {
        return new CommonResult<>(null, Boolean.FALSE, resultCodeEnum.getResultCode(), resultCodeEnum.getResultMsg());
    }
}
