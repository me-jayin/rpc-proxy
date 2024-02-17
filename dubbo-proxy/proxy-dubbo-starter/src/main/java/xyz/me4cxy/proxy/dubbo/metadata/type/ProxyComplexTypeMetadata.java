package xyz.me4cxy.proxy.dubbo.metadata.type;

import lombok.Getter;
import xyz.me4cxy.proxy.dubbo.asm.Coder;
import xyz.me4cxy.proxy.dubbo.config.TypeConfig;
import xyz.me4cxy.proxy.dubbo.metadata.ClassType;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 外部复杂类型元数据，需要加载自定义的类加载器，然后根据类加载器进行对象类生成
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

    public ProxyComplexTypeMetadata(String applicationPrefix, TypeDefinitionWrapper definition) {
        super(applicationPrefix, ClassType.OBJECT, definition, null);
    }

    @Override
    public void afterRegister(Function<String, ProxyTypeMetadata> fieldTypeLoader) {
        super.afterRegister(fieldTypeLoader);
        // 初始化field字段
        this.fields = getDefinition().getProperties().entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey, v -> fieldTypeLoader.apply(v.getValue())));
    }

    /**
     * 判断当前类是否是 final 不可继承类
     * @return
     */
    public boolean isFinalClass() {
        return false;
    }
}
