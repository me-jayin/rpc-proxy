package xyz.me4cxy.proxy.core.response.wrapper.enums;

/**
 * 状态码
 */
public interface Status {
    int SUCCESS_CODE = 0;

    int getCode();

    String getMsg();

    String getErrMsg();

    default boolean hasSuccess() {
        return getCode() == SUCCESS_CODE;
    }
}
