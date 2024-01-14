package xyz.me4cxy.proxy.core.mapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.ProxyIdentify;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 映射链
 *
 * @author jayin
 * @since 2024/01/13
 */
@Slf4j
public class ProxyIdentifyMappingChain {
    private final List<ProxyIdentifyMapping> identifyMappings;

    /**
     * 是否使用默认映射
     */
    private boolean useDefaultMapping = true;
    /**
     * 默认映射
     */
    private ProxyIdentifyMapping defaultMapping;

    public ProxyIdentifyMappingChain(List<ProxyIdentifyMapping> identifyMappings) {
        this.identifyMappings = identifyMappings;
    }

    public ProxyIdentifyMappingChain(List<ProxyIdentifyMapping> identifyMappings, boolean useDefaultMapping, ProxyIdentifyMapping defaultMapping) {
        this.identifyMappings = identifyMappings;
        this.useDefaultMapping = useDefaultMapping;
        if (useDefaultMapping) {
            useDefaultMapping(defaultMapping);
        }
    }

    /**
     * 设置默认代理标识mapping
     * @param identifyMappings
     * @param defaultMapping
     */
    public ProxyIdentifyMappingChain(List<ProxyIdentifyMapping> identifyMappings, ProxyIdentifyMapping defaultMapping) {
        this.identifyMappings = identifyMappings;
        useDefaultMapping(defaultMapping);
    }

    /**
     * 获得代理标识
     * @param context
     * @return
     */
    public ProxyIdentify getIdentify(ProxyRequestContext context) {
        Optional<ProxyIdentify> identifyOpt = identifyMappings.stream().map(mapping -> mapping.getIdentify(context)).filter(Objects::nonNull).findFirst();
        // 如果未获取标识，并通过使用默认标识符mapping时则用默认方式提取标识
        ProxyIdentify identify = identifyOpt.orElseGet(() -> useDefaultMapping ? defaultMapping.getIdentify(context) : null);
        if (identify == null) {
            return null;
        }
        return identify;
    }

    public void useDefaultMapping(ProxyIdentifyMapping mapping) {
        Assert.notNull(mapping, "默认代理标识不能为空");
        this.useDefaultMapping = true;
        this.defaultMapping = mapping;
    }

}
