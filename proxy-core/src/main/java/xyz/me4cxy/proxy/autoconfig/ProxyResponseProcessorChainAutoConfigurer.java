package xyz.me4cxy.proxy.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;
import xyz.me4cxy.proxy.core.config.DefaultProxyConfiguration;
import xyz.me4cxy.proxy.core.config.ProxyConfiguration;
import xyz.me4cxy.proxy.core.response.chain.ResponseProcessor;
import xyz.me4cxy.proxy.core.response.chain.ResponseProcessorChain;
import xyz.me4cxy.proxy.spring.SpringConfigurerSupport;

import java.util.Comparator;
import java.util.List;

/**
 * 代理响应处理链自动配置
 *
 * @author jayin
 * @since 2024/01/06
 */
@AutoConfiguration
public class ProxyResponseProcessorChainAutoConfigurer extends SpringConfigurerSupport {

    /**
     * 默认系统代理配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultProxyConfiguration proxyConfiguration() {
        return new DefaultProxyConfiguration();
    }

    /**
     * 响应结果处理链
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ResponseProcessorChain.class)
    public ResponseProcessorChain responseProcessorChain(ProxyConfiguration config) {
        ResponseProcessorChain chain = new ResponseProcessorChain();
        List<ResponseProcessor> responseProcessors = config.responseProcessors();
        if (responseProcessors != null) {
            chain.addProcessors(autowiredAndInitializeBean(responseProcessors));
        }
        return chain;
    }

}
