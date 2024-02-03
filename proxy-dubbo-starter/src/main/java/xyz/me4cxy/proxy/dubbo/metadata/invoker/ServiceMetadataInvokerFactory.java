package xyz.me4cxy.proxy.dubbo.metadata.invoker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xyz.me4cxy.proxy.core.invoker.Invoker;
import xyz.me4cxy.proxy.core.invoker.InvokerFactory;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;
import xyz.me4cxy.proxy.dubbo.metadata.service.ProxyServiceMetadataMatcher;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * dubbo服务调用器工厂类
 * 基于元数据创建调用器
 *
 * @author jayin
 * @since 2024/02/03
 */
@Slf4j
public class ServiceMetadataInvokerFactory implements InvokerFactory<DubboProxyIdentify>, ApplicationContextAware {
    @Resource
    private ProxyServiceMetadataMatcher metadataService;
    private AutowireCapableBeanFactory autowireCapableBeanFactory;
    /**
     * invoker缓存
     */
    private final ConcurrentHashMap<String, ServiceMetadataInvoker> INVOKER_CACHES = new ConcurrentHashMap<>();

    @Override
    public Invoker<DubboProxyIdentify> createInvoker(DubboProxyIdentify identify) {
        return getOrCreateInvoker(identify);
    }

    protected ServiceMetadataInvoker getOrCreateInvoker(DubboProxyIdentify identify) {
        return INVOKER_CACHES.computeIfAbsent(identify.identityKey(), key -> doCreate(identify));
    }

    private ServiceMetadataInvoker doCreate(DubboProxyIdentify identify) {
        ProxyServiceMetadata metadata = metadataService.loadMetadata(identify);
        log.info("代理 {} 的元数据信息加载成功：{}", identify.identityKey(), metadata);
        // 创建并使用spring填充属性
        ServiceMetadataInvoker invoker = new ServiceMetadataInvoker(metadata);

        autowireCapableBeanFactory.autowireBean(invoker);
        autowireCapableBeanFactory.initializeBean(invoker, identify.identityKey());
        log.info("代理 {} 的调用器创建成功：{}", identify.identityKey(), invoker);
        return invoker;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
    }
}
