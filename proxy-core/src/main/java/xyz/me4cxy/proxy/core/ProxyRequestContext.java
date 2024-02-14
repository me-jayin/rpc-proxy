package xyz.me4cxy.proxy.core;

import lombok.Builder;
import lombok.Getter;
import xyz.me4cxy.proxy.support.context.ProxyRequest;
import xyz.me4cxy.proxy.support.context.ProxyResponse;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 代理请求上下文
 *
 * @author jayin
 * @since 2024/01/06
 */
@Builder
@Getter
public class ProxyRequestContext {

    /**
     * 原请求对象
     */
    private ProxyRequest request;
    /**
     * 请求响应
     */
    private ProxyResponse response;
//    /**
//     * 请求参数
//     */
//    private LinkedHashMap<String, String> param;
//    /**
//     * 请求体
//     */
//    private String body;

    @Override
    public String toString() {
        return "context:{request: " + request.getPath() + "}";
    }
}
