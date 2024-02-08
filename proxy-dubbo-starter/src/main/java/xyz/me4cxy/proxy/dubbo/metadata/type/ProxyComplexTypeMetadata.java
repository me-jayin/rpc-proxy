package xyz.me4cxy.proxy.dubbo.metadata.type;

import lombok.Getter;
import xyz.me4cxy.proxy.dubbo.metadata.ClassType;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 复杂类型元数据
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
@Getter
public class ProxyComplexTypeMetadata extends ProxyInnerComplexTypeMetadata {
    /**
     * 属性类型信息
     */
    private Map<String, ProxyTypeMetadata> fields;

    public ProxyComplexTypeMetadata(TypeDefinitionWrapper definition) {
        super(ClassType.OBJECT, definition, null);
    }

    @Override
    public void afterRegister(Function<String, ProxyTypeMetadata> fieldTypeLoader) {
        super.afterRegister(fieldTypeLoader);
        // 初始化field字段
        this.fields = getDefinition().getProperties().entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey, v -> fieldTypeLoader.apply(v.getValue())));
    }
}
