package xyz.me4cxy.proxy.dubbo.mapping.resolver;

import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.ProxyIdentify;

/**
 * 代理标识解析器，从请求信息中获取对应的代理标识
 *
 * @author jayin
 * @since 2024/01/14
 */
public interface PathRuleProxyIdentifyResolver {

    /**
     * 从请求对象中获取服务标识符解析
     * @param context
     * @return
     */
    ProxyIdentify resolve(ProxyRequestContext context);

}
