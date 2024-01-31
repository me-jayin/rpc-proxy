package xyz.me4cxy.proxy.core.config;

import xyz.me4cxy.proxy.core.response.chain.ResponseProcessor;
import xyz.me4cxy.proxy.core.response.wrapper.ResponseWrapper;

import java.util.List;

/**
 * 代理配置信息
 * @author jayin
 * @since 2024/01/29
 */
public interface ProxyConfiguration {

    /**
     * 获取响应结果处理器
     * @return
     */
    List<ResponseProcessor> responseProcessors();

    /**
     * 响应包包装类
     * @return
     */
    ResponseWrapper responseWrapper();

}
