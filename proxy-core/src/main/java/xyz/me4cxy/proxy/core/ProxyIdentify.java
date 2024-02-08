package xyz.me4cxy.proxy.core;

/**
 * 代理标识，用于决定调用哪个服务
 *
 * @author jayin
 * @since 2024/01/07
 */
public interface ProxyIdentify {
    String IDENTIFY_SEPARATOR = ":";

    /**
     * 获取应用
     * @return
     */
    String getApplication();

    /**
     * 标识key
     * @return
     */
    String identifyKey();

}
