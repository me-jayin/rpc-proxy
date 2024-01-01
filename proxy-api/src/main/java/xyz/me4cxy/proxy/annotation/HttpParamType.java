package xyz.me4cxy.proxy.annotation;

/**
 * 参数类型
 *
 * @author jayin
 * @since 2024/01/01
 */
public enum HttpParamType {

    /**
     * 默认参数，表示从请求对应的地方获取参数：
     * <ul>
     *     <li>query参数：http://me4cxy.xyz/test?a=123&b=123</li>
     *     <li>content-type为application/x-www-form-urlencoded时，从请求体中的 a=123&b=123 中获取</li>
     * </ul>
     */
    PARAMETER,
    /**
     * 请求体参数，表示从请求体中获取。
     * 如果是该类型时，{@link HttpParam#name()}的值不影响取值结果
     */
    BODY,
    /**
     * 请求头参数，表示从请求头中获取
     */
    HEADER,

}
