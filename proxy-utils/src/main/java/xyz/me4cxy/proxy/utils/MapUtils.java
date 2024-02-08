package xyz.me4cxy.proxy.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Consumer;

/**
 * map工具类
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/07
 */
public class MapUtils {

    /**
     * 获取如果是默认值则将key作为参数交给consumer处理
     * @param map
     * @param key
     * @param consumer
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V getIfNull(Map<K, V> map, K key, Consumer<K> consumer) {
        V v = null;
        if (map == null || (v = map.get(key)) == null) {
            consumer.accept(key);
            return null;
        }
        return v;
    }

    /**
     * 获取字符串如果为空时，则将key交给指定处理器
     * @param map
     * @param key
     * @param consumer
     * @param <K>
     * @return
     */
    public static <K> String getIfBlank(Map<K, String> map, K key, Consumer<K> consumer) {
        String val = getIfNull(map, key, consumer);
        if (StringUtils.isBlank(val)) {
            consumer.accept(key);
            return null;
        }
        return val;
    }

}
