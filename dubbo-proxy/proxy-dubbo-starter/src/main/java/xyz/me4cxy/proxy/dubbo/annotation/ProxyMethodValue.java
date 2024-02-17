package xyz.me4cxy.proxy.dubbo.annotation;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 代理注解方法值
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/07
 */
@Getter
public class ProxyMethodValue {
    /**
     * 别名
     */
    private final Set<String> alias = new HashSet<>();
    /**
     * 方法
     */
    private final Set<String> methods = new HashSet<>();
}
