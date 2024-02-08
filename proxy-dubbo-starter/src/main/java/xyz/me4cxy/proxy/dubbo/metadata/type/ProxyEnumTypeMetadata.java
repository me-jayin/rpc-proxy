package xyz.me4cxy.proxy.dubbo.metadata.type;

import lombok.Getter;
import xyz.me4cxy.proxy.dubbo.metadata.ClassType;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;

import java.util.HashSet;
import java.util.Set;

/**
 * 枚举类型元数据
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
@Getter
public class ProxyEnumTypeMetadata extends ProxyTypeMetadata {
    /**
     * 枚举值列表
     */
    private final Set<String> enumValues;

    public ProxyEnumTypeMetadata(TypeDefinitionWrapper definition) {
        super(ClassType.ENUM, definition);
        this.enumValues = new HashSet<>(definition.getEnums());
    }

}
