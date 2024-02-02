package xyz.me4cxy.proxy.dubbo.metadata.service;

import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;

/**
 * 代理元数据业务类
 * @author jayin
 * @since 2024/01/28
 */
public interface ProxyMetadataService {

    /**
     * 加载元数据
     * @param identify
     */
    ProxyServiceMetadata loadMetadata(ProxyIdentify identify);

}
