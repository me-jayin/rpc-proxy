package xyz.me4cxy.proxy.core.invoker;

import xyz.me4cxy.proxy.core.ProxyIdentify;

/**
 * 调用器factory
 *
 * @author jayin
 * @since 2024/02/02
 */
public interface InvokerFactory<T extends ProxyIdentify> {

    /**
     * 创建invoker
     * @param identify
     * @return
     */
    Invoker<T> createInvoker(T identify);

    /**
     * 判断是否支持当前identify来创建调用器
     * @param identify
     * @return
     */
    boolean isSupport(ProxyIdentify identify);

}
