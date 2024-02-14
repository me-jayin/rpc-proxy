package xyz.me4cxy.proxy.exception;

/**
 * 服务资源未找到
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/09
 */
public class ProxyResourceException extends ProxyException {
    public ProxyResourceException(String msg) {
        this(msg, msg);
    }

    public ProxyResourceException(String msg, String innerMsg) {
        this(msg, innerMsg, null);
    }

    public ProxyResourceException(String msg, String innerMsg, Throwable t) {
        super(msg, innerMsg, t);
    }
}
