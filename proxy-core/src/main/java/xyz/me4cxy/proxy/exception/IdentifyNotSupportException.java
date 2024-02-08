package xyz.me4cxy.proxy.exception;

import xyz.me4cxy.proxy.core.ProxyIdentify;

/**
 * 代理标识不支持
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/08
 */
public class IdentifyNotSupportException extends ProxyException {
    public IdentifyNotSupportException(ProxyIdentify identify) {
        super("请求方式不支持", "代理标识" + identify.getClass().getCanonicalName() + "无对应的调用器创建工厂");
    }
}
