package cn.gov.forestry.common.domain.bo;

import lombok.Getter;

/**
 * 类名称：ResultCodeEnum<br>
 * 类描述：<br>
 * 创建时间：2021年11月01日<br>
 *
 * @author gongdear
 * @version 1.0.0
 */
public enum ResultCodeEnum {
    SUCCESS("成功", "10110000"),
    TOKEN_IS_EMPTY("Token信息为空", "10110001"),
    APP_KEY_IS_EMPTY("应用标识为空", "10110002"),
    LOGIN_FAIL("登录失败", "10110003"),
    LOGIN_NO_USER("登录失败,无此用户", "10110004"),
    LOGIN_USER_DISABLED("登录失败,用户被禁用", "10110005"),
    REQUEST_PARAMS_VALID_ERROR("输入的数据无效", "10110006"),
    ILLEGAL_USER_INFO("用户失效", "10110007"),
    OPERATOR_NOT_ALLOWED("不允许此操作", "10110008"),
    ELSE_ERROR("其他错误", "10119999")
    ;

    @Getter
    private final String resultMsg;
    @Getter
    private final String resultCode;

    ResultCodeEnum(String resultMsg, String resultCode) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public static Boolean isSuccess(String resultCode){
        return SUCCESS.getResultCode().equals(resultCode);
    }
}
