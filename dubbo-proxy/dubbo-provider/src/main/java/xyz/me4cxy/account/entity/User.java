package xyz.me4cxy.account.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 用户实体
 *
 * @author jayin
 * @since 2024/01/01
 */
public class User implements Serializable {
    private int year;
    private Long id;
    private String username;
    private Integer age;
    private Position position;

    private List<Integer> roleIds;

    private CustomDate birthday;

    public User(Long id, String username, Integer age) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.position = Position.DOCTOR;
        this.roleIds = Arrays.asList(1, 2, 3);
        this.birthday = new CustomDate();
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public CustomDate getBirthday() {
        return birthday;
    }

    public void setBirthday(CustomDate birthday) {
        this.birthday = birthday;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
