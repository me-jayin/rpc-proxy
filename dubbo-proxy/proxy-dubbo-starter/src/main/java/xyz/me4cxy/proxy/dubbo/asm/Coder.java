package xyz.me4cxy.proxy.dubbo.asm;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.IOUtils;
import org.springframework.core.io.ClassPathResource;
import xyz.me4cxy.proxy.dubbo.asm.descriptor.ClassDescriptor;
import xyz.me4cxy.proxy.dubbo.core.Cleaner;
import xyz.me4cxy.proxy.dubbo.core.CleanerRegistry;
import xyz.me4cxy.proxy.dubbo.metadata.GlobalTypeRegister;
import xyz.me4cxy.proxy.dubbo.metadata.type.ProxyTypeDescriptorUtils;
import xyz.me4cxy.proxy.dubbo.metadata.type.ProxyTypeMetadata;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义asm编码器
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/09
 */
@Slf4j
public class Coder extends ClassLoader {
    private static Properties DUMP_CONFIG = new Properties();
    /**
     * 实例缓存
     */
    private static final Map<String, Coder> CODER_CACHES = new ConcurrentHashMap<>();
    /**
     * 应用前缀
     */
    private final String applicationPrefix;
    /**
     * 内部类的添加器
     */
    private final AtomicLong classSize = new AtomicLong();

    static {

        ClassPathResource resource = new ClassPathResource("classpath:asm-dump.properties");
        if (resource.exists()) {
            try {
                DUMP_CONFIG.load(resource.getInputStream());
            } catch (IOException e) {
                log.error("asm-dump配置加载失败", e);
                throw new RuntimeException(e);
            }
        }
    }

    public Coder(String applicationPrefix) {
        this.applicationPrefix = applicationPrefix;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        ProxyTypeMetadata metadata = GlobalTypeRegister.getByClassName(name);
        // 将类型转为类字节数组
        ClassDescriptor clazz = ProxyTypeDescriptorUtils.descriptClass(name, metadata);
        byte[] bytes = AsmClassGenerator.generateClass(clazz);
        log.info("类 {} 生成成功", clazz.getCanonicalName());
        dumpClassFile(clazz.getClassPath(), bytes);
        return defineClass(name, bytes, 0 , bytes.length);
    }

    private void dumpClassFile(String classPath, byte[] bytes) {
        if (!StringUtils.equalsAny(DUMP_CONFIG.getProperty("asm.dump.enabled"), "true", "1")) {
            log.debug("无需dump类文件");
            return;
        }

        String path = DUMP_CONFIG.getProperty("asm.dump.path");
        try {
            File file = new File(path + StringUtils.substringBeforeLast(classPath, "/"));
            if (!file.exists()) {
                file.mkdirs();
            }
            IOUtils.write(new ByteArrayInputStream(bytes), new FileOutputStream(path + classPath + ".class"));
        } catch (IOException e) {
            log.error("类 {} 文件dump失败", classPath, e);
        }
    }

    /**
     * 获取实例，每个应用类提供一个，方便后续卸载应用
     * @param applicationPrefix
     * @return
     */
    public static Coder getInstance(String applicationPrefix) {
        return CODER_CACHES.computeIfAbsent(applicationPrefix, key -> {
            CleanerRegistry.addCleaner(applicationPrefix, new Cleaner() {
                @Override
                public void clear() {
                    CODER_CACHES.remove(applicationPrefix);
                }

                @Override
                public String name() {
                    return "ASM Coder 清除器";
                }
            });
            return new Coder(key);
        });
    }

    /**
     * 获取类名称
     * @return
     */
    public String generateClassName(String type) {
        return applicationPrefix + "." + type + classSize.addAndGet(1);
    }

}
