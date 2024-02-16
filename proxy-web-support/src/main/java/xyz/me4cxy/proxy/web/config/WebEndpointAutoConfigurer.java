package xyz.me4cxy.proxy.web.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.me4cxy.proxy.core.ProxyRequestContext;
import xyz.me4cxy.proxy.core.RpcProxy;
import xyz.me4cxy.proxy.exception.ProxyException;
import xyz.me4cxy.proxy.web.http.ProxyHttpServletRequest;
import xyz.me4cxy.proxy.web.http.ProxyHttpServletResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 请求端点自动配置类
 *
 * @author jayin
 * @since 2024/01/07
 */
@AutoConfiguration
public class WebEndpointAutoConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    /**
     * 代理入口注册
     */
    @Slf4j
    @RestController
    @RequestMapping("/**")
    public static class ProxyEndpoint {
        @Resource
        private ObjectMapper objectMapper;
        @Resource
        private RpcProxy rpcProxy;
        @Resource
        private HttpServletRequest request;
        @Resource
        private HttpServletResponse response;

        /**
         * 处理基本的
         * @param body
         * @return
         */
        @RequestMapping
        public Object proxyBody(@RequestBody(required = false) String body) {
            try {
                return rpcProxy.proxy(
                        ProxyRequestContext.builder()
                                .request(ProxyHttpServletRequest.builder().request(request).body(body).build())
                                .response(ProxyHttpServletResponse.builder().response(response).build())
                                .build()
                );
            } catch (ProxyException e) {
                log.error(e.getInnerMessage());
                throw e;
            }
        }


        /**
         * 用于处理非 application/json 的请求
         * @return
         */
        @RequestMapping(consumes = "!application/json")
        public Object proxyForm() {
            return proxyBody(null);
        }

    }
}
