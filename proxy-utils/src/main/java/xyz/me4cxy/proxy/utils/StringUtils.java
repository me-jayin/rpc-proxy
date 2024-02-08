package xyz.me4cxy.proxy.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/08
 */
public class StringUtils {
    private static Pattern PATTERN = Pattern.compile("[-_][a-zA-Z]");

    public static String toCamel(String lower) {
        Matcher matcher = PATTERN.matcher(lower);
        StringBuffer sb = new StringBuffer(lower.length());
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(0).toUpperCase().substring(1));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(toCamel("get-CND-address"));
        System.out.println(toCamel("getAddress"));
    }

}
