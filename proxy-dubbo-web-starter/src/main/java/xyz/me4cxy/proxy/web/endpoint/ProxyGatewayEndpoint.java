package xyz.me4cxy.proxy.web.endpoint;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.me4cxy.proxy.dubbo.core.CleanerRegistry;

/**
 * 代理网关端点
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/14
 */
@RestController
@RequestMapping("/proxy")
public class ProxyGatewayEndpoint {

    /**
     * 卸载应用信息
     * @param application
     * @return
     */
    @PostMapping("/uninstall/{application}")
    public String applicationUninstall(@PathVariable String application) {
        CleanerRegistry.clear(application);
        return "OK";
    }


}
