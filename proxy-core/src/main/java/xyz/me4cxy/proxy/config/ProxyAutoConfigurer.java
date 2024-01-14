package xyz.me4cxy.proxy.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import xyz.me4cxy.proxy.core.RpcProxy;

/**
 * 代理自动配置
 *
 * @author jayin
 * @since 2024/01/06
 */
@AutoConfiguration
public class ProxyAutoConfigurer {

    @Bean
    @ConditionalOnMissingBean(RpcProxy.class)
    public RpcProxy dubboProxy() {
        return new RpcProxy();
    }

}
