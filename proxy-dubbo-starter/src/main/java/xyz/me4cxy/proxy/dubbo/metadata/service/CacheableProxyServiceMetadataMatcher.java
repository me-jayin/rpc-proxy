package xyz.me4cxy.proxy.dubbo.metadata.service;

import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存代理元数据业务类
 *
 * @author jayin
 * @since 2024/01/30
 */
public abstract class CacheableProxyServiceMetadataMatcher implements ProxyServiceMetadataMatcher {
    /**
     * 元数据缓存
     */
    private final Map<String, ProxyServiceMetadata> metadataCache = new ConcurrentHashMap<>();

    @Override
    public ProxyServiceMetadata loadMetadata(ProxyIdentify identify) {
        return metadataCache.computeIfAbsent(identify.identityKey(), key -> loadMetadata0(identify));
    }

    /**
     * 实际加载元数据
     * @param identify
     * @return
     */
    protected abstract ProxyServiceMetadata loadMetadata0(ProxyIdentify identify);

}
