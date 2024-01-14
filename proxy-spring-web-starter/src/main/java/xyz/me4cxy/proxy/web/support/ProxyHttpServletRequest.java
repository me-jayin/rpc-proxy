package xyz.me4cxy.proxy.web.support;

import xyz.me4cxy.proxy.support.context.ProxyRequest;
import xyz.me4cxy.proxy.utils.ListUtils;
import xyz.me4cxy.proxy.utils.StreamUtils;
import xyz.me4cxy.proxy.web.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 基于http servlet的代理请求
 *
 * @author jayin
 * @since 2024/01/07
 */
public class ProxyHttpServletRequest implements ProxyRequest {
    private final HttpServletRequest request;

    public ProxyHttpServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    @Override
    public List<String> getParameters(String name) {
        return ListUtils.toList(request.getHeaders(name));
    }

    @Override
    public String getAdditional(String key) {
        return request.getHeader(key);
    }

    @Override
    public List<String> getMultiAdditional(String key) {
        return ListUtils.toList(request.getHeaders(key));
    }

    @Override
    public Map<String, List<String>> getAdditional() {
        List<String> names = getAdditionalNames();
        return StreamUtils.toMap(names, Function.identity(), this::getMultiAdditional);
    }

    @Override
    public List<String> getAdditionalNames() {
        return ListUtils.toList(request.getHeaderNames());
    }

    @Override
    public String getPath() {
        return request.getRequestURI();
    }

    @Override
    public String getIp() {
        return IpUtils.getIpAddr(request);
    }

    @Override
    public String requestMethod() {
        return request.getMethod();
    }

    public static ProxyHttpServletRequest of(HttpServletRequest request) {
        return new ProxyHttpServletRequest(request);
    }
}
