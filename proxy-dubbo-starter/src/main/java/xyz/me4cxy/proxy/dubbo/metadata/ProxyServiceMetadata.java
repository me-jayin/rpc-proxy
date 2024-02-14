package xyz.me4cxy.proxy.dubbo.metadata;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.metadata.definition.model.MethodDefinition;
import org.apache.dubbo.metadata.definition.model.ServiceDefinition;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodMetadata;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.exception.ProxyParamException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * proxy代理业务元数据
 *
 * @author jayin
 * @since 2024/01/31
 */
@Slf4j
public class ProxyServiceMetadata implements Serializable {
    private String applicationIdentify;
    /**
     * 服务名称
     */
    private String service;
    /**
     * 别名到方法列表
     */
    private Map<String, List<ProxyMethodMetadata>> aliasToMethodsMetadata;
    /**
     * 服务定义
     */
    @Getter
    private ServiceDefinition serviceDefinition;

    public ProxyServiceMetadata(String applicationIdentify, ServiceDefinition serviceDefinition) {
        this.applicationIdentify = applicationIdentify;
        this.serviceDefinition = serviceDefinition;
        log.info("开始初始化服务 {} 的元数据", applicationIdentify);

        List<MethodDefinition> methods = serviceDefinition.getMethods();
        Map<String, List<ProxyMethodMetadata>> methodsMetadata = new HashMap<>();
        for (MethodDefinition method : methods) {
            ProxyMethodMetadata metadata = ProxyMethodMetadata.of(applicationIdentify, this, method);
            // 遍历别名注册方法信息
            for (String alias : metadata.getAlias()) {
                methodsMetadata.computeIfAbsent(alias, k -> new ArrayList<>()).add(metadata);
            }
            boolean haveBodyParam = false;
            for (ProxyMethodParameterMetadata param : metadata.getParams()) {
                if (ProxyParamType.BODY.equals(param.getParamType())) {
                    if (haveBodyParam) {
                        throw new ProxyParamException(applicationIdentify, "存在多个body参数");
                    }
                    haveBodyParam = true;
                }
            }
        }
        this.aliasToMethodsMetadata = methodsMetadata;
        this.service = serviceDefinition.getCanonicalName();
    }

    /**
     * 获取方法定义列表
     * @param methodName
     * @return
     */
    public List<ProxyMethodMetadata> getMethods(String methodName) {
        return aliasToMethodsMetadata.get(methodName);
    }

    /**
     * 方法列表
     * @return
     */
    public Map<String, List<ProxyMethodMetadata>> getAllMethod() {
        return aliasToMethodsMetadata;
    }

    @Override
    public String toString() {
        return "ProxyServiceMetadata[" + applicationIdentify + ":" + service +
                "], method list: " + aliasToMethodsMetadata.keySet();
    }
}
