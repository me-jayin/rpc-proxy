package xyz.me4cxy.proxy.dubbo.metadata;

/**
 * 类型分类
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/06
 */
public enum ClassType {
    /**
     * 八大基本类型
     */
    BASIC,
    /**
     * 枚举
     */
    ENUM,
    /**
     * 内置的对象
     */
    INNER_OBJECT,
    /**
     * 普通对象
     */
    OBJECT;

    /**
     * 基本类型列表
     */
    public static final String[] BASIC_TYPES = new String[] {
            "void", "int", "short", "byte", "long", "boolean", "char", "float", "double"
    };
}
