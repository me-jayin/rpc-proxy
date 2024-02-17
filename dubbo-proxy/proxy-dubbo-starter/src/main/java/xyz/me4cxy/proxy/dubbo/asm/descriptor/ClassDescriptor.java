package xyz.me4cxy.proxy.dubbo.asm.descriptor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Opcodes;
import xyz.me4cxy.proxy.dubbo.metadata.ClassType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/10
 */
@Getter
@Setter
public class ClassDescriptor extends GenericsDescriptor {
    /**
     * 访问控制符
     */
    @Builder.Default
    private int access = ACC_PUBLIC | ACC_SUPER;

    /**
     * 是否基本类型
     */
    @Builder.Default
    private boolean isBasic = false;
    /**
     * 全类名
     */
    private String canonicalName;

    /**
     * 父类
     */
    private ClassDescriptor superClass;

    /**
     * 实现的接口列表
     */
    private List<ClassDescriptor> interfaceClasses;

    /**
     * 字段描述
     */
    private List<FieldDescriptor> fields;

    @Builder
    public ClassDescriptor(List<ClassDescriptor> generics, int access, boolean isBasic, String canonicalName, ClassDescriptor superClass, List<ClassDescriptor> interfaceClasses, List<FieldDescriptor> fields) {
        super(generics);
        this.access = access == 0 ? ACC_PUBLIC | ACC_SUPER : access;
        this.isBasic = isBasic;
        this.canonicalName = canonicalName;
        this.superClass = superClass;
        this.interfaceClasses = interfaceClasses;
        this.fields = fields;
    }

    /**
     * 将全类名中的 . 换成 /
     * @return
     */
    public String getClassPath() {
        return StringUtils.replace(canonicalName, ".", "/");
    }

    /**
     * 获取父类路径
     * @return
     */
    public String getSuperClassPath() {
        return superClass != null ? superClass.getClassPath() : "java/lang/Object";
    }

    /**
     * 获取接口路径数组
     * @return
     */
    public String[] getInterfacesClassPath() {
        if (CollectionUtils.isEmpty(interfaceClasses)) return null;

        return interfaceClasses.stream().map(ClassDescriptor::getClassPath).collect(Collectors.toList()).toArray(new String[0]);
    }

    @Override
    public String getGenericSignature() {
        if (CollectionUtils.isEmpty(getGenerics())) return null;

        return "L" + getClassPath() + "<" + getGenerics().stream().map(g -> "L" + g.getClassPath() + ";").collect(Collectors.joining("")) + ">;";
    }

    public static class ClassDescriptorBuilder {

        public ClassDescriptorBuilder canonicalName(String canonicalName) {
            this.canonicalName = canonicalName;
            this.isBasic(ClassType.isBasicType(canonicalName));
            return this;
        }

    }

    @Override
    public String toString() {
        return "class " + canonicalName;
    }
}
