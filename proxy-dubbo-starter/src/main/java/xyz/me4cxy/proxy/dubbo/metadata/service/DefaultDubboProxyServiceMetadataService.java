package xyz.me4cxy.proxy.dubbo.metadata.service;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.metadata.definition.model.ServiceDefinition;
import org.apache.dubbo.metadata.definition.model.TypeDefinition;
import org.apache.dubbo.metadata.report.MetadataReport;
import org.apache.dubbo.metadata.report.MetadataReportInstance;
import org.apache.dubbo.metadata.report.identifier.MetadataIdentifier;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.GlobalTypeRegister;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;
import xyz.me4cxy.proxy.dubbo.metadata.definition.TypeDefinitionWrapper;
import xyz.me4cxy.proxy.exception.NotFoundServiceException;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * dubbo默认的metadata service，通过 ServiceDefinition 获取得到 ServiceDefinition 从而获取元数据
 * 由于dubbo的元数据都是类级别的，获取某个方法的信息时，需要先获取类的元数据。
 * 因此，采用服务级别的缓存，减少类下多个方法需要重复获取
 * @author jayin
 * @since 2024/01/28
 */
@Slf4j
@Component
public class DefaultDubboProxyServiceMetadataService extends CacheableProxyServiceMetadataService {

    private final MetadataReport metadataReport;

    public DefaultDubboProxyServiceMetadataService() {
        Map<String, MetadataReport> reports = ApplicationModel.defaultModel().getBeanFactory().getBean(MetadataReportInstance.class).getMetadataReports(false);
        Assert.isTrue(reports.size() >= 1, "proxy依赖于dubbo元数据中心，请先检查元数据中心相关配置");
        Assert.isTrue(reports.size() == 1, "获取到多个dubbo元数据中心，目前暂时只支持单个元数据中心");

        metadataReport = reports.values().stream().findFirst().get();
    }

    @Override
    protected String getCacheKey(ProxyIdentify identify) {
        return ((DubboProxyIdentify) identify).serviceIdentifyKey();
    }

    @Override
    public ProxyServiceMetadata loadMetadata0(ProxyIdentify identify) {
        DubboProxyIdentify proxyIdentify = (DubboProxyIdentify) identify;
        MetadataIdentifier identifier = new MetadataIdentifier();
        identifier.setApplication(proxyIdentify.getApplication());
        identifier.setServiceInterface(proxyIdentify.getService());
        identifier.setGroup(proxyIdentify.getGroup());
        identifier.setVersion(proxyIdentify.getVersion());
        identifier.setSide("provider");

        String metadataStr = metadataReport.getServiceDefinition(identifier);
        if (StringUtils.isEmpty(metadataStr)) {
            log.info("获取服务 {} 的元数据失败，元数据：{}", proxyIdentify.serviceIdentifyKey(), metadataStr);
            throw new NotFoundServiceException("服务元数据未找到");
        }

        log.debug("服务 {} 元数据加载成功：{}", proxyIdentify.serviceIdentifyKey(), metadataStr);
        // 解析json
        ServiceDefinition serviceDefinition = JSONObject.parseObject(metadataStr, ServiceDefinition.class);
        // 获取服务相关的类型定义
        Map<String, TypeDefinitionWrapper> typeToDefinition = serviceDefinition.getTypes().stream().collect(Collectors.toMap(TypeDefinition::getType, TypeDefinitionWrapper::new));

        // 按应用层级注册类型定义信息并创建服务元数据
        String applicationIdentify = proxyIdentify.applicationIdentifyKey();
        GlobalTypeRegister.registerAll(applicationIdentify, typeToDefinition);
        ProxyServiceMetadata metadata = new ProxyServiceMetadata(applicationIdentify, serviceDefinition);
        log.info("服务方法 {} 的元数据信息加载成功：{}", applicationIdentify, metadata);
        return metadata;
    }
}
