package xyz.me4cxy.proxy.core.invoker;

import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.core.ProxyRequestContext;

/**
 * 调用器
 *
 * @author jayin
 * @since 2024/02/02
 */
public interface Invoker<T extends ProxyIdentify> {

    /**
     * 调用invoke
     * @return
     */
    Object invoke(ProxyRequestContext context);

}
