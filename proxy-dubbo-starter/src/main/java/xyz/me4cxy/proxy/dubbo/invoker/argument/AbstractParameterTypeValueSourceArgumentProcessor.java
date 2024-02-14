package xyz.me4cxy.proxy.dubbo.invoker.argument;

import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.exception.ProxyIllegalArgumentException;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 基于代理参数类型，从而获取对应数据值的参数处理器
 * @author Jayin】、
 * @email 1035933250@qq.com
 * @date 2024/02/12
 */
public abstract class AbstractParameterTypeValueSourceArgumentProcessor implements ServiceMethodArgumentResolver {

    private ProxyParamType targetType;
    private Function<ProxyRequest, Map<String, List<String>>> valueSource;

    public AbstractParameterTypeValueSourceArgumentProcessor(ProxyParamType targetType, Function<ProxyRequest, Map<String, List<String>>> valueSource) {
        this.targetType = targetType;
        this.valueSource = valueSource;
    }

    @Override
    public boolean supportsParameter(ProxyMethodParameterMetadata parameter) {
        return targetType.equals(parameter.getParamType()) && supportsParameter0(parameter);
    }

    /**
     * 除了参数类型外的其他条件
     * @param parameter
     * @return
     */
    protected boolean supportsParameter0(ProxyMethodParameterMetadata parameter) {
        return true;
    }

    /**
     * 根据类型获取数据来源
     * @param request
     * @return
     */
    protected Map<String, List<String>> getParameterValues(ProxyRequest request) {
        return valueSource.apply(request);
    }

}
