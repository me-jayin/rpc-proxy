package xyz.me4cxy.proxy.support.context;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * 代理响应对象
 *
 * @author jayin
 * @since 2024/01/06
 */
public interface ProxyResponse {

    /**
     * 添加附加参数
     * @param name
     * @param value
     */
    void addAdditional(String name, String value);

    /**
     * 添加附加参数
     * @param addis
     */
    void addAdditional(Map<String, List<String>> addis);

    /**
     * 获取输出流
     * @return
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * 输出
     * @return
     */
    PrintWriter getWriter() throws IOException;
}
