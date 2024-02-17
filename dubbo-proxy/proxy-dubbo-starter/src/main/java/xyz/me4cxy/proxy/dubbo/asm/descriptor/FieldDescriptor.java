package xyz.me4cxy.proxy.dubbo.asm.descriptor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * 字段描述
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/10
 */
@Getter
@Setter
public class FieldDescriptor extends GenericsDescriptor {
    /**
     * 访问限制类型
     */
    @Builder.Default
    private int access = ACC_PRIVATE;
    /**
     * 字段名称
     */
    private String name;
    /**
     * 默认值
     */
    private Object defaultVal;
    /**
     * 属性类型
     */
    private ClassDescriptor type;

    @Builder
    public FieldDescriptor(List<ClassDescriptor> generics, int access, String name, Object defaultVal, ClassDescriptor type) {
        super(generics);
        this.access = access;
        this.name = name;
        this.defaultVal = defaultVal;
        this.type = type;
    }

    @Override
    public String getGenericSignature() {
        if (CollectionUtils.isEmpty(getGenerics())) return null;

        return "L" + type.getClassPath() + "<" + getGenerics().stream().map(g -> "L" + g.getClassPath() + ";") + ">;";
    }


}
