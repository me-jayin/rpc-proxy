package xyz.me4cxy.proxy.dubbo.metadata.type;

import lombok.Getter;
import xyz.me4cxy.proxy.dubbo.metadata.ClassType;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 基本类型元数据，直接返回对应的包装类
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
@Getter
public class ProxyBasicTypeMetadata extends ProxyTypeMetadata {
    /**
     * 基本类型映射列表
     */
    private static final Map<String, Class> BASIC_TYPE_MAPPING = new HashMap<String, Class>() {{
        this.put("boolean", Boolean.class);
        this.put("char", Character.class);
        this.put("byte", Byte.class);
        this.put("short", Short.class);
        this.put("int", Integer.class);
        this.put("float", Float.class);
        this.put("long", Long.class);
        this.put("double", Double.class);
        this.put("void", Void.class);
    }};
    /**
     * 包装类
     */
    private final Class wrapperClazz;

    public ProxyBasicTypeMetadata(String applicationIdentify, TypeDefinitionWrapper definition) {
        super(applicationIdentify, ClassType.BASIC, definition);
        wrapperClazz = BASIC_TYPE_MAPPING.get(getDefinition().getRawType());
    }

    /**
     * 获取实际类型
     * @return
     */
    @Override
    public String getCanonicalName() {
        return wrapperClazz.getCanonicalName();
    }

    @Override
    public Class getTypeClass() {
        return wrapperClazz;
    }

}
