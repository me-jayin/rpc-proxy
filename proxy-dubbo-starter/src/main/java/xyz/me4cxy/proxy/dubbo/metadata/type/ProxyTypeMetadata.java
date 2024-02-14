package xyz.me4cxy.proxy.dubbo.metadata.type;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import xyz.me4cxy.proxy.dubbo.exception.TypeDefinitionNotFoundException;
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
     *
     */
    protected final String applicationPrefix;
    /**
     * 原类型定义
     */
    protected final TypeDefinitionWrapper definition;
    /**
     * 类类型
     */
    protected final ClassType catalog;
    /**
     * 类型原名称
     */
    protected final String canonicalName;

    public ProxyTypeMetadata(String applicationPrefix, ClassType catalog, TypeDefinitionWrapper definition) {
        this.applicationPrefix = applicationPrefix;
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
    public static ProxyTypeMetadata ofBasicType(String applicationIdentify, TypeDefinitionWrapper definition) {
        return new ProxyBasicTypeMetadata(applicationIdentify, definition);
    }

    /**
     * 构建复杂类型
     *
     * @param applicationPrefix
     * @param definition
     * @return
     */
    public static ProxyTypeMetadata ofComplexType(String applicationPrefix, TypeDefinitionWrapper definition) {
        try {
            Class<?> clazz = Class.forName(definition.getType());
            return new ProxyInnerComplexTypeMetadata(applicationPrefix, definition, clazz);
        } catch (Exception e) {
            if (definition.isInnerType()) {
                throw new TypeDefinitionNotFoundException(applicationPrefix, definition.getType());
            }
            log.debug("类型 {} 非内置对象", definition.getType());
        }

        if (CollectionUtils.isNotEmpty(definition.getEnums())) { // 如果是枚举值
            return new ProxyEnumTypeMetadata(applicationPrefix, definition);
        }
        return new ProxyComplexTypeMetadata(applicationPrefix, definition);
    }

    public String getApplicationPrefix() {
        return applicationPrefix;
    }

    /**
     * 获取asm中类描述
     * @return
     */
    public abstract String getCanonicalName();

    /**
     * 获取当前类
     * @return
     */
    public abstract Class getTypeClass();


}
