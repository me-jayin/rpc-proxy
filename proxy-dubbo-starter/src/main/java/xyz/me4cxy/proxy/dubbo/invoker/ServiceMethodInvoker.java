package xyz.me4cxy.proxy.dubbo.invoker;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.invoker.Invoker;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.exception.NoUniqueServiceMethodException;
import xyz.me4cxy.proxy.dubbo.exception.ServiceMethodNotFoundException;
import xyz.me4cxy.proxy.dubbo.invoker.argument.ServiceMethodArgumentResolver;
import xyz.me4cxy.proxy.dubbo.invoker.client.DubboClient;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;
import xyz.me4cxy.proxy.dubbo.invoker.matcher.MethodMatcher;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodMetadata;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过元数据的dubbo调用器
 *
 * @author jayin
 * @since 2024/02/03
 */
@Slf4j
public class ServiceMethodInvoker implements Invoker<DubboProxyIdentify> {
    /**
     * 方法匹配器，用于匹配找出符合条件的方法
     */
    @Setter
    @Resource
    private MethodMatcher methodMatcher;
    /**
     * 参数解析器
     */
    @Setter
    @Resource
    private ServiceMethodArgumentResolver argumentResolver;
    /**
     * dubbo客户端
     */
    @Setter
    @Resource
    private DubboClient dubboClient;
    /**
     * 服务描述元数据
     */
    private final List<ProxyMethodMetadata> methods;
    /**
     * 服务标识
     */
    private final ServiceIdentify serviceIdentify;
    /**
     * 方法名称
     */
    private final String methodName;

    public ServiceMethodInvoker(ProxyServiceMetadata metadata, DubboProxyIdentify identify) {
        this.serviceIdentify = ServiceIdentify.of(identify);
        this.methodName = identify.getMethod();
        this.methods = metadata.getMethods(methodName);
        if (methods == null) {
            throw new ServiceMethodNotFoundException(identify.getService() + "#" + methodName);
        }
    }

    @Override
    public Object invoke(ProxyRequestContext context) {
        // 1.过滤出符合method以及命中参数最多的方法
        List<ProxyMethodMetadata> matchResult = methodMatcher.match(methods, context);
        log.info("当前请求 {} 匹配到方法：{}", context, matchResult);
        if (matchResult.isEmpty()) {
            throw new ServiceMethodNotFoundException(serviceIdentify.getService() + "#" + methodName);
        } else if (matchResult.size() > 1) {
            throw new NoUniqueServiceMethodException(serviceIdentify.getService() + "#" + methodName);
        }

        ProxyMethodMetadata method = matchResult.get(0);
        // 2.获取属性
        Object[] args = resolveArguments(method.getParams(), context.getRequest());
        // 进行dubbo实际调用
        return dubboClient.invoke(serviceIdentify, method, args);
    }

    private Object[] resolveArguments(List<ProxyMethodParameterMetadata> params, ProxyRequest request) {
        Object[] args = new Object[params.size()];
        int idx = 0;
        for (ProxyMethodParameterMetadata param : params) {
            args[idx++] = argumentResolver.resolveArgument(param, request);
        }
        log.debug("解析完成，入参：{}", args);
        return args;
    }

    @Override
    public String toString() {
        return "Invoker$" + serviceIdentify.getApplicationIdentify() + "@" + serviceIdentify.getService() + "#" + methodName;
    }
}
