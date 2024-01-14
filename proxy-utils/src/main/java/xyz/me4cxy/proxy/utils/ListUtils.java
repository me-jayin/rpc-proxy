package xyz.me4cxy.proxy.utils;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * List工具类
 *
 * @author jayin
 * @since 2024/01/07
 */
public class ListUtils {

    private ListUtils() {}

    /**
     * 将 Enumeration 转成 List
     * @param e
     * @return
     * @param <T>
     */
    public static <T> List<T> toList(Enumeration<T> e) {
        List<T> list = new ArrayList<>();
        while (e.hasMoreElements()) {
            list.add(e.nextElement());
        }
        return list;
    }

}
