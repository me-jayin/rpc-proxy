package xyz.me4cxy.proxy.web.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.Accessors;
import xyz.me4cxy.proxy.support.context.ProxyRequest;
import xyz.me4cxy.proxy.utils.ListUtils;
import xyz.me4cxy.proxy.utils.StreamUtils;
import xyz.me4cxy.proxy.web.utils.IpUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于http servlet的代理请求
 *
 * @author jayin
 * @since 2024/01/07
 */
@Builder
@AllArgsConstructor
public class ProxyHttpServletRequest implements ProxyRequest {
    private String body;
    private final HttpServletRequest request;
    private Map<String, List<String>> params;
    private Map<String, List<String>> additional;

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
    public Map<String, List<String>> getParameters() {
        if (params == null) {
            Map<String, String[]> map = request.getParameterMap();
            params = map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> Arrays.asList(entry.getValue())));
        }
        return params;
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
        if (additional == null) {
            List<String> names = getAdditionalNames();
            additional = StreamUtils.toMap(names, Function.identity(), this::getMultiAdditional);
        }
        return additional;
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

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String getBody() {
        return body;
    }

    public static ProxyHttpServletRequest of(HttpServletRequest request) {
        return new ProxyHttpServletRequest(request);
    }
}
