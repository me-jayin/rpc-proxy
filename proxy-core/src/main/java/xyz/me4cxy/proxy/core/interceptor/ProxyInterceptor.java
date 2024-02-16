package xyz.me4cxy.proxy.core.interceptor;

import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.invoker.Invoker;

/**
 * 代理拦截器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/15
 */
public interface ProxyInterceptor {

    /**
     * 判断当前拦截器是否需要拦截当前请求
     * @param identify
     * @return
     */
    boolean isSupport(ProxyRequestContext context, ProxyIdentify identify);

    /**
     * 调用前拦截
     * @param context
     * @param identify
     * @param invoker
     * @return 如果返回值true则继续执行，false则不执行调用器
     */
    boolean before(ProxyRequestContext context, ProxyIdentify identify, Invoker<ProxyIdentify> invoker);

}
