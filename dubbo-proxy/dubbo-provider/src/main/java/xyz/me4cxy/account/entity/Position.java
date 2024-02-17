package xyz.me4cxy.account.entity;

/**
 * 职位
 *
 * @author Jayin
 * @email 1035933250@qq.com
 * @date 2024/02/05
 */
public enum Position {

    DOCTOR(0),
    TEACHER(1),
    STUDENT(2);

    int code;

    Position(int code) {
        this.code = code;
    }
}
