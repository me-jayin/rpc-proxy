package xyz.me4cxy.proxy.dubbo.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import xyz.me4cxy.proxy.exception.ProxyException;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 类型定义
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/09
 */
@Slf4j
public class TypeConfig {
    private static final Map<String, Class> supperTypeMapping;
    private static final Set<Class> finalTypes;

    static {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] finalTypeResource = resolver.getResources("classpath*:/final-type.list");
            finalTypes = initFinalType(finalTypeResource);

            Resource[] superTypeResource = resolver.getResources("classpath*:/super-type.properties");
            supperTypeMapping = initSuperType(superTypeResource);
        } catch (Exception e) {
            log.error("加载配置失败", e);
            throw new ProxyException("类型配置类未找到", e);
        }
    }

    private static Set<Class> initFinalType(Resource[] resources) throws IOException, ClassNotFoundException {
        Set<Class> finalType = new HashSet<>();
        for (Resource resource : resources) {
            String[] lines = IOUtils.readLines(resource.getInputStream());
            for (String line : lines) {
                line = StringUtils.trim(line);
                if (StringUtils.isNotEmpty(line) && !StringUtils.startsWith(line, "#"))
                    finalType.add(Class.forName(line));
            }
        }
        log.debug("不可继承类：{}", finalType);
        return finalType;
    }

    private static Map<String, Class> initSuperType(Resource[] resources) throws IOException, ClassNotFoundException {
        Properties props;
        Map<String, Class> types = new HashMap<>();
        for (Resource resource : resources) {
            props = new Properties();
            props.load(resource.getInputStream());
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                Class superType = Class.forName((String) entry.getValue());
                types.put((String) entry.getKey(), superType);
            }
        }

        log.debug("父类映射：{}", types);
        return types;
    }

    /**
     * 是否是不可继承类
     * @param clazz
     * @return
     */
    public static boolean isFinalType(Class clazz) {
        return finalTypes.contains(clazz);
    }

    /**
     * 获取父类
     * @param clazz
     * @return 如果需要切换父类时，则返回对应父类，否则返回传入类
     */
    public static Class getSuperClass(Class clazz) {
        if (clazz == null) {
            return Object.class;
        }

        Class superClazz = supperTypeMapping.get(clazz.getName());
        return superClazz == null ? clazz : superClazz;
    }
}
