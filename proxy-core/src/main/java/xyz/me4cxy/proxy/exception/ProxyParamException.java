package xyz.me4cxy.proxy.exception;

/**
 * 代理参数异常
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/07
 */
public class ProxyParamException extends ProxyException {

    public ProxyParamException(String service, String reason) {
        this("服务初始化失败", "服务”" + service + "“代理参数有误：" + reason, null);
    }

    public ProxyParamException(String service, String reason, Throwable t) {
        super("服务初始化失败", "服务”" + service + "“代理参数有误：" + reason, t);
    }

}
