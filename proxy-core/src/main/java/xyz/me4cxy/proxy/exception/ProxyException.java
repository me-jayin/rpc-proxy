package xyz.me4cxy.proxy.exception;

/**
 * 代理处理时遇到的异常
 *
 * @author jayin
 * @since 2024/01/13
 */
public class ProxyException extends RuntimeException {
    private final String innerMsg;

    public ProxyException(String msg, Throwable t) {
        this(msg, msg, t);
    }

    public ProxyException(String msg, String innerMsg, Throwable t) {
        super(msg, t);
        this.innerMsg = innerMsg;
    }

    public String getInnerMessage() {
        return innerMsg;
    }
}
