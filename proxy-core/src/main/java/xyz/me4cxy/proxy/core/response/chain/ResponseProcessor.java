package xyz.me4cxy.proxy.core.response.chain;

import org.springframework.core.Ordered;

/**
 * 响应结果处理器
 *
 * @author jayin
 * @since 2024/01/06
 */
public interface ResponseProcessor extends Ordered {

    /**
     * 处理响应结果
     * @return
     */
    Object process(Object result);

    /**
     * 判断是否支持
     * @param v
     * @return
     */
    boolean isSupport(Object v);

    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE >> 2;
    }
}
