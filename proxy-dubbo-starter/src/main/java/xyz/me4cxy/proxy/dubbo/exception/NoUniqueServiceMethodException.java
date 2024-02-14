package xyz.me4cxy.proxy.dubbo.exception;

import xyz.me4cxy.proxy.exception.ProxyException;

/**
 * 非唯一的业务方法
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/09
 */
public class NoUniqueServiceMethodException extends ProxyException {
    public NoUniqueServiceMethodException(String method) {
        super("服务非唯一", "匹配命中多个服务方法：" + method, null);
    }
}
