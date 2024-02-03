package xyz.me4cxy.proxy.dubbo.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import xyz.me4cxy.proxy.core.invoker.InvokerFactory;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.invoker.ServiceMetadataInvokerFactory;

/**
 * dubbo代理自动配置
 *
 * @author jayin
 * @since 2024/01/29
 */
@AutoConfiguration
public class DubboProxyAutoConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public InvokerFactory<DubboProxyIdentify> dubboServiceInvokerFactory() {
        return new ServiceMetadataInvokerFactory();
    }

}
