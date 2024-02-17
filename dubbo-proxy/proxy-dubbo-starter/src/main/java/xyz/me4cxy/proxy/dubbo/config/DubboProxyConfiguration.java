package xyz.me4cxy.proxy.dubbo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.invoker.argument.BodyServiceMethodArgumentProcessor;
import xyz.me4cxy.proxy.dubbo.invoker.argument.MapMethodArgumentProcessor;
import xyz.me4cxy.proxy.dubbo.invoker.argument.ModelAttributeMethodProcessor;
import xyz.me4cxy.proxy.dubbo.invoker.argument.NamedValueMethodArgumentResolver;
import xyz.me4cxy.proxy.dubbo.invoker.argument.ServiceMethodArgumentResolver;
import xyz.me4cxy.proxy.dubbo.invoker.argument.ServiceMethodArgumentResolverComposite;
import xyz.me4cxy.proxy.dubbo.invoker.client.DefaultDubboClient;
import xyz.me4cxy.proxy.dubbo.invoker.client.DubboClient;
import xyz.me4cxy.proxy.spring.SpringConfigurerSupport;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

import javax.annotation.Resource;

/**
 * dubbo代理配置
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/11
 */
@AutoConfiguration
public class DubboProxyConfiguration extends SpringConfigurerSupport {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 业务方法参数解析器
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public ServiceMethodArgumentResolver serviceMethodArgumentResolver() {
        ServiceMethodArgumentResolverComposite composite = new ServiceMethodArgumentResolverComposite();
        addArgumentProcessor(composite);
        return composite;
    }

    protected void addArgumentProcessor(ServiceMethodArgumentResolverComposite composite) {
        composite.addResolvers(
                super.autowiredAndInitializeBean(new BodyServiceMethodArgumentProcessor(objectMapper)),
                super.autowiredAndInitializeBean(new MapMethodArgumentProcessor(ProxyParamType.PARAMETER, ProxyRequest::getParameters)),
                super.autowiredAndInitializeBean(new MapMethodArgumentProcessor(ProxyParamType.ADDITIONAL, ProxyRequest::getAdditional)),
                super.autowiredAndInitializeBean(new NamedValueMethodArgumentResolver(ProxyParamType.PARAMETER, ProxyRequest::getParameters)),
                super.autowiredAndInitializeBean(new NamedValueMethodArgumentResolver(ProxyParamType.ADDITIONAL, ProxyRequest::getAdditional)),
                super.autowiredAndInitializeBean(new ModelAttributeMethodProcessor(ProxyParamType.PARAMETER, ProxyRequest::getParameters)),
                super.autowiredAndInitializeBean(new ModelAttributeMethodProcessor(ProxyParamType.ADDITIONAL, ProxyRequest::getAdditional))

        );
    }

    /**
     * dubbo客户端
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public DubboClient dubboClient() {
        return new DefaultDubboClient();
    }

}
