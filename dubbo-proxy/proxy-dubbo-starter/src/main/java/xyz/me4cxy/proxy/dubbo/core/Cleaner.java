package xyz.me4cxy.proxy.dubbo.core;

/**
 * 清除器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/14
 */
public interface Cleaner {

    /**
     * 清除
     */
    void clear();

    /**
     * 清除器名称
     * @return
     */
    String name();
}
