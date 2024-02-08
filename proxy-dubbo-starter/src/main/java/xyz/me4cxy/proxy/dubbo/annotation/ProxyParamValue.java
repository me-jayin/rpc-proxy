package xyz.me4cxy.proxy.dubbo.annotation;

import lombok.Builder;
import lombok.Getter;
import org.apache.dubbo.rpc.protocol.rest.exception.ParamParseException;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.exception.ProxyParamException;

/**
 * 代理参数信息值
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/07
 */
@Getter
@Builder
public class ProxyParamValue {
    private String service;
    /**
     * 参数位置
     */
    private int index;
    /**
     * 参数名
     */
    private String name;
    /**
     * 参数类型
     */
    private ProxyParamType paramType;

    /**
     * 校验参数
     */
    private void valid() {
        if (index < 0) {
            throw new ProxyParamException(service, "参数" + name + "下标[index]必须大于0：" + index);
        }
    }
    
    public static class CheckableProxyParamValueBuilder extends ProxyParamValue.ProxyParamValueBuilder {
        CheckableProxyParamValueBuilder() {
            super();
        }
        @Override
        public ProxyParamValue build() {
            ProxyParamValue model = super.build();
            model.valid();
            return model;
        }
    }

    public static ProxyParamValue.ProxyParamValueBuilder builder() {
        return new CheckableProxyParamValueBuilder();
    }

}
