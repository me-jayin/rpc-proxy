package xyz.me4cxy.proxy.dubbo.core;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 清除注册器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/14
 */
@Slf4j
public class CleanerRegistry {
    private static Map<String, List<Cleaner>> CLEANERS = new ConcurrentHashMap<>();

    /**
     * 添加某应用的清除器
     * @param application
     * @param cleaner
     */
    public static void addCleaner(String application, Cleaner cleaner) {
        CLEANERS.computeIfAbsent(application, key -> new CopyOnWriteArrayList<>()).add(cleaner);
    }

    /**
     * 执行应用清除任务
     * @param application
     */
    public static void clear(String application) {
        List<Cleaner> cleaners = CLEANERS.remove(application);
        if (cleaners == null) {
            log.info("应用暂未注册清除器，无需执行清除任务");
            return;
        }

        log.info("开始执行应用[{}]的清除器", application);
        for (Cleaner cleaner : cleaners) {
            cleaner.clear();
            log.info("应用[{}]清除器 {} 执行完成", application, cleaner.name());
        }
        log.info("应用[{}]执行所有清除器完成", application);
    }

}
