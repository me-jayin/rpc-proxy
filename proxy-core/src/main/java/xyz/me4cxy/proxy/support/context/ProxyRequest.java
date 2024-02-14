package xyz.me4cxy.proxy.support.context;

import java.util.List;
import java.util.Map;

/**
 * 代理请求对象
 *
 * @author jayin
 * @since 2024/01/06
 */
public interface ProxyRequest {

    /**
     * 获取参数
     * @param name
     * @return
     */
    String getParameter(String name);

    /**
     * 获取参数
     * @param name
     * @return
     */
    List<String> getParameters(String name);

    /**
     * 获取所有参数
     * @return
     */
    Map<String, List<String>> getParameters();

    /**
     * 获取默认参数
     * @param name
     * @return
     */
    default String getParameterIfNull(String name, String ifNull) {
        String parameter = getParameter(name);
        return parameter == null ? ifNull : name;
    }

    /**
     * 获取附加参数
     * @param key
     * @return
     */
    String getAdditional(String key);

    /**
     *
     * @param name
     * @param ifNull
     * @return
     */
    default String getAdditionalIfNull(String name, String ifNull) {
        String addi = getAdditional(name);
        return addi == null ? ifNull : addi;
    }

    /**
     * 获取附加参数集合
     * @param key
     * @return
     */
    List<String> getMultiAdditional(String key);

    /**
     * 获取附加参数列表
     * @return
     */
    Map<String, List<String>> getAdditional();

    /**
     * 附加参数名称列表
     * @return
     */
    List<String> getAdditionalNames();

    /**
     * 获取代理的路径
     * @return
     */
    String getPath();

    /**
     * 获取客户端ip
     * @return
     */
    String getIp();

    /**
     * 请求方式
     * @return
     */
    String requestMethod();

    /**
     * 获取body内容
     * @return
     */
    String getBody();

}
