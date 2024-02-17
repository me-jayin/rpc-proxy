package xyz.me4cxy.proxy.dubbo.metadata.type;

import xyz.me4cxy.proxy.dubbo.asm.descriptor.ClassDescriptor;
import xyz.me4cxy.proxy.dubbo.asm.descriptor.FieldDescriptor;
import xyz.me4cxy.proxy.dubbo.config.TypeConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类型描述工具类
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/11
 */
public class ProxyTypeDescriptorUtils {
    private static final ClassDescriptor SERIALIZABLE = ClassDescriptor.builder()
            .canonicalName(Serializable.class.getCanonicalName())
            .build();

    /**
     * 将类型描述转为类描述
     * @param className
     * @param metadata
     * @return
     */
    public static ClassDescriptor descriptClass(String className, ProxyTypeMetadata metadata) {
        return descriptClass(className, metadata, true);
    }

    /**
     * 将类型描述转为类描述
     * @param className
     * @param metadata
     * @param needConvertField 是否需要转换字段
     * @return
     */
    public static ClassDescriptor descriptClass(String className, ProxyTypeMetadata metadata, boolean needConvertField) {
        ClassDescriptor.ClassDescriptorBuilder builder = ClassDescriptor.builder()
                .canonicalName(className)
                .interfaceClasses(Collections.singletonList(
                        SERIALIZABLE
                ));
        // 是否需要父类
        if (metadata instanceof ProxyInnerComplexTypeMetadata && !(metadata instanceof ProxyComplexTypeMetadata)) {
            ProxyInnerComplexTypeMetadata superMetadata = (ProxyInnerComplexTypeMetadata) metadata;
            Class superClass = TypeConfig.getSuperClass(superMetadata.innerClass());
            ClassDescriptor.ClassDescriptorBuilder superClassBuilder = ClassDescriptor.builder().canonicalName(superClass.getCanonicalName());
            if (superMetadata.isHavGenerics()) {
                superClassBuilder.generics(superMetadata.getGenerics().stream().map(generic -> ClassDescriptor.builder().canonicalName(generic.getCanonicalName()).build()).collect(Collectors.toList()));
            }
            builder.superClass(superClassBuilder.build());
        }

        // 转字段
        if (metadata instanceof ProxyComplexTypeMetadata && needConvertField) {
            ProxyComplexTypeMetadata typeMetadata = (ProxyComplexTypeMetadata) metadata;
            ArrayList<FieldDescriptor> fields = new ArrayList<>(typeMetadata.getFields().size());
            for (Map.Entry<String, ProxyTypeMetadata> entry : typeMetadata.getFields().entrySet()) {
                ProxyTypeMetadata fieldType = entry.getValue();
                fields.add(FieldDescriptor.builder()
                        .name(entry.getKey())
                        .type(descriptClass(fieldType.getCanonicalName(), fieldType, false))
                        .build()
                );
            }

            builder.fields(fields);
        }

        return builder.build();
    }

}
