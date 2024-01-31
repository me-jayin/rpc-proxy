package xyz.me4cxy.proxy.core;

import lombok.extern.slf4j.Slf4j;
import xyz.me4cxy.proxy.core.mapping.ProxyIdentifyMappingChain;
import xyz.me4cxy.proxy.core.metadata.ProxyMetadata;
import xyz.me4cxy.proxy.core.metadata.service.ProxyMetadataService;
import xyz.me4cxy.proxy.exception.ProxyException;

/**
 * rpc代理调用
 *
 * @author jayin
 * @since 2024/01/07
 */
@Slf4j
public class RpcProxy {
    private ProxyIdentifyMappingChain chain;
    private ProxyMetadataService proxyMetadataService;

    public Object proxy(ProxyRequestContext context) {
        ProxyIdentify identify = chain.getIdentify(context);
        if (identify == null) {
            log.info("当前获取代理标识时未找到有效节点，context: {}", context);
            throw new ProxyException("无效的服务", "请求" + context.toString() + "未找到对应的服务");
        }
        log.info("请求路径映射后取到代理信息：{}", identify);

        // 创建代理调用上下文
        ProxyMetadata metadata = proxyMetadataService.loadMetadata(identify);

        // 进行请求调用
        return null;
    }

}
