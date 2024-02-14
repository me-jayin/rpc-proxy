package xyz.me4cxy.proxy.dubbo.invoker.argument;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.exception.TypeDefinitionNotFoundException;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.dubbo.metadata.type.ProxyTypeMetadata;
import xyz.me4cxy.proxy.exception.ProxyIllegalArgumentException;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

/**
 * 基于方法体的方法参数解析器，直接将方法体内容转为指定类型即可
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/10
 */
@Slf4j
public class BodyServiceMethodArgumentProcessor implements ServiceMethodArgumentResolver {
    private ObjectMapper objectMapper;

    public BodyServiceMethodArgumentProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(ProxyMethodParameterMetadata parameter) {
        return parameter.getParamType().equals(ProxyParamType.BODY);
    }

    @Override
    public Object resolveArgument(ProxyMethodParameterMetadata param, ProxyRequest request) {
        ProxyTypeMetadata type = param.getType();
        try {
            return request.getBody() != null ? objectMapper.readValue(request.getBody(), type.getTypeClass()) : null;
        } catch (JsonProcessingException e) {
            throw new ProxyIllegalArgumentException("请求体反序列化失败", e);
        }
    }
}
