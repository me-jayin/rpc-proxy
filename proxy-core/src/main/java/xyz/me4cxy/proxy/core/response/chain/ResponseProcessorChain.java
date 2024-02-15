package xyz.me4cxy.proxy.core.response.chain;

import java.util.ArrayList;
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

    public ResponseProcessorChain() {
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

    public ResponseProcessorChain addProcessor(ResponseProcessor processor) {
        if (chain == null) {
            chain = new ArrayList<>();
        }
        chain.add(processor);
        return this;
    }

    public ResponseProcessorChain addProcessors(List<ResponseProcessor> processor) {
        if (chain == null) {
            chain = new ArrayList<>();
        }
        chain.addAll(processor);
        return this;
    }

}
