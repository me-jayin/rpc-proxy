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
    private TypeDefinition definition;

    public TypeDefinitionWrapper(TypeDefinition definition) {
        this.definition = definition;
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

    /**
     * 基于指定类型创建类型定义
     * @param type
     * @return
     */
    public static TypeDefinitionWrapper ofType(String type) {
        TypeDefinition definition = new TypeDefinition();
        definition.setType(type);
        return new TypeDefinitionWrapper(definition);
    }
}
