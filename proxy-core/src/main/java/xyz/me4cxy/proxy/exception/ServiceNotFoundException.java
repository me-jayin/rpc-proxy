package xyz.me4cxy.proxy.exception;

/**
 * 无效的服务
 * @author jayin
 * @since 2024/01/13
 */
public class ServiceNotFoundException extends ProxyResourceException {
    public ServiceNotFoundException(String msg) {
        super(msg);
    }

    public ServiceNotFoundException(String msg, String innerMsg) {
        super(msg, innerMsg);
    }
}
