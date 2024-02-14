package xyz.me4cxy.proxy.dubbo.invoker.argument;

import org.springframework.lang.Nullable;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.exception.ProxyIllegalArgumentException;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务方法参数解析器复合
 * 参考：org.springframework.web.method.support.HandlerMethodArgumentResolverComposite
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/10
 */
public class ServiceMethodArgumentResolverComposite implements ServiceMethodArgumentResolver {
    private final List<ServiceMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    @Override
    public Object resolveArgument(ProxyMethodParameterMetadata param, ProxyRequest request) {
        ServiceMethodArgumentResolver resolver = getArgumentResolver(param);
        if (resolver == null) {
            throw new ProxyIllegalArgumentException("参数不正确", "不支持的参数类型[" + param.getParamType() + " " + param.getType().getTypeClass() + "]");
        }
        return resolver.resolveArgument(param, request);
    }

    /**
     * 获取支持当前参数的解析器
     * @param param
     * @return
     */
    private ServiceMethodArgumentResolver getArgumentResolver(ProxyMethodParameterMetadata param) {
        ServiceMethodArgumentResolver result = null;
        for (ServiceMethodArgumentResolver resolver : this.argumentResolvers) {
            if (resolver.supportsParameter(param)) {
                result = resolver;
                break;
            }
        }
        return result;
    }

    /**
     * 添加列表
     */
    public ServiceMethodArgumentResolverComposite addResolver(ServiceMethodArgumentResolver resolver) {
        this.argumentResolvers.add(resolver);
        return this;
    }

    /**
     * 添加多个解析器
     */
    public ServiceMethodArgumentResolverComposite addResolvers(@Nullable ServiceMethodArgumentResolver... resolvers) {
        if (resolvers != null) {
            addResolvers(Arrays.stream(resolvers).collect(Collectors.toList()));
        }
        return this;
    }

    /**
     * 添加解析器列表
     */
    public ServiceMethodArgumentResolverComposite addResolvers(@Nullable List<? extends ServiceMethodArgumentResolver> resolvers) {
        if (resolvers != null) {
            resolvers = resolvers.stream().filter(v -> v != ServiceMethodArgumentResolverComposite.this).collect(Collectors.toList());
            this.argumentResolvers.addAll(resolvers);
        }
        return this;
    }

    /**
     * 获取只读的解析器
     */
    public List<ServiceMethodArgumentResolver> getResolvers() {
        return Collections.unmodifiableList(this.argumentResolvers);
    }

    @Override
    public boolean supportsParameter(ProxyMethodParameterMetadata parameter) {
        return getArgumentResolver(parameter) != null;
    }
}
