package xyz.me4cxy.proxy.dubbo.metadata.type;

import lombok.Getter;
import xyz.me4cxy.proxy.dubbo.metadata.ClassType;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;

/**
 * 基本类型元数据
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
@Getter
public class ProxyBasicTypeMetadata extends ProxyTypeMetadata {

    public ProxyBasicTypeMetadata(TypeDefinitionWrapper definition) {
        super(ClassType.BASIC, definition);
    }

}
