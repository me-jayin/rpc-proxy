package xyz.me4cxy.proxy.dubbo.invoker.matcher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodMetadata;
import xyz.me4cxy.proxy.dubbo.metadata.method.ProxyMethodParameterMetadata;
import xyz.me4cxy.proxy.support.context.ProxyRequest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 请求默认匹配器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/09
 */
public class DefaultProxyRequestMethodMatcher implements MethodMatcher {

    @Override
    public List<ProxyMethodMetadata> match(List<ProxyMethodMetadata> methods, ProxyRequestContext context) {
        String requestMethod = context.getRequest().requestMethod().toUpperCase();
        List<MatchScore> scores = methods.stream()
                .filter(metadata -> metadata.getRequestMethod().contains(requestMethod))
                .map(metadata -> match(metadata, context))
                .sorted(Comparator.comparing(MatchScore::getScore))
                .collect(Collectors.toList());
        if (scores.isEmpty()) return Collections.emptyList();

        return scores.stream().filter(s -> s.getScore() == scores.get(0).getScore()).map(MatchScore::getMetadata).collect(Collectors.toList());
    }

    public MatchScore match(ProxyMethodMetadata methodMetadata, ProxyRequestContext context) {
        int count = 0;
        ProxyRequest request = context.getRequest();
        for (ProxyMethodParameterMetadata param : methodMetadata.getParams()) {
            ProxyParamType type = param.getParamType();
            boolean match = false;
            if (type.equals(ProxyParamType.ADDITIONAL) && matchMap(param.getName(), request.getAdditional())
                    || type.equals(ProxyParamType.PARAMETER) && matchMap(param.getName(), request.getParameters())
                    || type.equals(ProxyParamType.BODY) && request.getBody() != null) {
                match = true;
            }

            if (match) count++;
        }

        return new MatchScore(count, methodMetadata);
    }

    /**
     * 匹配map
     * @param name
     * @param param
     * @return
     */
    private boolean matchMap(String name, Map param) {
        for (Object entry : param.entrySet()) {
            String key = (String) ((Map.Entry) entry).getKey();
            if (key.equals(name) || StringUtils.startsWith(key, name + ".")) {
                return true;
            }
        }
        return false;
    }

    @Getter
    @AllArgsConstructor
    class MatchScore {
        int score;
        ProxyMethodMetadata metadata;
    }

}
