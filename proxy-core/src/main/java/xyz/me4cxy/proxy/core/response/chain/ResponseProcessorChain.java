package xyz.me4cxy.proxy.core.response.chain;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 过滤器链
 *
 * @author jayin
 * @since 2024/01/06
 */
public class ResponseProcessorChain {

    private List<ResponseProcessor> chain;

    public ResponseProcessorChain(List<ResponseProcessor> processor) {
        this.chain = processor.stream().filter(v -> v != null && v != this).collect(Collectors.toList());
    }

    public Object process(Object result) {
        Object v = result;
        for (ResponseProcessor processor : chain) {
            if (processor.isSupport(v)) {
                v = processor.process(result);
            }
        }
        return v;
    }

}
