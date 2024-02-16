package xyz.me4cxy.proxy.core;

import lombok.extern.slf4j.Slf4j;
import xyz.me4cxy.proxy.core.interceptor.ProxyInterceptor;
import xyz.me4cxy.proxy.core.invoker.Invoker;
import xyz.me4cxy.proxy.core.invoker.InvokerFactory;
import xyz.me4cxy.proxy.core.mapping.ProxyIdentifyMappingChain;
import xyz.me4cxy.proxy.core.response.chain.ResponseProcessorChain;
import xyz.me4cxy.proxy.exception.IdentifyMappingException;
import xyz.me4cxy.proxy.exception.IdentifyNotSupportException;

import java.util.List;

/**
 * rpc代理调用
 *
 * @author jayin
 * @since 2024/01/07
 */
@Slf4j
public class RpcProxy {
    /**
     * 代理标识映射链
     */
    private final ProxyIdentifyMappingChain identifyMappingChain;
    /**
     * 调用工厂列表
     */
    private final List<InvokerFactory> factories;
    /**
     * 响应处理链
     */
    private final ResponseProcessorChain responseProcessorChain;
    /**
     * 拦截器
     */
    private final List<ProxyInterceptor> interceptors;

    public RpcProxy(ProxyIdentifyMappingChain identifyMappingChain,
                    List<InvokerFactory> factories,
                    ResponseProcessorChain processorChain,
                    List<ProxyInterceptor> interceptors) {
        this.identifyMappingChain = identifyMappingChain;
        this.factories = factories;
        this.responseProcessorChain = processorChain;
        this.interceptors = interceptors;
    }

    public Object proxy(ProxyRequestContext context) {
        ProxyIdentify identify = identifyMappingChain.getIdentify(context);
        if (identify == null) {
            log.info("当前获取代理标识时未找到有效节点，{}", context);
            throw new IdentifyMappingException("无效的服务", "请求" + context.toString() + "未找到对应的服务");
        }
        log.info("请求路径映射后取到代理信息：{}", identify);

        // 创建代理调用上下文
        InvokerFactory factory = factories.stream().filter(f -> f.isSupport(context, identify)).findFirst().orElse(null);
        if (factory == null) {
            throw new IdentifyNotSupportException(identify);
        }
        Invoker<ProxyIdentify> invoker = factory.createInvoker(identify);
        log.debug("代理标识 {} 创建调用器成功：{}", identify.identifyKey(), invoker);

        // 执行前置拦截器等
        for (ProxyInterceptor interceptor : interceptors) {
            if (interceptor.isSupport(context, identify) && !interceptor.before(context, identify, invoker)) {
                log.debug("请求 {} 不被拦截器 {} 允许", context, interceptor);
                return null;
            }
        }

        // 进行实际调用
        Object res = invoker.invoke(context);
        // 对响应结果进行封装
        return responseProcessorChain.process(res);
    }

}
