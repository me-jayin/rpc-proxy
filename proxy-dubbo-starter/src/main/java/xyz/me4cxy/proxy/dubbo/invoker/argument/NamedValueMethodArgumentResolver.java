package xyz.me4cxy.proxy.dubbo.invoker.argument;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.invoker.argument.convert.ValueConverter;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 基于命名参数获取方法参数的值
 * 支持简单的类型、Collection
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/12
 */
public class NamedValueMethodArgumentResolver extends AbstractParameterTypeValueSourceArgumentProcessor implements ApplicationContextAware {
    private ConfigurableBeanFactory beanFactory;
    @Resource
    private ConversionService conversionService;

    public NamedValueMethodArgumentResolver(ProxyParamType targetType, Function<ProxyRequest, Map<String, List<String>>> valueSource) {
        super(targetType, valueSource);
    }

    @Override
    protected boolean supportsParameter0(ProxyMethodParameterMetadata parameter) {
        Class typeClass = parameter.getType().getTypeClass();
        return !Map.class.isAssignableFrom(typeClass) && (
                BeanUtils.isSimpleProperty(typeClass)
                || Collection.class.isAssignableFrom(typeClass)
        );
    }

    @Override
    public Object resolveArgument(ProxyMethodParameterMetadata param, ProxyRequest request) {
        String name = beanFactory.resolveEmbeddedValue(param.getName());
        List<String> values = getParameterValues(request).get(name);

        Class paramTypeClass = param.getType().getTypeClass();
        // 解析获取到value的泛型
        ResolvableType resolvableType = ResolvableType.forClass(paramTypeClass);
        ValueConverter converter = ValueConverter.of(conversionService, resolvableType);
        return converter.convert(values == null ? null : values.toArray(new String[0]));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        beanFactory = ((AbstractApplicationContext) applicationContext).getBeanFactory();
    }
}
