package xyz.me4cxy.proxy.annotation;

import java.lang.annotation.*;

/**
 * http请求参数位置及信息
 *
 * <p>用在dubbo服务类上，用于描述提供者方法的参数位置，以及在http请求中的参数名、取值类型。</p>
 * 
 * 例如
 * <pre>{@code
 *     @HttpParam(index = 0, name = "id")
 *     User findById(String id)
 * }</pre>
 *
 * @author jayin
 * @since 2023/12/31
 */
@Repeatable(ProxyParams.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyParam {

    /**
     * 参数索引，表示指定的是第几个参数
     * @return
     */
    int index();

    /**
     * 参数名，指定从请求中获取哪个参数值
     * @return
     */
    String name();

    /**
     * 参数类型，表示从哪里获取值
     * @return
     */
    ProxyParamType paramType() default ProxyParamType.PARAMETER;

}
