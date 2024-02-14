package xyz.me4cxy.proxy.dubbo.asm.descriptor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Opcodes;

import java.util.List;

/**
 * 泛型描述
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/10
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class GenericsDescriptor implements Opcodes {
    /**
     * 泛型列表
     */
    protected List<ClassDescriptor> generics;

    /**
     * 获取泛型签名
     * @return
     */
    public abstract String getGenericSignature();

}
