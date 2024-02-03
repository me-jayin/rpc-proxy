package xyz.me4cxy.proxy.dubbo.metadata;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.metadata.definition.model.ServiceDefinition;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * proxy代理业务元数据
 *
 * @author jayin
 * @since 2024/01/31
 */
@Slf4j
public class ProxyServiceMetadata implements Serializable {
    private DubboProxyIdentify identify;
    private Map<String, ProxyMethodMetadata> methods = new HashMap<>();

    public ProxyServiceMetadata(DubboProxyIdentify identify, ServiceDefinition serviceDefinition) {
        this.identify = identify;
        log.info("开始初始化服务 {} 的元数据", identify.identityKey());


    }

    public Map<String, ProxyMethodMetadata> methods() {
        return null;
    }

    @Override
    public String toString() {
        return "ProxyServiceMetadata." + identify.getService() + "[" +
                "application= " + identify.getApplication() +
                "group= " + identify.getGroup() +
                "version= " + identify.getVersion() +
                "] method size:" + methods.size();
    }
}
