package xyz.me4cxy.proxy.exception;

/**
 * 无效的服务
 * @author jayin
 * @since 2024/01/13
 */
public class NotFoundServiceException extends ProxyException {
    public NotFoundServiceException(String msg) {
        super(msg);
    }

    public NotFoundServiceException(String msg, String innerMsg) {
        super(msg, innerMsg);
    }
}
