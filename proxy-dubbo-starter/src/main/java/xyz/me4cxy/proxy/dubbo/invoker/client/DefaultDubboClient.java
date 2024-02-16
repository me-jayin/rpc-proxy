package xyz.me4cxy.proxy.dubbo.invoker.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.service.GenericService;
import xyz.me4cxy.proxy.dubbo.core.Cleaner;
import xyz.me4cxy.proxy.dubbo.core.CleanerRegistry;
import xyz.me4cxy.proxy.dubbo.invoker.ServiceIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodMetadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认dubbo客户端
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/14
 */
@Slf4j
public class DefaultDubboClient implements DubboClient {
    private final Map<String, Map<String, ReferenceConfig<GenericService>>> clientCaches = new ConcurrentHashMap<>();


    @Override
    public Object invoke(ServiceIdentify serviceIdentify, ProxyMethodMetadata method, Object[] args) {
        GenericService svc = getOrCreateReference(serviceIdentify).get();

        long t = System.currentTimeMillis();
        log.info(
                "开始进行dubbo调用，服务 {}，版本 {}，分组 {}。方法入参 {}",
                method, serviceIdentify.getVersion(), serviceIdentify.getGroup(), args
        );
        try {
            Object obj = svc.$invoke(method.getName(), method.getParamTypes(), args);
            log.info("dubbo服务 {} 调用完成，共耗时{}ms", method, System.currentTimeMillis() - t);
            return obj;
        } catch (Exception e) {
            log.info("dubbo服务 {} 调用失败，共耗时{}ms", method, System.currentTimeMillis() - t);
            throw e;
        }
    }

    private ReferenceConfig<GenericService> getOrCreateReference(ServiceIdentify identify) {
        String applicationIdentify = identify.getApplicationIdentify();
        Map<String, ReferenceConfig<GenericService>> referenceCaches = clientCaches.computeIfAbsent(applicationIdentify, k -> {
            CleanerRegistry.addCleaner(applicationIdentify, new Cleaner() {
                @Override
                public void clear() {
                    Map<String, ReferenceConfig<GenericService>> configs = clientCaches.remove(applicationIdentify);
                    try {
                        configs.values().forEach(ReferenceConfig::destroy);
                    } catch (Exception ignored) { }
                }

                @Override
                public String name() {
                    return "Dubbo Client 清除器";
                }
            });
            return new ConcurrentHashMap<>();
        });

        String referenceKey = identify.getService();
        ReferenceConfig<GenericService> config = referenceCaches.get(referenceKey);
        if (config == null) {
            config = createReference(identify.getService(), identify.getGroup(), identify.getVersion());
            referenceCaches.putIfAbsent(referenceKey, config);
            log.info("成功初始化应用 {} 服务提供者 {} 的引用配置", identify.getApplicationIdentify(), identify.getService());
        }
        return config;
    }

    private ReferenceConfig<GenericService> createReference(String service, String group, String version) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setGeneric("true");
        reference.setScopeModel(ApplicationModel.defaultModel().getModuleModels().get(0));
        reference.setInterface(service);
        reference.setGroup(group);
        reference.setVersion(version);
        reference.setRetries(0); // 不重试
        return reference;
    }

}
