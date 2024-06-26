package xyz.me4cxy.proxy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 路径匹配工具类
 *
 * @author jayin
 * @since 2024/01/14
 */
@Slf4j
public class PathPatternUtils {
    /** 匹配路径单块片段 */
    public final static String SINGLE_SEGMENT = "[a-zA-Z0-9\\-_]+";
    /** 匹配路径多块片段 */
    public final static String MULTIPLE_SEGMENT = "[a-zA-Z0-9/\\-_]+";
    /** 不支持的类路径模板 */
    public final static String CLASS_PATH_NOT_SUPPORT_PATTERN = "[^0-9a-zA-Z._]";
    /** Pattern 缓存 */
    private final static Map<String, Pattern> PATTERN_CACHE = new ConcurrentHashMap<>();
    /** 路径匹配结果缓存 */
    private final static Map<String, Map<String, String>> MATCH_RESULT_CACHE = new ConcurrentHashMap<>();

    public static final String PATH_GROUP_APPLICATION = "application";
    public static final String PATH_GROUP_PACKAGE = "package";
    public static final String PATH_GROUP_CLASS_NAME = "className";
    public static final String PATH_GROUP_METHOD = "method";

    /** 分组名称 */
    public final static List<String> IDENTIFY_PARAM_NAMES = Arrays.asList(
            PATH_GROUP_APPLICATION,
            PATH_GROUP_PACKAGE,
            PATH_GROUP_CLASS_NAME,
            PATH_GROUP_METHOD
    );

    public static Pattern getPathPattern(String pathRuleRegex) {
        return PATTERN_CACHE.computeIfAbsent(pathRuleRegex, key -> {
            String regex = StringUtils.replace(pathRuleRegex, "${mseg}", MULTIPLE_SEGMENT);
            regex = StringUtils.replace(regex, "${seg}", SINGLE_SEGMENT);
            return Pattern.compile(regex);
        });
    }

    /**
     * 将匹配组转为map
     * @param pathRuleRegex
     * @param path
     * @return unmodifiableMap
     */
    public static Map<String, String> matchGroupToMap(String pathRuleRegex, String path) {
        return MATCH_RESULT_CACHE.computeIfAbsent(path, key -> {
            Pattern pattern = getPathPattern(pathRuleRegex);
            Matcher matcher = pattern.matcher(path);
            if (!matcher.matches()) {
                return null;
            }
            return Collections.unmodifiableMap(IDENTIFY_PARAM_NAMES.stream().collect(Collectors.toMap(Function.identity(), matcher::group)));
        });
    }

    /**
     * 将类路径不支持的字符，替换成指定字符
     * 类路径支持的字符：0-9a-zA-Z._
     * @param str
     * @param replace
     * @return
     */
    public static String replaceClassPathNotSupportChar(String str, String replace) {
        return RegExUtils.replaceAll(str, CLASS_PATH_NOT_SUPPORT_PATTERN, replace);
    }

    public static void main(String[] args) {
        Pattern pattern = getPathPattern("/(?<application>${seg})/(?<package>${mseg})/(?<className>${seg})/(?<method>${seg})");
        Matcher matcher = pattern.matcher("/mayflower-scrm/api/rpc/kyc-bind/list-by-cust-num");
        System.out.println(matcher.matches());
////        if (matcher.find()) {
//            System.out.println(matcher.group("application"));
//            System.out.println(matcher.group("package"));
//            System.out.println(matcher.group("className"));
//            System.out.println(matcher.group("method"));
////        }
        System.out.println(matchGroupToMap(
                "/(?<application>${seg})/(?<package>${mseg})/(?<className>${seg})/(?<method>${seg})",
                "/mayflower-scrm/api/rpc/kyc-bind/list-by-cust-num"
        ));
    }

}
