package xyz.me4cxy.proxy.dubbo.mapping;

import lombok.extern.slf4j.Slf4j;
import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.core.mapping.ProxyIdentifyMapping;
import xyz.me4cxy.proxy.dubbo.resolver.PathRuleProxyIdentifyResolver;

/**
 * 基于路径规则进行代理映射
 *
 * @author jayin
 * @since 2024/01/14
 */
@Slf4j
public class PathRuleProxyIdentifyMapping implements ProxyIdentifyMapping {
    private final PathRuleProxyIdentifyResolver resolver;

    public PathRuleProxyIdentifyMapping(PathRuleProxyIdentifyResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public ProxyIdentify getIdentify(ProxyRequestContext context) {
        return resolver.resolve(context);
    }

}
