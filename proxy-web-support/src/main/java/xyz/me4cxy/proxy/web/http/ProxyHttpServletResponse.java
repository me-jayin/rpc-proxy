package xyz.me4cxy.proxy.web.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import xyz.me4cxy.proxy.support.context.ProxyResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 基于http servlet的响应代理
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/15
 */
@Builder
@AllArgsConstructor
public class ProxyHttpServletResponse implements ProxyResponse {
    private final HttpServletResponse response;

    @Override
    public void addAdditional(String name, String value) {
        response.addHeader(name, value);
    }

    @Override
    public void addAdditional(Map<String, List<String>> addis) {
        for (Map.Entry<String, List<String>> entry : addis.entrySet()) {
            String key = entry.getKey();
            for (String value : entry.getValue()) {
                addAdditional(key, value);
            }
        }
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return response.getOutputStream();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return response.getWriter();
    }
}
