package xyz.me4cxy.proxy.dubbo.metadata.method;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.metadata.definition.model.MethodDefinition;
import org.apache.dubbo.metadata.definition.model.TypeDefinition;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.annotation.ProxyAnnotationUtils;
import xyz.me4cxy.proxy.dubbo.annotation.ProxyMethodValue;
import xyz.me4cxy.proxy.dubbo.annotation.ProxyParamValue;
import xyz.me4cxy.proxy.dubbo.metadata.GlobalTypeRegister;
import xyz.me4cxy.proxy.dubbo.metadata.type.ProxyTypeMetadata;
import xyz.me4cxy.proxy.exception.ProxyParamException;
import xyz.me4cxy.proxy.utils.AnnotationResolverUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 代理方法实现类
 *
 * @author jayin
 * @since 2024/02/03
 */
@Getter
@Builder
public class ProxyMethodMetadata implements Serializable {
    /**
     * 方法名
     */
    private String name;

    /**
     * 方法别名
     */
    private Set<String> alias;

    /**
     * 请求方法
     */
    private Set<String> requestMethod;

    /**
     * 参数列表
     */
    private List<ProxyMethodParameterMetadata> params;

    /**
     * 返回类型
     */
    private ProxyTypeMetadata returnType;

    /**
     * 创建方法元数据
     * @param applicationIdentify
     * @param definition
     * @return
     */
    public static ProxyMethodMetadata of(String applicationIdentify, MethodDefinition definition) {
        Map<String, List<Map<String, String>>> annoAttrs = AnnotationResolverUtils.resolve(definition.getAnnotations());
        // 获取代理方法列表
        ProxyMethodValue proxyMethod = ProxyAnnotationUtils.getProxyMethodsInfo(applicationIdentify, annoAttrs);
        proxyMethod.getAlias().add(definition.getName()); // 将原方法名也加入别名中

        // 处理参数信息
        List<ProxyParamValue> proxyParamsInfo = ProxyAnnotationUtils.getProxyParamsInfo(applicationIdentify, annoAttrs);
        String[] parameterTypes = definition.getParameterTypes();
        if (parameterTypes.length != proxyParamsInfo.size()) {
            throw new ProxyParamException(applicationIdentify, "存在参数未使用“@ProxyParam”声明参数信息");
        }
        // 转换参数类型
        List<ProxyMethodParameterMetadata> parameterMetadata = new ArrayList<>(proxyParamsInfo.size());
        for (int i = 0; i < proxyParamsInfo.size(); i++) {
            parameterMetadata.add(ProxyMethodParameterMetadata.of(applicationIdentify, proxyParamsInfo.get(i), parameterTypes[i]));
        }

        return ProxyMethodMetadata.builder()
                .name(definition.getName())
                .alias(proxyMethod.getAlias())
                .requestMethod(proxyMethod.getMethods())
                .returnType(GlobalTypeRegister.getType(applicationIdentify, definition.getReturnType()))
                .params(parameterMetadata)
                .build();
    }

}
