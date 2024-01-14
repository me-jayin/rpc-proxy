package xyz.me4cxy.proxy.core.response.wrapper;

import xyz.me4cxy.proxy.core.response.wrapper.model.ApiResponse;

/**
 * 默认响应包装
 *
 * @author jayin
 * @since 2024/01/06
 */
public class DefaultResponseWrapper implements ResponseWrapper {
    @Override
    public Object wrap(Object response) {
        return ApiResponse.success(response);
    }
}
