package xyz.me4cxy.proxy.dubbo.exception;

import xyz.me4cxy.proxy.exception.ProxyResourceException;

/**
 * 服务方法未找到
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/09
 */
public class ServiceMethodNotFoundException extends ProxyResourceException {
    public ServiceMethodNotFoundException(String method) {
        super("服务方法未找到", "服务方法" + method + "未找到");
    }
}
