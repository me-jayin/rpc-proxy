package xyz.me4cxy.proxy.dubbo.exception;

import xyz.me4cxy.proxy.exception.ProxyException;

/**
 * 类型定义不存在
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
public class TypeDefinitionNotFoundException extends ProxyException {

    /**
     * @param applicationIdentifyKey 应用标识
     * @param type 找不到的类型
     */
    public TypeDefinitionNotFoundException(String applicationIdentifyKey, String type) {
        super("类型定义未找到：" + type, "应用[" + applicationIdentifyKey + "]的类行定义未找到：" + type);
    }

}
