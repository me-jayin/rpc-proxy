package xyz.me4cxy.proxy.core.response.wrapper.enums;

/**
 * rest状态码
 */
public enum RestStatus implements Status {

    SUCCESS(0, "操作成功"),

    ERROR(500, "操作异常"),
    UNKNOWN_ERROR(501, "未知异常"),
    REMOTE_SERVICE_ERROR(502, "远程服务调用失败"),
    SERIALIZATION_ERROR(503, "数据序列化失败"),

    /* 异常状态码 */
    DUBBO_METADATA_GET_TIMEOUT_ERROR(100001, "元数据获取超时"),
    DUBBO_METADATA_PARSE_ERROR(100002, "元数据解析失败"),

    /* rpc代理相关状态码 */
    RPC_PROXY_NOT_SUPPORT(200001, "调用方式不支持"),
    RPC_PROXY_TARGET_NOT_FOUND(200002, "目标接口不存在"),
    RPC_PROXY_METHOD_OVERLOAD(200003, "服务提供者重载方法"),
    RPC_PROXY_PARAM_NOT_SUPPORT(200004, "参数不支持"),
    RPC_INVOKER_FAILURE(200005, "服务调用失败"),

    /* 请求参数状态码 */
    PARAMS_UNSUPPORTED_TYPE(300001, "参数类型不支持"),
    PARAMS_VALIDATE_FAILED(300002, "参数校验失败"),
    PARAMS_FORMAT_EXCEPTION(300003, "参数格式不正确"),

    /* http 相关 */
    HTTP_REQUEST_TIMEOUT(400001, "请求超时，请稍后再试"),
    HTTP_REQUEST_FORBIDDEN(403000, "请求被拒绝"),
    HTTP_DENIED_REQUEST_LIMIT(403001, "请求频率过高，请稍候再试"),
    ;

    /**
     * 状态码
     */
    private final int code;
    /**
     * 消息
     */
    private final String msg;
    /**
     * 异常消息
     */
    private String errMsg;

    RestStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.errMsg = "";
    }

    RestStatus(int code, String msg, String errMsg) {
        this.code = code;
        this.msg = msg;
        this.errMsg = errMsg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public Status err(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
