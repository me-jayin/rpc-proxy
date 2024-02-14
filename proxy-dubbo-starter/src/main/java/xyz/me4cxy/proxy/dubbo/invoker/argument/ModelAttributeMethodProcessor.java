package xyz.me4cxy.proxy.dubbo.invoker.argument;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.invoker.argument.convert.ValueConverter;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.exception.ProxyIllegalArgumentException;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * 实体属性方法处理
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/13
 */
@Slf4j
public class ModelAttributeMethodProcessor extends AbstractParameterTypeValueSourceArgumentProcessor {
    @Resource
    private ConversionService conversionService;

    public ModelAttributeMethodProcessor(ProxyParamType targetType, Function<ProxyRequest, Map<String, List<String>>> valueSource) {
        super(targetType, valueSource);
    }

    @Override
    public Object resolveArgument(ProxyMethodParameterMetadata param, ProxyRequest request) {
        String name = param.getName();
        try {
            Class paramTypeClass = param.getType().getTypeClass();
            return createAndBindObject(paramTypeClass, getParameterValues(request), name, false);
        } catch (Exception e) {
            log.error("初始化对象失败", e);
            throw new ProxyIllegalArgumentException("参数初始化失败", "参数[" + param.getName() + "]实例化失败");
        }
    }

    private Object createAndBindObject(Class<?> clazz, Map<String, List<String>> params, String path, boolean lazyInit) throws IllegalAccessException, InstantiationException {
        AtomicReference<Object> obj;
        if (lazyInit) {
            obj = new AtomicReference<>(null);
        } else {
            obj = new AtomicReference<>(BeanUtils.instantiateClass(clazz));
        }
        // 进行遍历填充
        ReflectionUtils.doWithFields(clazz, new PopulateFieldCallback(obj, path, params));
        return obj.get();
    }

    @AllArgsConstructor
    private class PopulateFieldCallback implements ReflectionUtils.FieldCallback {
        /**
         * 原对象
         */
        private AtomicReference<Object> object;
        /**
         * 当前对象的路径
         */
        private String path;
        /**
         * 参数
         */
        private Map<String, List<String>> params;

        @SneakyThrows
        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            final Class<?> fieldType = field.getType();
            Object value = null;
            if (Map.class.isAssignableFrom(fieldType)) {
                value = populateMap(field);
            } else if (Collection.class.isAssignableFrom(fieldType) || fieldType.isArray()) {
                value = populateCollect(field);
            } else if (BeanUtils.isSimpleProperty(fieldType)) {
                value = populateSimpleProperty(field);
            } else { // 实体
                value = createAndBindObject(fieldType, params, path + "." + field.getName(), true);
            }

            try {
                if (value == null) {
                    return;
                }
                if (object.get() == null) {
                    object.set(BeanUtils.instantiateClass(field.getDeclaringClass()));
                }

                FieldUtils.writeField(field, object.get(), value, true);
            } catch (IllegalAccessException e) {
                log.error("设置属性[" + path + "." + field.getName() + "]失败", e);
                throw new ProxyIllegalArgumentException("参数值设置失败");
            }
        }

        private Object populateCollect(Field field) {
            return null;
        }

        private Object populateSimpleProperty(Field field) {
            String fieldPath = path + "." + field.getName();
            List<String> values = params.get(fieldPath);
            if (CollectionUtils.isEmpty(values)) {
                return null;
            }
            return conversionService.convert(values.get(0), field.getType());
        }

        private Object populateMap(Field field) {
            Map map = (Map) BeanUtils.instantiateClass(field.getType());
            String fieldPath = path + "." + field.getName() + ".";
            log.debug("开始填充属性 {} 的Map值", fieldPath);

            // 解析获取到value的泛型
            ResolvableType resolvableType = ResolvableType.forField(field).asMap();
            ResolvableType keyGeneric = resolvableType.getGeneric(0), valueGeneric = resolvableType.getGeneric(1);

            ValueConverter converter = ValueConverter.of(conversionService, valueGeneric);
            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String key = entry.getKey();
                // 只有使用"参数名."的作为开头的参数才装进去
                if (StringUtils.startsWith(key, fieldPath)) {
                    // 处理键
                    Object actKey = conversionService.convert(
                            StringUtils.substring(key, fieldPath.length()), keyGeneric.resolve()
                    );
                    // 填充值
                    map.put(actKey, converter.convert(entry.getValue().toArray(new String[0])));
                    log.debug("往 {} 属性填充键 {} 成功", fieldPath, actKey);
                }
            }
            log.debug("填充属性 {} 值成功", fieldPath);
            return map;
        }

    }

}
