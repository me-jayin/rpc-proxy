package xyz.me4cxy.proxy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http请求参数位置及信息集。
 * <b>必须注意，方法中有多少个参数接受代理参数就写多少个，否则代理传入的值将为 null</b>
 *
 * 例如：
 * <pre>{@code
 *     @HttpParams({
 *        @HttpParam(index = 0, name = "id"),
 *        @HttpParam(index = 1, name = "type")
 *     })
 *     User findByIdAndType(String id, String type)
 * }</pre>
 * 这样，在proxy层将会根据位置，匹配到对应的属性名。
 *
 * @author jayin
 * @since 2024/01/01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyParams {

    ProxyParam[] value();

}
