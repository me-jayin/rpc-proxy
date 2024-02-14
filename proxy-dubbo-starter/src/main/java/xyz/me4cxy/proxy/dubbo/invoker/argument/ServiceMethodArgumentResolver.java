package xyz.me4cxy.proxy.dubbo.invoker.argument;

import org.springframework.core.MethodParameter;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.exception.ProxyIllegalArgumentException;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

import java.util.List;
import java.util.Map;

/**
 * 服务方法参数解析器
 * 参考 org.springframework.web.method.support.HandlerMethodArgumentResolver
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/09
 */
public interface ServiceMethodArgumentResolver {

    /**
     * 判断当前解析器是否支持该参数
     * @param parameter
     */
    boolean supportsParameter(ProxyMethodParameterMetadata parameter);

    /**
     * 解析参数，根据参数信息从请求中获取到内容
     * @param param
     * @param request
     * @return
     */
    Object resolveArgument(ProxyMethodParameterMetadata param, ProxyRequest request);

}
