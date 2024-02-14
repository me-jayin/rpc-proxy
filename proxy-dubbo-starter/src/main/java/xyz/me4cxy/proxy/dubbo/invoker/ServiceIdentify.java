package xyz.me4cxy.proxy.dubbo.invoker;

import lombok.Data;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;

/**
 * 服务信息
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/14
 */
@Data
public class ServiceIdentify {

    /**
     * 应用标识
     */
    private final String applicationIdentify;
    /**
     * 服务名称
     */
    private final String service;
    /**
     * 分组
     */
    private final String group;
    /**
     * 版本
     */
    private final String version;

    public static ServiceIdentify of(DubboProxyIdentify identify) {
        return new ServiceIdentify(
                identify.applicationIdentify(), identify.getService(),
                identify.getGroup(), identify.getVersion()
        );
    }

}
