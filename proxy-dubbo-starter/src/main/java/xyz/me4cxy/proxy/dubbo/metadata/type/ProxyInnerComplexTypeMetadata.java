package xyz.me4cxy.proxy.dubbo.metadata.type;

import org.apache.commons.collections4.CollectionUtils;
import xyz.me4cxy.proxy.dubbo.metadata.ClassType;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 内置类型定义
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
public class ProxyInnerComplexTypeMetadata extends ProxyTypeMetadata {
    /**
     * 内部class
     */
    private final Class innerClass;
    /**
     * 是否有泛型
     */
    private final boolean havGenerics;
    /**
     * 泛型列表
     */
    private List<ProxyTypeMetadata> generics;

    public ProxyInnerComplexTypeMetadata(TypeDefinitionWrapper definition, Class innerClass) {
        this(ClassType.INNER_OBJECT, definition, innerClass);
    }

    protected ProxyInnerComplexTypeMetadata(ClassType type, TypeDefinitionWrapper definition, Class innerClass) {
        super(type, definition);
        this.innerClass = innerClass;
        this.havGenerics = CollectionUtils.isNotEmpty(definition.getItems());
    }

    @Override
    public void afterRegister(Function<String, ProxyTypeMetadata> fieldTypeLoader) {
        super.afterRegister(fieldTypeLoader);
        // 初始化泛型类型
        List<String> items = getDefinition().getItems();
        generics = items.stream().map(fieldTypeLoader).collect(Collectors.toList());
    }
}
