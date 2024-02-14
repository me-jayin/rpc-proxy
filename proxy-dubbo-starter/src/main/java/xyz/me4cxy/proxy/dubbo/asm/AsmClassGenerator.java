package xyz.me4cxy.proxy.dubbo.asm;

import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import xyz.me4cxy.proxy.dubbo.asm.descriptor.ClassDescriptor;
import xyz.me4cxy.proxy.dubbo.asm.descriptor.FieldDescriptor;

import java.util.List;

/**
 * 类生成
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/10
 */
public class AsmClassGenerator implements Opcodes {

    public static byte[] generateClass(ClassDescriptor clazz) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classWriter.visit(
                V1_8,
                clazz.getAccess(),
                clazz.getClassPath(),
                clazz.getSuperClass() != null ? clazz.getSuperClass().getGenericSignature() : null,
                clazz.getSuperClassPath(),
                clazz.getInterfacesClassPath()
        );

        // 构造函数
        MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(ALOAD, 0);
        methodVisitor.visitMethodInsn(INVOKESPECIAL, clazz.getSuperClassPath(), "<init>", "()V", false);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitMaxs(1, 1);
        methodVisitor.visitEnd();

        // 添加字段描述
        addFields(classWriter, clazz, true);
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    /**
     * 添加字段
     * @param classWriter
     * @param generateGetterSetter 是否生成get/set方法
     */
    private static void addFields(ClassWriter classWriter, ClassDescriptor clazz, boolean generateGetterSetter) {
        if (clazz.getFields() == null) {
            return;
        }
        for (FieldDescriptor field : clazz.getFields()) {
            String fieldName = field.getName(), bytecode = field.getType().getClassPath();

            // 增加字段
            FieldVisitor fieldVisitor = classWriter.visitField(field.getAccess(), fieldName, "L" + bytecode + ";", null, null);
            fieldVisitor.visitEnd();
            //
            if (generateGetterSetter) {
                // get方法
                MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, appendPrefix("get", fieldName), "()" + "L" + bytecode + ";", null, null);
                methodVisitor.visitCode();
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitFieldInsn(GETFIELD, clazz.getClassPath(), fieldName, "L" + bytecode + ";");
                methodVisitor.visitInsn(ARETURN);
                methodVisitor.visitMaxs(1, 1);
                methodVisitor.visitEnd();
                // set方法
                methodVisitor = classWriter.visitMethod(ACC_PUBLIC, appendPrefix("set", fieldName), "(" + "L" + bytecode + ";)V", null, null);
                methodVisitor.visitParameter(fieldName, 0);
                methodVisitor.visitCode();
                methodVisitor.visitVarInsn(ALOAD, 0);
                methodVisitor.visitVarInsn(ALOAD, 1);
                methodVisitor.visitFieldInsn(PUTFIELD, clazz.getClassPath(), fieldName, "L" + bytecode + ";");
                methodVisitor.visitInsn(RETURN);
                methodVisitor.visitMaxs(2, 2);
                methodVisitor.visitEnd();
            }
        }
    }

    private static String appendPrefix(String prefix, String field) {
        char[] chars = field.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return prefix + String.valueOf(chars);
    }

}
