package xyz.me4cxy.proxy.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注解解析器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
@Slf4j
public class AnnotationResolverUtils {
    /**
     * 注解正则
     */
    private static final Pattern ANNOTATION_PATTERN = Pattern.compile("^" +
            "(?<annotation>@[\\w\\.]+)" +
            "\\(" +
            "(?<attr>(" +
            "(\\w+)" +
            "=" +
            "([\\s\\S]*(, )?)" +
            ")*)" +
            "\\)");
    /**
     * 注解分组正则，用于匹配某字符串中的注解
     */
    private static final Pattern ANNOTATION_GROUP_PATTERN = Pattern.compile("(?<annotation>(@[\\w\\.]+)\\(((\\w+)=(\\[[\\s\\S]*])|[^,]*(, )?)*?\\))");
    /**
     * 键值正则
     */
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("(?<name>\\w+)=(?<value>(\\[[\\s\\S]*?])|[^,]*)");
//    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("(?<name>(\\w+))=(?<value>(\\[[\\s\\S]*])|[^,]*)?(, )?");

    /**
     * 解析注解列表
     * @param annos
     * @return
     */
    public static Map<String, List<Map<String, String>>> resolve(List<String> annos) {
        Map<String, List<Map<String, String>>> container = new HashMap<>();
        for (String anno : annos) {
            resolveSingleAnnotation(anno, container);
        }
        return container;
    }

    private static void resolveSingleAnnotation(String annotation, Map<String, List<Map<String, String>>> container) {
        if (!isAnnotation(annotation)) {
            log.debug("字符串 {} 非注解字符串", annotation);
            return;
        }

        Matcher matcher = ANNOTATION_PATTERN.matcher(annotation);
        while (matcher.find()) {
            String type = matcher.group("annotation");
            String attrs = matcher.group("attr");
            Map<String, String> attributes = resolveAttributes(attrs);
            container.computeIfAbsent(type, key -> new ArrayList<>()).add(attributes);
            // 开始比例判断值是否是其他注解，如果是则拆解开
            for (String value : attributes.values()) {
                for (String s : extractAnnotationList(value)) {
                    resolveSingleAnnotation(s, container);
                }
            }
        }
    }

    /**
     * 提取注解列表
     * @param str
     * @return
     */
    private static List<String> extractAnnotationList(String str) {
        Matcher matcher = ANNOTATION_GROUP_PATTERN.matcher(str);
        List<String> annos = new ArrayList<>();
        while (matcher.find()) {
            annos.add(matcher.group("annotation"));
        }
        return annos;
    }

    /**
     * 基于属性字符串解析成属性映射表
     * @param attrs
     * @return
     */
    private static Map<String, String> resolveAttributes(String attrs) {
        Matcher kv = KEY_VALUE_PATTERN.matcher(attrs);
        Map<String, String> map = new HashMap<>();
        while (kv.find()) {
            map.put(kv.group("name"), kv.group("value"));
        }
        return map;
    }

    /**
     * 判断是否是注解字符串
     * @param str
     * @return
     */
    public static boolean isAnnotation(String str) {
        return ANNOTATION_PATTERN.matcher(str).matches();
    }

    public static void main(String[] args) {
//        System.out.println(ANNOTATION_PATTERN.pattern());
////        Matcher matcher = ANNOTATION_PATTERN.matcher("@xyz.me4cxy.proxy.annotation.HttpParam(paramType=PARAMETER, defaultVal=, index=0, name=id)");
//        Matcher matcher = ANNOTATION_PATTERN.matcher("@xyz.me4cxy.proxy.annotation.HttpParams(value=[@xyz.me4cxy.proxy.annotation.HttpParam(paramType=PARAMETER, defaultVal=, index=0, name=id)], key=123)");
//        while (matcher.find()) {
//            System.out.println(matcher.group("annotation"));
//            System.out.println(matcher.group("attr"));
//            System.out.println("...");
//        }
//
//        Matcher kv = KEY_VALUE_PATTERN.matcher("value=[@xyz.me4cxy.proxy.annotation.HttpParam(paramType=PARAMETER, defaultVal=, index=0, name=id)], key=123");
//        while (kv.find()) {
//            System.out.println(kv.group("name"));
//            System.out.println(kv.group("value"));
//            System.out.println("...");
//        }

//        final Matcher matcher = Pattern.compile("(?<annotation>(@[\\w\\.]+)\\(((\\w+)=(\\[[\\s\\S]*])|[^,](, )?)*?\\))").matcher("[@xyz.me4cxy.proxy.annotation.HttpParam(paramType=PARAMETER, defaultVal=, index=0, name=id), @xyz.me4cxy.proxy.annotation.HttpParam(paramType=PARAMETER, defaultVal=, index=0, name=id), @xyz.me4cxy.proxy.annotation.HttpParam(paramType=PARAMETER, defaultVal=, index=0, name=id)]");
//        while (matcher.find()) {
//            System.out.println(matcher.group("annotation"));
//            System.out.println("...");
//        }
        System.out.println(resolve(Arrays.asList(
                "@xyz.me4cxy.proxy.annotation.HttpMethod(method=[GET, POST, PUT, DELETE], alias=[])",
                "@xyz.me4cxy.proxy.annotation.HttpParams(value=[@xyz.me4cxy.proxy.annotation.HttpParam(paramType=PARAMETER, defaultVal=, index=0, name=id)])",
                "@xyz.me4cxy.proxy.annotation.HttpParam(paramType=PARAMETER, defaultVal=, index=1, name=user)")
        ));
    }
}
