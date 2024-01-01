package xyz.me4cxy.proxy.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * proxy代理业务异常
 *
 * @author jayin
 * @since 2023/12/31
 */
public class ProxyServiceException extends RuntimeException {
    /**
     * 消息内部分隔符，用于切割消息内容
     */
    public final static String SEPARATOR = "@:@";
    /**
     * 业务异常状态码
     */
    private final int code;
    /**
     * 业务异常消息
     */
    private final String serviceMsg;
    /**
     * 内部异常消息
     */
    private final String innerMsg;
    /**
     * 抛出异常的时间
     */
    private final long currentTime;

    public ProxyServiceException(int code, String serviceMsg) {
        this(code, serviceMsg, null);
    }

    public ProxyServiceException(int code, String serviceMsg, String innerMsg) {
        this(System.currentTimeMillis(), code, serviceMsg, innerMsg);
    }

    private ProxyServiceException(long currentTime, int code, String serviceMsg, String innerMsg) {
        this.currentTime = currentTime;
        this.code = code;
        this.serviceMsg = serviceMsg;
        this.innerMsg = innerMsg;
    }

    @Override
    public String getMessage() {
        return Stream.of(String.valueOf(currentTime), String.valueOf(code), serviceMsg, innerMsg)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(SEPARATOR));
    }

    public int getCode() {
        return code;
    }

    public String getServiceMsg() {
        return serviceMsg;
    }

    public String getInnerMsg() {
        return innerMsg;
    }

    /**
     * 无需把完整异常栈返回
     *
     * @return
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }

    /**
     * 解析自定义异常内容
     * @param msg
     * @return
     */
    public static ProxyServiceException parse(String msg) {
        String[] splits = StringUtils.split(msg, SEPARATOR);
        if (splits == null) {
            return null;
        } else if (splits.length == 3) {
            return new ProxyServiceException(Long.parseLong(splits[0]), Integer.parseInt(splits[1]), splits[2], null);
        } else if (splits.length == 4) {
            return new ProxyServiceException(Long.parseLong(splits[0]), Integer.parseInt(splits[1]), splits[2], splits[3]);
        }
        return null;
    }
}
