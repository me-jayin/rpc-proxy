package xyz.me4cxy.proxy.dubbo.invoker.matcher;

import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodMetadata;

import java.util.List;

/**
 * 方法匹配器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/08
 */
public interface MethodMatcher {

    /**
     * 匹配找到方法
     * @param methods
     * @param context
     * @return
     */
    List<ProxyMethodMetadata> match(List<ProxyMethodMetadata> methods, ProxyRequestContext context);

}
