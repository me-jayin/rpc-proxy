package xyz.me4cxy.proxy.core.config;

import xyz.me4cxy.proxy.core.response.chain.ClassValueRemoveProcessor;
import xyz.me4cxy.proxy.core.response.chain.ResponseProcessor;
import xyz.me4cxy.proxy.core.response.chain.ResponseWrapperProcessor;
import xyz.me4cxy.proxy.core.response.wrapper.DefaultResponseWrapper;
import xyz.me4cxy.proxy.core.response.wrapper.ResponseWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认代理配置
 *
 * @author jayin
 * @since 2024/01/29
 */
public class DefaultProxyConfiguration implements ProxyConfiguration {

    /**
     * 获取响应结果处理器
     * @return
     */
    public List<ResponseProcessor> responseProcessors() {
        List<ResponseProcessor> processors = new ArrayList<>();
        // 类信息移除处理器
        processors.add(new ClassValueRemoveProcessor());
        // 默认拼接响应结果包装类
        processors.add(new ResponseWrapperProcessor(responseWrapper()));
        return processors;
    }

    /**
     * 响应包包装类
     * @return
     */
    public ResponseWrapper responseWrapper() {
        return new DefaultResponseWrapper();
    }

}
