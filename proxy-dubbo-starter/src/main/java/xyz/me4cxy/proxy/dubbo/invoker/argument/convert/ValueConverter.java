package xyz.me4cxy.proxy.dubbo.invoker.argument.convert;

import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 值转换器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/12
 */
public interface ValueConverter {

    /**
     * 转换值
     * @param values
     * @return
     */
    Object convert(String[] values);

    static ValueConverter of(ConversionService service, ResolvableType type) {
        Class<?> clazz = type.resolve();
        if (Collection.class.isAssignableFrom(clazz) || clazz.isArray()) {
            Class<?> collectGeneric = type.asCollection().getGeneric(0).resolve();
            return values -> {
                if (values == null) {
                    return null;
                }
                List<?> collect = Stream.of(values).map(v -> service.convert(v, collectGeneric)).collect(Collectors.toList());
                if (clazz.isArray()) { // 转数组
                    return collect.toArray();
                }
                return collect;
            };
        } else {
            // 直接转换
            return values -> {
                if (values == null || values.length == 0) {
                    return null;
                }
                return service.convert(values[0], clazz);
            };
        }
    }

}
