package xyz.me4cxy.proxy.dubbo.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import xyz.me4cxy.proxy.core.invoker.InvokerFactory;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.invoker.matcher.DefaultProxyRequestMethodMatcher;
import xyz.me4cxy.proxy.dubbo.invoker.matcher.MethodMatcher;
import xyz.me4cxy.proxy.dubbo.mapping.PathRuleProxyIdentifyMapping;
import xyz.me4cxy.proxy.dubbo.invoker.ServiceMetadataInvokerFactory;
import xyz.me4cxy.proxy.dubbo.mapping.resolver.DubboProxyConfig;
import xyz.me4cxy.proxy.dubbo.mapping.resolver.PathAndHeaderRuleIdentifyResolver;
import xyz.me4cxy.proxy.dubbo.mapping.resolver.PathRuleProxyIdentifyResolver;

import javax.annotation.Resource;

/**
 * dubbo代理自动配置
 *
 * @author jayin
 * @since 2024/01/29
 */
@AutoConfiguration
@EnableConfigurationProperties(DubboProxyConfig.class)
public class DubboProxyAutoConfigurer {
    @Resource
    private DubboProxyConfig dubboProxyConfig;

    @Bean
    @ConditionalOnMissingBean
    public InvokerFactory<DubboProxyIdentify> dubboServiceInvokerFactory() {
        return new ServiceMetadataInvokerFactory();
    }

    /**
     * 基于路径规则的标识映射
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public PathRuleProxyIdentifyMapping pathRuleProxyIdentifyMapping() {
        return new PathRuleProxyIdentifyMapping(pathRuleProxyIdentifyResolver());
    }

    /**
     * 路径代理标识解析器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public PathRuleProxyIdentifyResolver pathRuleProxyIdentifyResolver() {
        return new PathAndHeaderRuleIdentifyResolver(dubboProxyConfig);
    }

    /**
     * 默认的方法优先级匹配器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MethodMatcher methodMatcher() {
        return new DefaultProxyRequestMethodMatcher();
    }




}
