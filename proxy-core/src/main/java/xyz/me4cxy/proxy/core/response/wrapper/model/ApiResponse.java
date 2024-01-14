package xyz.me4cxy.proxy.core.response.wrapper.model;

import xyz.me4cxy.proxy.core.response.wrapper.enums.RestStatus;

/**
 * api响应结果
 * @author cxy
 * @createAt 2023-05-2023/5/22
 */
public class ApiResponse<T> {

    private Integer code;

    private String msg;

    private String errorMsg;

    private T data;

    private ApiResponse(Integer code, String msg, String errorMsg, T data) {
        this.code = code;
        this.msg = msg;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    private ApiResponse(RestStatus status, String msg, String errorMsg, T data) {
        this(status.getCode(), msg, errorMsg, data);
    }

    /**
     * 成功
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 成功
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> success(T data) {
        return of(RestStatus.SUCCESS_CODE, RestStatus.SUCCESS.getMsg(), data);
    }

    /**
     * 失败
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> fail(String msg) {
        return ofStatus(RestStatus.PARAMS_VALIDATE_FAILED, msg, null);
    }

    /**
     * 失败
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> fail(int code, String msg) {
        return of(code, msg, null);
    }

    public static <T> ApiResponse<T> of(Integer code, String msg, String errorMsg, T data) {
        return new ApiResponse<>(code, msg, errorMsg, data);
    }

    public static <T> ApiResponse<T> of(Integer code, String msg, T data) {
        return of(code, msg, null, data);
    }

    public static <T> ApiResponse<T> of(Integer code, String msg) {
        return of(code, msg, null);
    }

    public static <T> ApiResponse<T> ofStatus(RestStatus status, String errorMsg, T data) {
        return of(status.getCode(), status.getMsg(), errorMsg, data);
    }

    public static <T> ApiResponse<T> ofStatus(RestStatus status, T data) {
        return ofStatus(status, null, data);
    }

    public static <T> ApiResponse<T> ofStatus(RestStatus status) {
        return ofStatus(status, null, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
