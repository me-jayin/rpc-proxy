package xyz.me4cxy.proxy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示当前service方法仅支持的请求方法类型
 *
 * @author jayin
 * @since 2024/01/01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethod {

    /**
     * 方法别名，如果一个service有重载方法，并且method对应相同时，可以用别名做区分
     * @return
     */
    String[] alias() default "";

    /**
     * 请求方法类型，默认支持所有
     * @return
     */
    HttpMethodType[] method() default { HttpMethodType.GET, HttpMethodType.POST, HttpMethodType.PUT, HttpMethodType.DELETE };

}
