package xyz.me4cxy.proxy.core.response.chain;

import xyz.me4cxy.proxy.core.response.wrapper.model.ApiResponse;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 类信息移除处理器
 * 由于 dubbo 泛化调用时，会返回具体类的类名称，因此为了保证数据安全需要将类信息剔除
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/15
 */
public class ClassValueRemoveProcessor implements ResponseProcessor {
    @Override
    public Object process(Object result) {
        if (result == null) {
            return null;
        } else if (result instanceof Collection || result.getClass().isArray()) {
            Collection c = result.getClass().isArray() ? Arrays.asList(result) : ((Collection) result);
            for (Object o : c) {
                removeClassValue(o);
            }
        } else {
            removeClassValue(result);
        }

        return result;
    }

    private void removeClassValue(Object result) {
        if (result instanceof ApiResponse) {
            process(((ApiResponse<?>) result).getData());
        } else if (result instanceof Map) {
            removeClassValue((Map) result);
        }
    }

    private void removeClassValue(Map map) {
        if (map != null) {
            map.remove("class");
        }
        for (Object value : map.values()) {
            process(value);
        }
    }

    @Override
    public boolean isSupport(Object v) {
        return true;
    }
}
