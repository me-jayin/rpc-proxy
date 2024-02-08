package xyz.me4cxy.proxy.autoconfig;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.core.RpcProxy;
import xyz.me4cxy.proxy.core.invoker.InvokerFactory;
import xyz.me4cxy.proxy.core.mapping.ProxyIdentifyMapping;
import xyz.me4cxy.proxy.core.mapping.ProxyIdentifyMappingChain;

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
     * @param chain
     * @param factories
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RpcProxy rpcProxy(ProxyIdentifyMappingChain chain, List<InvokerFactory> factories) {
        return new RpcProxy(chain, factories);
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
