package xyz.me4cxy.proxy.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.core.RpcProxy;
import xyz.me4cxy.proxy.core.interceptor.ProxyInterceptor;
import xyz.me4cxy.proxy.core.invoker.InvokerFactory;
import xyz.me4cxy.proxy.core.mapping.ProxyIdentifyMapping;
import xyz.me4cxy.proxy.core.mapping.ProxyIdentifyMappingChain;
import xyz.me4cxy.proxy.core.response.chain.ResponseProcessorChain;

import java.util.Collections;
import java.util.List;

/**
 * 代理自动配置
 *
 * @author jayin
 * @since 2024/01/06
 */
@AutoConfiguration
public class ProxyAutoConfigurer {

    /**
     * rpc代理器
     * @param identifyMappingChain
     * @param factories
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RpcProxy rpcProxy(ProxyIdentifyMappingChain identifyMappingChain,
                             List<InvokerFactory> factories,
                             ResponseProcessorChain responseProcessorChain,
                             @Autowired(required = false) List<ProxyInterceptor> interceptors) {
        return new RpcProxy(
                identifyMappingChain,
                factories,
                responseProcessorChain,
                interceptors == null ? Collections.emptyList() : interceptors
        );
    }

    /**
     * 代理标识过滤链
     * @param mappings
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ProxyIdentifyMappingChain proxyIdentifyMappingChain(List<ProxyIdentifyMapping> mappings) {
        return new ProxyIdentifyMappingChain(mappings);
    }

}
