package xyz.me4cxy.proxy.exception;

/**
 * 非法参数
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/10
 */
public class ProxyIllegalArgumentException extends ProxyException {
    public ProxyIllegalArgumentException(String msg) {
        super(msg, null);
    }

    public ProxyIllegalArgumentException(String msg, String innerMsg) {
        super(msg, innerMsg, null);
    }

    public ProxyIllegalArgumentException(String msg, Throwable t) {
        super(msg, t);
    }

    public ProxyIllegalArgumentException(String msg, String innerMsg, Throwable t) {
        super(msg, innerMsg, t);
    }
}
