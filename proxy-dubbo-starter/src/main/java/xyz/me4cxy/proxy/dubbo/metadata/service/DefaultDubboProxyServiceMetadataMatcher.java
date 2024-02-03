package xyz.me4cxy.proxy.dubbo.metadata.service;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.metadata.definition.model.ServiceDefinition;
import org.apache.dubbo.metadata.report.MetadataReport;
import org.apache.dubbo.metadata.report.MetadataReportInstance;
import org.apache.dubbo.metadata.report.identifier.MetadataIdentifier;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import xyz.me4cxy.proxy.core.ProxyIdentify;
import xyz.me4cxy.proxy.dubbo.DubboProxyIdentify;
import xyz.me4cxy.proxy.dubbo.metadata.ProxyServiceMetadata;
import xyz.me4cxy.proxy.exception.NotFoundServiceException;

import java.util.Map;

/**
 * dubbo默认的metadata service，通过 ServiceDefinition 获取得到 ServiceDefinition 从而获取元数据
 * @author jayin
 * @since 2024/01/28
 */
@Slf4j
@Component
public class DefaultDubboProxyServiceMetadataMatcher extends CacheableProxyServiceMetadataMatcher {

    private final MetadataReport metadataReport;

    public DefaultDubboProxyServiceMetadataMatcher() {
        Map<String, MetadataReport> reports = ApplicationModel.defaultModel().getBeanFactory().getBean(MetadataReportInstance.class).getMetadataReports(false);
        Assert.isTrue(reports.size() >= 1, "proxy依赖于dubbo元数据中心，请先检查元数据中心相关配置");
        Assert.isTrue(reports.size() == 1, "获取到多个dubbo元数据中心，目前暂时只支持单个元数据中心");

        metadataReport = reports.values().stream().findFirst().get();
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
            log.info("获取服务 {} 的元数据失败，元数据：{}", identify.identityKey(), metadataStr);
            throw new NotFoundServiceException("服务元数据未找到");
        }

        // 解析json
        ServiceDefinition serviceDefinition = JSONObject.parseObject(metadataStr, ServiceDefinition.class);
        return new ProxyServiceMetadata(proxyIdentify, serviceDefinition);
    }
}
