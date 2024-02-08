package xyz.me4cxy.proxy.dubbo.annotation;

import org.apache.commons.lang3.StringUtils;
import xyz.me4cxy.proxy.annotation.ProxyMethod;
import xyz.me4cxy.proxy.annotation.ProxyParam;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.exception.ProxyParamException;
import xyz.me4cxy.proxy.utils.MapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 代理注解工具类，用于获取代理属性等
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/07
 */
public class ProxyAnnotationUtils {
    /**
     * 代理方法别名字段名
     */
    public static final String PROXY_METHOD_ALIAS = "alias";
    /**
     * 代理方法方法字段名
     */
    public static final String PROXY_METHOD_METHOD = "method";


    /**
     * 获取支持的代理方法
     * @param annoAttrs
     * @return
     */
    public static ProxyMethodValue getProxyMethodsInfo(String applicationIdentify, Map<String, List<Map<String, String>>> annoAttrs) {
        List<Map<String, String>> infos = getAnnotationAttributes(annoAttrs, ProxyMethod.class);
        ProxyMethodValue value = new ProxyMethodValue();
        for (Map<String, String> info : infos) {
            value.getAlias().addAll(valueToList(info.get(PROXY_METHOD_ALIAS), true));
            value.getMethods().addAll(valueToList(info.get(PROXY_METHOD_METHOD), true));
        }
        return value;
    }

    /**
     * 获取代理参数信息
     * @param annoAttrs
     * @return
     */
    public static List<ProxyParamValue> getProxyParamsInfo(String applicationIdentify, Map<String, List<Map<String, String>>> annoAttrs) {
        List<Map<String, String>> infos = getAnnotationAttributes(annoAttrs, ProxyParam.class);
        List<ProxyParamValue> paramsInfo = infos.stream().map(map -> {
            String index = MapUtils.getIfBlank(map, "index", key -> {
                throw new ProxyParamException(applicationIdentify, "参数下标[index]为空");
            });
            String name = MapUtils.getIfBlank(map, "name", key -> {
                throw new ProxyParamException(applicationIdentify, "参数名称[name]为空");
            });
            String paramType = MapUtils.getIfBlank(map, "paramType", key -> {
                throw new ProxyParamException(applicationIdentify, "参数类型[paramType]为空");
            });
            return ProxyParamValue.builder()
                    .service(applicationIdentify)
                    .index(Integer.parseInt(index))
                    .name(name)
                    .paramType(ProxyParamType.valueOf(paramType)).build();
        }).sorted(Comparator.comparing(ProxyParamValue::getIndex)) // 按索引排序
                .collect(Collectors.toList());

        int index = -1;
        for (ProxyParamValue value : paramsInfo) {
            if (index == value.getIndex() - 1) {
                index = value.getIndex();
                continue;
            }
            throw new ProxyParamException(applicationIdentify, "参数下标[index]不连贯，缺少第" + value.getIndex() + "参数信息");
        }

        return paramsInfo;
    }

    /**
     * 将值转集合
     * @param values
     * @param trim
     * @return
     */
    private static Collection<String> valueToList(String values, boolean trim) {
        if (StringUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        if (StringUtils.contains(values, "[")) {
            values = StringUtils.substring(values, 1, values.length() - 1);
        }
        return Arrays.stream(StringUtils.split(values, ","))
                .map(v -> trim ? StringUtils.trim(v) : v)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定注解属性列表
     * @param annoAttrs
     * @param clazz
     * @return
     */
    private static List<Map<String, String>> getAnnotationAttributes(Map<String, List<Map<String, String>>> annoAttrs, Class clazz) {
        return Optional.ofNullable(annoAttrs.get(getAnnotationName(clazz))).orElse(new ArrayList<>());
    }

    private static String getAnnotationName(Class clazz) {
        return "@" + clazz.getCanonicalName();
    }
}
