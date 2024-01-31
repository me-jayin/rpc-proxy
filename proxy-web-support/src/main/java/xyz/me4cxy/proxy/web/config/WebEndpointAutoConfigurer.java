package xyz.me4cxy.proxy.web.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 请求端点自动配置类
 *
 * @author jayin
 * @since 2024/01/07
 */
@AutoConfiguration
public class WebEndpointAutoConfigurer {


    /**
     * 代理入口注册
     */
    @RestController
    @RequestMapping("/**")
    public static class ProxyEndpoint {
        @Resource
        private HttpServletRequest request;
        @Resource
        private HttpServletResponse response;

        /**
         * 处理基本的
         * @param param
         * @param body
         * @return
         */
        @RequestMapping
        public Object proxyBody(@RequestParam LinkedHashMap<String, String> param, @RequestBody(required = false) Map<String, Object> body) {
            return null;
        }


        /**
         * 用于处理非 application/json 的请求
         * @param param
         * @return
         */
        @RequestMapping(consumes = "!application/json")
        public Object proxyForm(@RequestParam LinkedHashMap<String, String> param) {
            return proxyBody(param, null);
        }

    }
}
