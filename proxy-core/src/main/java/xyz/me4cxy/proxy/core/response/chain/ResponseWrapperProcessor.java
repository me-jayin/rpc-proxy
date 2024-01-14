package xyz.me4cxy.proxy.core.response.chain;

import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import xyz.me4cxy.proxy.core.response.wrapper.ResponseWrapper;

/**
 * 给响应结果增加一层包装
 *
 * @author jayin
 * @since 2024/01/06
 */
public class ResponseWrapperProcessor implements ResponseProcessor {

    private final ResponseWrapper wrapper;

    public ResponseWrapperProcessor(ResponseWrapper wrapper) {
        Assert.notNull(wrapper, "响应包装处理器不能为空");
        this.wrapper = wrapper;
    }

    @Override
    public Object process(Object result) {
        return wrapper.wrap(result);
    }

    @Override
    public boolean isSupport(Object v) {
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
