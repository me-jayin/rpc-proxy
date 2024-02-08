package xyz.me4cxy.proxy.dubbo.metadata;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;
import xyz.me4cxy.proxy.dubbo.metadata.type.ProxyTypeMetadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局类型定义注册器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/05
 */
@Slf4j
public class GlobalTypeRegister {
    private static final Map<String, Map<String, ProxyTypeMetadata>> APPLICATION_TYPES = new ConcurrentHashMap<>();

    public static void registerAll(String applicationIdentify, Map<String, TypeDefinitionWrapper> typeToDefinition) {
        // 应用级锁，防止并发类型注册出问题
        synchronized (applicationIdentify.intern()) {
            for (TypeDefinitionWrapper definition : typeToDefinition.values()) {
                register(applicationIdentify, definition, typeToDefinition);
            }
        }
    }

    /**
     * 注册单个类型定义
     * @param applicationIdentify 应用标识
     * @param definition 类型
     * @param typeSources 如果类型不存在时，才该
     */
    public static ProxyTypeMetadata register(String applicationIdentify, TypeDefinitionWrapper definition, Map<String, TypeDefinitionWrapper> typeSources) {
        Map<String, ProxyTypeMetadata> applicationTypes = getApplicationTypes(applicationIdentify);
        String rawType = definition.getRawType();
        ProxyTypeMetadata cacheType = applicationTypes.get(rawType);
        if (cacheType == null) {
            synchronized (rawType.intern()) {
                cacheType = applicationTypes.get(rawType);
                if (cacheType == null) {
                    cacheType = registerType(applicationIdentify, definition, applicationTypes, typeSources);
                    applicationTypes.put(rawType, cacheType);
                }
            }
        }
        // TODO 校验类型与当前类型定义的描述是否一致

        log.debug("应用 {} 类型 {} 注册到全局成功", applicationIdentify, rawType);
        return cacheType;
    }

    /**
     * 注册类型到指定应用类型集中
     *
     * @param applicationIdentify 应用标识
     * @param type 类型定义
     * @param applicationTypes 应用标识
     * @param typeSources 原类型定义列表
     * @return
     */
    private static ProxyTypeMetadata registerType(String applicationIdentify,
                                                  TypeDefinitionWrapper type,
                                                  Map<String, ProxyTypeMetadata> applicationTypes,
                                                  Map<String, TypeDefinitionWrapper> typeSources) {
        log.debug("开始注册应用 {} 的类型 {}", applicationIdentify, type.getRawType());
        // 基本类型则直接返回基本描述
        if (ArrayUtils.contains(ClassType.BASIC_TYPES, type.getRawType())) {
            ProxyTypeMetadata metadata = ProxyTypeMetadata.ofBasicType(type);
            applicationTypes.put(type.getRawType(), metadata);
            log.info("应用 {} 中类型 {} 元数据初始化完成，其类型为基本类型", applicationIdentify, type.getRawType());
            return metadata;
        }
        // 非基本类型当作复杂类型处理
        return registerComplexType(applicationIdentify, type, applicationTypes, typeSources);
    }

    /**
     * 注册复杂类型。
     * 由于类型可能存在循环嵌套，因此，在复合类型中，会先创建元数据对象并注册上去，然后才开始初始化填充对象信息
     * @param applicationIdentify
     * @param type
     * @param applicationTypes
     * @param typeSources
     * @return
     */
    private static ProxyTypeMetadata registerComplexType(String applicationIdentify,
                                                         TypeDefinitionWrapper type,
                                                         Map<String, ProxyTypeMetadata> applicationTypes,
                                                         Map<String, TypeDefinitionWrapper> typeSources) {
        ProxyTypeMetadata metadata = ProxyTypeMetadata.ofComplexType(type);
        applicationTypes.put(type.getRawType(), metadata);
        // 后置初始化
        metadata.afterRegister(fieldType -> {
            TypeDefinitionWrapper fieldTypeDefinition = typeSources.get(fieldType);
            if (fieldTypeDefinition == null) { // 如果是遇到类型丢失，则先尝试直接注册到映射中
                fieldTypeDefinition = TypeDefinitionWrapper.ofType(fieldType);
            }
            return registerType(applicationIdentify, fieldTypeDefinition, applicationTypes, typeSources);
        });
        log.info("应用 {} 中类型 {} 元数据初始化完成", applicationIdentify, type.getRawType());
        return metadata;
    }

    public static ProxyTypeMetadata getType(String applicationIdentify, String type) {
        return getApplicationTypes(applicationIdentify).get(type);
    }

    /**
     * 获取指定应用的类型列表
     * @param applicationIdentify
     * @return
     */
    private static Map<String, ProxyTypeMetadata> getApplicationTypes(String applicationIdentify) {
        return APPLICATION_TYPES.computeIfAbsent(
                applicationIdentify,
                key -> new ConcurrentHashMap<>()
        );
    }
}
