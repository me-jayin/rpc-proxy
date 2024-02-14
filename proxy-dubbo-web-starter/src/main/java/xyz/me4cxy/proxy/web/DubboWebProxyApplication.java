package xyz.me4cxy.proxy.web;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import xyz.me4cxy.proxy.dubbo.config.DubboProxyAutoConfigurer;

/**
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/08
 */
@EnableDubbo
@SpringBootApplication(scanBasePackages = "xyz.me4cxy")
public class DubboWebProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(DubboWebProxyApplication.class);
    }
}
