package xyz.me4cxy.proxy.dubbo.invoker.argument;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.invoker.argument.convert.ValueConverter;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.exception.ProxyIllegalArgumentException;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 抽象map参数处理器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/12
 */
@Slf4j
public class MapMethodArgumentProcessor extends AbstractParameterTypeValueSourceArgumentProcessor {
    @Resource
    private ConversionService conversionService;

    public MapMethodArgumentProcessor(ProxyParamType targetType, Function<ProxyRequest, Map<String, List<String>>> valueSource) {
        super(targetType, valueSource);
    }

    @Override
    public boolean supportsParameter0(ProxyMethodParameterMetadata parameter) {
        return Map.class.isAssignableFrom(parameter.getType().getTypeClass());
    }

    @Override
    public Object resolveArgument(ProxyMethodParameterMetadata param, ProxyRequest request) {
        Map<String, List<String>> parameterValues = getParameterValues(request);

        Class paramTypeClass = param.getType().getTypeClass();
        // 解析获取到value的泛型
        ResolvableType generic = ResolvableType.forClass(paramTypeClass).asMap().getGeneric(1);
        try {
            Map o = (Map) paramTypeClass.newInstance();

            ValueConverter converter = ValueConverter.of(conversionService, generic);
            for (Map.Entry<String, List<String>> entry : parameterValues.entrySet()) {
                String key = entry.getKey();
                // 只有使用"参数名."的作为开头的参数才装进去
                if (StringUtils.startsWith(key, param.getName() + ".")) {
                    o.put(key, converter.convert(entry.getValue().toArray(new String[0])));
                }
            }

            return o;
        } catch (Exception e) {
            log.error("初始化对象失败", e);
            throw new ProxyIllegalArgumentException("参数初始化失败", "参数[" + param.getName() + "]实例化失败");
        }
    }

}
