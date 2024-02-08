package xyz.me4cxy.proxy.core;

import lombok.extern.slf4j.Slf4j;
import xyz.me4cxy.proxy.core.invoker.Invoker;
import xyz.me4cxy.proxy.core.invoker.InvokerFactory;
import xyz.me4cxy.proxy.core.mapping.ProxyIdentifyMappingChain;
import xyz.me4cxy.proxy.exception.IdentifyMappingException;
import xyz.me4cxy.proxy.exception.IdentifyNotSupportException;
import xyz.me4cxy.proxy.exception.ProxyException;

import java.lang.reflect.Proxy;
import java.util.List;

/**
 * rpc代理调用
 *
 * @author jayin
 * @since 2024/01/07
 */
@Slf4j
public class RpcProxy {
    private ProxyIdentifyMappingChain chain;
    private List<InvokerFactory> factories;

    public RpcProxy(ProxyIdentifyMappingChain chain, List<InvokerFactory> factories) {
        this.chain = chain;
        this.factories = factories;
    }

    public Object proxy(ProxyRequestContext context) {
        ProxyIdentify identify = chain.getIdentify(context);
        if (identify == null) {
            log.info("当前获取代理标识时未找到有效节点，context: {}", context);
            throw new IdentifyMappingException("无效的服务", "请求" + context.toString() + "未找到对应的服务");
        }
        log.info("请求路径映射后取到代理信息：{}", identify);

        // 创建代理调用上下文
        InvokerFactory factory = factories.stream().filter(f -> f.isSupport(identify)).findFirst().orElse(null);
        if (factory == null) {
            throw new IdentifyNotSupportException(identify);
        }
        Invoker<ProxyIdentify> invoker = factory.createInvoker(identify);
        log.debug("代理标识 {} 创建调用器成功：{}", identify.identifyKey(), invoker);

        // TODO 执行前置拦截器等
        // 获取调用结果
        Object res = invoker.invoke(context);

        // 进行请求调用
        return null;
    }

}
