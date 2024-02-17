package xyz.me4cxy.proxy.dubbo.mapping.resolver;

import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.exception.IdentifyMappingException;
import xyz.me4cxy.proxy.utils.PathPatternUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基于路径和请求头获取代理标识
 *
 * @author jayin
 * @since 2024/01/14
 */
@Slf4j
public class PathAndHeaderRuleIdentifyResolver implements PathRuleProxyIdentifyResolver {

    /**
     * 路径分隔符
     */
    public static final String PATH_SEPARATOR = "/";
    /**
     * 业务包分隔符
     */
    public static final String PACKAGE_SEPARATOR = ".";
    /**
     * 应用名称连字符
     */
    public static final String APPLICATION_HYPHEN = "-";
    /**
     * 下划线
     */
    public static final String APPLICATION_UNDERLINE = "_";
    /**
     * dubbo代理配置
     */
    private final DubboProxyConfig dubboProxyConfig;

    public PathAndHeaderRuleIdentifyResolver(DubboProxyConfig dubboProxyConfig) {
        this.dubboProxyConfig = dubboProxyConfig;
    }

    @Override
    public ProxyIdentify resolve(ProxyRequestContext context) {
        String path = context.getRequest().getPath();

        Map<String, String> serviceInfo = PathPatternUtils.matchGroupToMap(dubboProxyConfig.getPathRuleRegex(), path);
        if (MapUtils.isEmpty(serviceInfo)) {
            throw new IdentifyMappingException("请求标识解析失败", "路径" + path + " 不符合路径规则");
        }
        List<String> notFoundParam = PathPatternUtils.IDENTIFY_PARAM_NAMES.stream()
                .filter(p -> StringUtils.isEmpty(serviceInfo.get(p))).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(notFoundParam)) {
            throw new IdentifyMappingException("请求缺少标识参数", "请求路径 " + path + " 匹配后缺少参数：" + String.join(",", notFoundParam));
        }

        // 获取版本号和分组
        String version = context.getRequest().getAdditionalIfNull(dubboProxyConfig.getVersionHeaderName(), dubboProxyConfig.getDefaultVersion());
        String group = context.getRequest().getAdditionalIfNull(dubboProxyConfig.getGroupHeaderName(), dubboProxyConfig.getDefaultGroup());
        String application = serviceInfo.get(PathPatternUtils.PATH_GROUP_APPLICATION),
                packageName = serviceInfo.get(PathPatternUtils.PATH_GROUP_PACKAGE),
                className = serviceInfo.get(PathPatternUtils.PATH_GROUP_CLASS_NAME),
                method = serviceInfo.get(PathPatternUtils.PATH_GROUP_METHOD);

        return DubboProxyIdentify.builder()
                .application(application)
                .service(getDubboServiceName(application, packageName, className))
                .method(xyz.me4cxy.proxy.utils.StringUtils.toCamel(method))
                .group(group)
                .version(version)
                .build();
    }

    /**
     * 获取服务名
     * @return
     */
    public String getDubboServiceName(String application, String packageName, String className) {
        String service = getPackageName(application, packageName) + PACKAGE_SEPARATOR + getClassName(className);
        log.debug("[标识字段] 获取服务全类名：{}", service);
        return service;
    }

    public String getPackageName(String application, String packageName) {
        String packageAppSegment = StringUtils.lowerCase(StringUtils.replace(application, APPLICATION_HYPHEN, APPLICATION_UNDERLINE));
        String actPackageName = StringUtils.join(Arrays.asList(
                dubboProxyConfig.getPackagePrefix(), // 配置的包前缀
                packageAppSegment, // 转包名后的
                StringUtils.replace(packageName, PATH_SEPARATOR, PACKAGE_SEPARATOR) // 请求路径中的包路径
        ), PACKAGE_SEPARATOR);
        log.debug("[标识字段] 应用信息：{}，包信息：{}，解析后实际包名：{}", application, packageName, actPackageName);
        return actPackageName;
    }

    /**
     * 获取类名
     * @return
     */
    public String getClassName(String className) {
        // 将 xxx-xxx 转成 XxxXxx 并加上 类后缀
        String _className = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, className) + dubboProxyConfig.getServiceClassSuffix();
        log.debug("[标识字段] 类名：{}，解析后类名：{}", className, _className);
        return _className;
    }

}
