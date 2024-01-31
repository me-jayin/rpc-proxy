package xyz.me4cxy.proxy.core.mapping;

import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.ProxyIdentify;

/**
 * 代理标识解析器
 *
 * @author jayin
 * @since 2024/01/07
 */
public interface  ProxyIdentifyMapping {

    /**
     * 获得代理标识
     * @param context
     * @return
     */
    ProxyIdentify getIdentify(ProxyRequestContext context);

}
