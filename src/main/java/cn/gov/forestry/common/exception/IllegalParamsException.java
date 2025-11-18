package cn.gov.forestry.common.exception;

public class IllegalParamsException extends RuntimeException{

    // 无参构造函数（可选）
    public IllegalParamsException() {
        super();
    }

    // 带错误信息的构造函数
    public IllegalParamsException(String message) {
        super(message);  // 调用父类RuntimeException的构造函数
    }

    // 带错误信息和异常原因的构造函数（可选）
    public IllegalParamsException(String message, Throwable cause) {
        super(message, cause);
    }
}
