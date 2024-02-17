package xyz.me4cxy.proxy.dubbo.metadata.method;

import lombok.Builder;
import lombok.Getter;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.annotation.ProxyParamValue;
import xyz.me4cxy.proxy.dubbo.metadata.GlobalTypeRegister;
import xyz.me4cxy.proxy.dubbo.metadata.type.ProxyTypeMetadata;

import java.io.Serializable;

/**
 * proxy方法参数元数据
 *
 * @author jayin
 * @since 2024/02/03
 */
@Getter
@Builder
public class ProxyMethodParameterMetadata implements Serializable {
    /**
     * 参数索引
     */
    private int index;
    /**
     * 属性名称
     */
    private String name;
    /**
     * 当前参数类型
     */
    private ProxyTypeMetadata type;
    /**
     * 代理参数类型，
     */
    private ProxyParamType paramType;

    /**
     * 创建方法参数元数据对象
     * @param proxyParamValue
     * @param parameterType
     * @return
     */
    public static ProxyMethodParameterMetadata of(String applicationIdentify, ProxyParamValue proxyParamValue, String parameterType) {
        return ProxyMethodParameterMetadata.builder()
                .index(proxyParamValue.getIndex())
                .name(proxyParamValue.getName())
                .paramType(proxyParamValue.getParamType())
                .type(GlobalTypeRegister.getType(applicationIdentify, parameterType))
                .build();
    }

}
