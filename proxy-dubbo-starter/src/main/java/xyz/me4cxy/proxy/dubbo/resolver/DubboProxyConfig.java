package xyz.me4cxy.proxy.dubbo.resolver;

import com.sun.istack.internal.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * dubbo代理默认配置
 *
 * @author jayin
 * @since 2024/01/14
 */
@Data
@ConfigurationProperties(prefix = "xyz.me4cxy.rpc-proxy.dubbo")
public class DubboProxyConfig {

    /** 包名前缀 */
    private String packagePrefix = "";

    /** 默认版本 */
    private String defaultVersion = "";
    /** 默认分组 */
    @NotNull
    private String defaultGroup;
    /** 服务类名后缀，默认为 Service */
    @NotNull
    private String serviceClassSuffix = "Service";
    /** group值所在的请求头名称 */
    @NotNull
    private String groupHeaderName = "svc-group";
    /** 版本所在的请求头名称 */
    @NotNull
    private String versionHeaderName = "svc-version";

    /** dubbo调用超时时间，单位毫秒，默认20s */
    private Long timeoutMilli = TimeUnit.SECONDS.toMillis(20);

    /**
     * 路径规则正则，需要进行分组命名，必须有以下分组：
     * 1. application
     * 2. package
     * 3. class-name
     * 4. method
     * 可用占位符 ${seg} 简写单段（xxx）路径，而 ${mseg} 可以匹配多段（xxx/xxx）
     */
    @NotNull
    private String pathRuleRegex = "/(?<application>${seg})/(?<package>${mseg})/(?<className>${seg})/(?<method>${seg})";

}
