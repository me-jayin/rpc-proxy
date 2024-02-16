package xyz.me4cxy.proxy.dubbo.invoker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.invoker.Invoker;
import xyz.me4cxy.proxy.core.invoker.InvokerFactory;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.core.Cleaner;
import xyz.me4cxy.proxy.dubbo.core.CleanerRegistry;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;
import xyz.me4cxy.proxy.dubbo.metadata.service.ProxyServiceMetadataService;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    private AutowireCapableBeanFactory autowireCapableBeanFactory;
    @Resource
    private ProxyServiceMetadataService metadataService;
    /**
     * invoker缓存
     */
    private final Map<String, ServiceMethodInvoker> INVOKER_CACHES = new ConcurrentHashMap<>();
    /**
     * 记录某个应用下有哪些调用器
     */
    private final Map<String, Set<String>> APPLICATION_TO_IDENTIFY_KEYS = new ConcurrentHashMap<>();

    @Override
    public Invoker<DubboProxyIdentify> createInvoker(DubboProxyIdentify identify) {
        return getOrCreateInvoker(identify);
    }

    @Override
    public boolean isSupport(ProxyRequestContext context, ProxyIdentify identify) {
        return identify instanceof DubboProxyIdentify;
    }

    /**
     * dubbo是基于服务来的，每次获取后，同一个服务即可进行缓存，后面该服务其他方法直接从缓存中获取即可
     * @param identify
     * @return
     */
    protected ServiceMethodInvoker getOrCreateInvoker(DubboProxyIdentify identify) {
        return INVOKER_CACHES.computeIfAbsent(identify.identifyKey(), key -> {
            APPLICATION_TO_IDENTIFY_KEYS.computeIfAbsent(identify.applicationIdentify(), k -> new HashSet<>()).add(key);
            CleanerRegistry.addCleaner(identify.applicationIdentify(), new Cleaner() {
                @Override
                public void clear() {
                    Set<String> keys = APPLICATION_TO_IDENTIFY_KEYS.remove(identify.applicationIdentify());
                    if (keys != null) {
                        keys.forEach(INVOKER_CACHES::remove);
                    }
                }

                @Override
                public String name() {
                    return "ServiceMetadata Invoker 缓存清除器";
                }
            });
            return doCreate(identify);
        });
    }

    private ServiceMethodInvoker doCreate(DubboProxyIdentify identify) {
        log.debug("开始创建服务方法 {} 的元数据服务调用者", identify.identifyKey());
        // 获取代理服务定义元数据
        ProxyServiceMetadata metadata = metadataService.loadMetadata(identify);

        // 创建服务方法调用器，并使用spring填充属性
        ServiceMethodInvoker invoker = new ServiceMethodInvoker(metadata, identify);
        autowireCapableBeanFactory.autowireBean(invoker);
        autowireCapableBeanFactory.initializeBean(invoker, identify.identifyKey());
        return invoker;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
    }
}
