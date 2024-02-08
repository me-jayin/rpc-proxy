package xyz.me4cxy.proxy.dubbo.metadata.type;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import xyz.me4cxy.proxy.dubbo.metadata.ClassType;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;

import java.util.function.Function;

/**
 * 代理类型元数据
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/05
 */
@Slf4j
@Getter
public abstract class ProxyTypeMetadata {
    /**
     * 原类型定义
     */
    private final TypeDefinitionWrapper definition;
    /**
     * 类类型
     */
    private final ClassType catalog;

    /**
     * 类型原名称
     */
    private final String canonicalName;

    public ProxyTypeMetadata(ClassType catalog, TypeDefinitionWrapper definition) {
        this.catalog = catalog;
        this.definition = definition;
        this.canonicalName = definition.getType();
    }

    /**
     * 用于复合类型创建，并注册后，进行后续初始化
     * @param fieldTypeLoader 属性字段类型加载器，可加载属性类型
     */
    public void afterRegister(Function<String, ProxyTypeMetadata> fieldTypeLoader) {

    }

    /**
     * 构建基础类型描述
     * @param definition
     * @return
     */
    public static ProxyTypeMetadata ofBasicType(TypeDefinitionWrapper definition) {
        return new ProxyBasicTypeMetadata(definition);
    }

    /**
     * 构建复杂类型
     * @param definition
     * @return
     */
    public static ProxyTypeMetadata ofComplexType(TypeDefinitionWrapper definition) {
        try {
            Class<?> clazz = Class.forName(definition.getType());
            return new ProxyInnerComplexTypeMetadata(definition, clazz);
        } catch (Exception e) {
            log.debug("类型 {} 非内置对象", definition.getType());
        }

        if (CollectionUtils.isNotEmpty(definition.getEnums())) { // 如果是枚举值
            return new ProxyEnumTypeMetadata(definition);
        }
        return new ProxyComplexTypeMetadata(definition);
    }

}
