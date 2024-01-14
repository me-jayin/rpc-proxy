package xyz.me4cxy.proxy.exception;

/**
 * 无法解析标识异常
 *
 * @author jayin
 * @since 2024/01/14
 */
public class IdentifyMappingException extends ProxyException {
    public IdentifyMappingException(String msg) {
        super(msg);
    }

    public IdentifyMappingException(String msg, String innerMsg) {
        super(msg, innerMsg);
    }
}
