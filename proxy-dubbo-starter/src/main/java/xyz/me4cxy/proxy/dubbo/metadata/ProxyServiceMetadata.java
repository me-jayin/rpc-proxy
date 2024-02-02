package xyz.me4cxy.proxy.dubbo.metadata;

import java.io.Serializable;
import java.util.Map;

/**
 * 代理元数据
 *
 * @author jayin
 * @since 2024/01/30
 */
public interface ProxyServiceMetadata extends Serializable {

    /**
     * 获取所有方法信息
     * @return
     */
    Map<String, ProxyMethodMetadata> methods();

}
