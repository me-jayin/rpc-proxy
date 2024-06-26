package xyz.me4cxy.proxy.dubbo.metadata.service;

import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;
import xyz.me4cxy.proxy.exception.ServiceNotFoundException;

/**
 * 代理元数据业务类
 * @author jayin
 * @since 2024/01/28
 */
public interface ProxyServiceMetadataService {

    /**
     * 加载元数据
     * @param identify
     * @return NotNull，如果为空请抛出{@link ServiceNotFoundException}异常
     */
    ProxyServiceMetadata loadMetadata(ProxyIdentify identify) throws ServiceNotFoundException;

}
