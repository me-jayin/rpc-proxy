package xyz.me4cxy.proxy.dubbo.metadata.invoker;

import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.invoker.Invoker;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;

/**
 * 通过元数据的dubbo调用器
 *
 * @author jayin
 * @since 2024/02/03
 */
public class ServiceMetadataInvoker implements Invoker<DubboProxyIdentify> {

    /**
     * 服务描述元数据
     */
    private final ProxyServiceMetadata serviceMetadata;

    public ServiceMetadataInvoker(ProxyServiceMetadata serviceMetadata) {
        this.serviceMetadata = serviceMetadata;
    }

    @Override
    public Object invoke(ProxyRequestContext context) {
        return null;
    }

    public ProxyServiceMetadata getServiceMetadata() {
        return serviceMetadata;
    }

    @Override
    public String toString() {
        return "Invoker." + serviceMetadata;
    }
}
