package xyz.me4cxy.proxy.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import xyz.me4cxy.proxy.core.response.chain.ResponseProcessorChain;
import xyz.me4cxy.proxy.core.response.chain.ResponseProcessor;
import xyz.me4cxy.proxy.core.response.chain.ResponseWrapperProcessor;
import xyz.me4cxy.proxy.core.response.wrapper.DefaultResponseWrapper;
import xyz.me4cxy.proxy.core.response.wrapper.ResponseWrapper;

import java.util.List;

/**
 * 代理响应处理链自动配置
 *
 * @author jayin
 * @since 2024/01/06
 */
@AutoConfiguration
public class ProxyResponseProcessorChainAutoConfigurer {

    /**
     * 响应结果处理链
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ResponseProcessorChain.class)
    public ResponseProcessorChain responseProcessorChain(List<ResponseProcessor> responseProcessors) {
        return new ResponseProcessorChain(responseProcessors);
    }

    /**
     * 响应包装处理器，需要至于处理链最后一个
     * @param wrapper
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ResponseWrapperProcessor.class)
    public ResponseWrapperProcessor responseWrapperProcessor(ResponseWrapper wrapper) {
        return new ResponseWrapperProcessor(wrapper);
    }

    /**
     * 响应包装器，实际的包装操作
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ResponseWrapper.class)
    public ResponseWrapper responseWrapper() {
        return new DefaultResponseWrapper();
    }

}
