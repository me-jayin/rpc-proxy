package xyz.me4cxy.proxy.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 请求头持有对象
 *
 * @author jayin
 * @since 2024/01/01
 */
public class HttpHeaderHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHeaderHolder.class);

    /**
     * header名称前缀
     */
    public static final String HEADER_NAME_PREFIX = "HEADER_";

    /**
     * header值分隔符
     */
    public static final String HEADER_VALUE_SEPARATOR = "%%";

    /**
     * IP请求头
     */
    public static final String HEADER_IP = HEADER_NAME_PREFIX + "IP";

    /**
     * 获取实际请求头名称
     * @param header
     * @return
     */
    public static String getActualName(String header) {
        if (header == null) {
            return null;
        }
        return HEADER_NAME_PREFIX + header;
    }

    /**
     * 获取请求头的值
     * @param header 原生请求头名字，如 Content-Type
     * @return
     */
    public static String getHeader(String header) {
        return CollectionUtils.first(getHeaders(header));
    }

    /**
     * 获取请求头的值
     * @param header 原生请求头名字，如 Content-Type
     * @return
     */
    public static String getHeaderByActualName(String header) {
        return CollectionUtils.first(getHeaders(header));
    }

    /**
     * 获取请求头所有的值
     * @param header 原生请求头名字，如 Content-Type
     * @return
     */
    public static List<String> getHeaders(String header) {
        return getHeadersByActualName(getActualName(header));
    }

    public static List<String> getHeadersByActualName(String actualName) {
        if (actualName == null) {
            LOGGER.debug("获取代理请求头名称为空，忽略当前获取操作");
            return Collections.emptyList();
        }

        String value = RpcContext.getServerAttachment().getAttachment(StringUtils.lowerCase(actualName));
        List<String> values;
        if (StringUtils.isEmpty(value)) {
            values = Collections.emptyList();
        } else {
            values = Arrays.asList(StringUtils.split(HEADER_VALUE_SEPARATOR));
        }

        LOGGER.debug("获取代理请求头成功，名称：{}，值：{}", actualName, values);
        return values;
    }

    /**
     * 获取ip地址
     * @return
     */
    public static String getIp() {
        return getHeader(HEADER_IP);
    }

}
