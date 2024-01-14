package xyz.me4cxy.proxy.core.response.wrapper;

/**
 * 响应包装器
 *
 * @author jayin
 * @since 2024/01/06
 */
public interface ResponseWrapper {

    /**
     * 包装响应结果
     * @param response
     * @return
     */
    Object wrap(Object response);

}
