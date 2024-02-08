package xyz.me4cxy.proxy.dubbo.metadata.definition;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.metadata.definition.model.TypeDefinition;

import java.util.List;
import java.util.Map;

/**
 * 类型定义包装类
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
public class TypeDefinitionWrapper {
    private final TypeDefinition definition;
    private final boolean isInnerType;

    public TypeDefinitionWrapper(TypeDefinition definition) {
        this(definition, false);
    }

    public TypeDefinitionWrapper(TypeDefinition definition, boolean isInnerType) {
        this.definition = definition;
        this.isInnerType = isInnerType;
    }

    public List<String> getEnums() {
        return definition.getEnums();
    }

    public List<String> getItems() {
        return definition.getItems();
    }

    public Map<String, String> getProperties() {
        return definition.getProperties();
    }

    /**
     * 剔除掉泛型后的原始类型
     * @return
     */
    public String getType() {
        return StringUtils.substringBefore(definition.getType(), "<");
    }

    /**
     * 类型定义中的原类型
     * @return
     */
    public String getRawType() {
        return definition.getType();
    }

    public boolean isInnerType() {
        return isInnerType;
    }

    /**
     * 基于指定类型创建类型定义
     * @param type
     * @return
     */
    public static TypeDefinitionWrapper innerOfType(String type) {
        TypeDefinition definition = new TypeDefinition();
        definition.setType(type);
        return new TypeDefinitionWrapper(definition, true);
    }
}
