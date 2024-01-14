package xyz.me4cxy.proxy.core;

/**
 * 代理上下文持有者
 *
 * @author jayin
 * @since 2024/01/07
 */
public class ProxyContextHolder {
    private static ThreadLocal<ProxyRequestContext> CONTEXT = new ThreadLocal<>();

    public static ProxyRequestContext getCurrentContext() {
        return CONTEXT.get();
    }

    public static void setCurrentContext(ProxyRequestContext context) {
        CONTEXT.set(context);
    }

}
