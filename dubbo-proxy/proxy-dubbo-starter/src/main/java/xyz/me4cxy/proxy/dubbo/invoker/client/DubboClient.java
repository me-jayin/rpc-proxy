package xyz.me4cxy.proxy.dubbo.invoker.client;

import org.apache.dubbo.rpc.cluster.router.mesh.rule.virtualservice.DubboRoute;
import xyz.me4cxy.proxy.dubbo.invoker.ServiceIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodMetadata;

/**
 * dubbo客户端
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/14
 */
public interface DubboClient {
    /**
     * 调用服务
     * @param serviceIdentify
     * @param method
     * @param args
     * @return
     */
    Object invoke(ServiceIdentify serviceIdentify, ProxyMethodMetadata method, Object[] args);
}
