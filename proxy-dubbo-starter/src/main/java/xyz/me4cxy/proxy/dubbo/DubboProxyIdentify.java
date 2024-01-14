package xyz.me4cxy.proxy.dubbo;

import com.google.common.base.Joiner;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.exception.IdentifyMappingException;

import java.util.Arrays;

/**
 * dubbo的代理标识
 *
 * @author jayin
 * @since 2024/01/14
 */
@Getter
@Builder
public class DubboProxyIdentify implements ProxyIdentify {
    private static final Joiner JOINER = Joiner.on(":");

    /** 应用名 */
    private String application;
    /** 服务分组 */
    private String group;
    /** 服务版本 */
    private String version;
    /** 服务业务类 */
    private String service;
    /** 调用方法 */
    private String method;

    /**
     * 服务标识符，{service}:{version}:{group}
     * @return
     */
    public String serviceIdentity() {
        return JOINER.join(Arrays.asList(service, version, group));
    }

    @Override
    public String toString() {
        return "{" +
                "application='" + application + '\'' +
                ", group='" + group + '\'' +
                ", version='" + version + '\'' +
                ", service='" + service + '\'' +
                ", method='" + method + '\'' +
                '}';
    }

    private void valid() {
        if (StringUtils.isEmpty(application)) {
            throw new IdentifyMappingException("代理应用为空");
        } else if (StringUtils.isEmpty(service)) {
            throw new IdentifyMappingException("代理服务为空");
        } else if (StringUtils.isEmpty(method)) {
            throw new IdentifyMappingException("代理服务方法为空");
        } else if (version == null) {
            throw new IdentifyMappingException("请求中版本信息为空");
        } else if (StringUtils.isEmpty(group)) {
            throw new IdentifyMappingException("请求中分组信息为空");
        }
    }

    public static class CheckableDubboProxyIdentifyBuilder extends DubboProxyIdentifyBuilder {
        CheckableDubboProxyIdentifyBuilder() {
            super();
        }
        @Override
        public DubboProxyIdentify build() {
            DubboProxyIdentify model = super.build();
            model.valid();
            return model;
        }
    }

    public static DubboProxyIdentifyBuilder builder() {
        return new CheckableDubboProxyIdentifyBuilder();
    }

}
