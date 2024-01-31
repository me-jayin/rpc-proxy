package xyz.me4cxy.proxy.core.metadata.service;

import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.core.metadata.ProxyMetadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存代理元数据业务类
 *
 * @author jayin
 * @since 2024/01/30
 */
public abstract class CacheableProxyMetadataService implements ProxyMetadataService {
    /**
     * 元数据缓存
     */
    private final Map<String, ProxyMetadata> metadataCache = new ConcurrentHashMap<>();

    @Override
    public ProxyMetadata loadMetadata(ProxyIdentify identify) {
        return metadataCache.computeIfAbsent(identify.identityKey(), key -> loadMetadata0(identify));
    }

    /**
     * 实际加载元数据
     * @param identify
     * @return
     */
    protected abstract ProxyMetadata loadMetadata0(ProxyIdentify identify);

}
