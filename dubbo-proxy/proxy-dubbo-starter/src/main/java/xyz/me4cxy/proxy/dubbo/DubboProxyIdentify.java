package xyz.me4cxy.proxy.dubbo;

import com.google.common.base.Joiner;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import xyz.me4cxy.proxy.consts.StringConstants;
import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.exception.IdentifyMappingException;
import xyz.me4cxy.proxy.utils.PathPatternUtils;

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
    private static final Joiner JOINER = Joiner.on(IDENTIFY_SEPARATOR);
    /**
     * 类分隔符
     */
    private static final Joiner CLASS_SEPARATOR = Joiner.on(StringConstants.CLASS_PATH_SEPARATOR);

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
     * 服务标识符，{application}:{group}:{version}:{service}
     * @return
     */
    public String serviceIdentifyKey() {
        return JOINER.join(Arrays.asList(application, service, version, group));
    }

    /**
     * 方法级别的标识符，，{application}:{group}:{version}:{service}:{method}
     * @return
     */
    @Override
    public String identifyKey() {
        return JOINER.join(Arrays.asList(application, service, version, group, method));
    }

    /**
     * 应用前缀，只与应用、分组和版本有关，采用 {application}.{group}.v{version}
     * 并且会将application、group中特殊字符换成 _ ，并且在 version 中 . 换成 _
     * @return
     */
    public String applicationIdentify() {
        return CLASS_SEPARATOR.join(
                PathPatternUtils.replaceClassPathNotSupportChar(application, StringConstants.UNDERLINE),
                PathPatternUtils.replaceClassPathNotSupportChar(group, StringConstants.UNDERLINE),
                getUnderlineVersion()
        );
    }

    /**
     * 去除特殊字符，将特殊字符换成 _ 后的版本号
     * @return
     */
    private String getUnderlineVersion() {
        String v = version;
        if (!StringUtils.startsWith(v, "v")) {
            v = "v" + v;
        }
        return StringUtils.replace(v, StringConstants.CLASS_PATH_SEPARATOR, StringConstants.UNDERLINE);
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
