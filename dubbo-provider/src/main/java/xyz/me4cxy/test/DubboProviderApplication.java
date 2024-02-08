package xyz.me4cxy.test;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * dubbo提供者启动类
 *
 * @author jayin
 * @since 2024/01/01
 */
@EnableDubbo
@SpringBootApplication
public class DubboProviderApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DubboProviderApplication.class, args);
        System.out.println("启动成功");
        Thread.currentThread().join();
    }

}
