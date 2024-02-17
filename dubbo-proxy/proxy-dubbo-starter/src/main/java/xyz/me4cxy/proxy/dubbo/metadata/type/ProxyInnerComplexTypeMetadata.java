package xyz.me4cxy.proxy.dubbo.metadata.type;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import xyz.me4cxy.proxy.dubbo.asm.Coder;
import xyz.me4cxy.proxy.dubbo.config.TypeConfig;
import xyz.me4cxy.proxy.dubbo.metadata.ClassType;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;
import xyz.me4cxy.proxy.exception.ProxyIllegalArgumentException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 内置类型定义，此类型需要通过继承的方式来进行创建类
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
public class ProxyInnerComplexTypeMetadata extends ProxyTypeMetadata {
    /**
     * 内部class
     */
    protected final Class innerClass;
    /**
     * 是否有泛型
     */
    @Getter
    protected final boolean havGenerics;
    /**
     * 泛型列表
     */
    @Getter
    protected List<ProxyTypeMetadata> generics;
    /**
     * 全类名
     */
    protected String canonicalName;

    public ProxyInnerComplexTypeMetadata(String applicationPrefix, TypeDefinitionWrapper definition, Class innerClass) {
        this(applicationPrefix, ClassType.INNER_OBJECT, definition, innerClass);
    }

    protected ProxyInnerComplexTypeMetadata(String applicationPrefix, ClassType type, TypeDefinitionWrapper definition, Class innerClass) {
        super(applicationPrefix, type, definition);
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

    @Override
    public String getCanonicalName() {
        if (canonicalName != null) {
            return canonicalName;
        }

        if (isFinalClass()) { // 不可继承时，当前类即为最终类
            canonicalName = innerClass.getCanonicalName();
        } else {
            canonicalName = Coder.getInstance(super.applicationPrefix).generateClassName(super.definition.getType());
        }
        return canonicalName;
    }

    /**
     * 判断当前类是否是 final 不可继承类
     * @return
     */
    public boolean isFinalClass() {
        return TypeConfig.isFinalType(innerClass);
    }

    /**
     * 获取内部类对象，也就是其父类
     * @return
     */
    public Class innerClass() {
        return innerClass;
    }

    @Override
    public Class getTypeClass() {
        if (isFinalClass()) { // 如果是不可变内部类直接返回
            return innerClass;
        }
        try {
            return Coder.getInstance(applicationPrefix).loadClass(getCanonicalName());
        } catch (ClassNotFoundException e) {
            throw new ProxyIllegalArgumentException("参数类型解析失败", "参数类型[" + getCanonicalName() + "]未找到", e);
        }
    }
}
