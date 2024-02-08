package xyz.me4cxy.proxy.dubbo.metadata.invoker;

import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.invoker.Invoker;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodMetadata;

import java.util.List;

/**
 * 通过元数据的dubbo调用器
 *
 * @author jayin
 * @since 2024/02/03
 */
public class ServiceMethodInvoker implements Invoker<DubboProxyIdentify> {

    /**
     * 服务描述元数据
     */
    private final List<ProxyMethodMetadata> methods;
    /**
     * 应用标识
     */
    private final String applicationIdentify;
    /**
     * 服务名称
     */
    private final String service;
    /**
     * 方法名称
     */
    private final String methodName;

    public ServiceMethodInvoker(ProxyServiceMetadata metadata, DubboProxyIdentify identify) {
        this.applicationIdentify = identify.applicationIdentifyKey();
        this.service = identify.getService();
        this.methodName = identify.getMethod();
        this.methods = metadata.getMethods(methodName);
    }

    @Override
    public Object invoke(ProxyRequestContext context) {
        // 1.过滤出符合method的方法
        // 2.匹配参数，找出参数命中最多的方法
        // 进行dubbo实际调用
        return null;
    }

    @Override
    public String toString() {
        return "Invoker$" + applicationIdentify + "@" + service + "#" + methodName;
    }
}
