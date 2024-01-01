package xyz.me4cxy.proxy.service;

import xyz.me4cxy.proxy.annotation.HttpMethod;
import xyz.me4cxy.proxy.annotation.HttpParam;
import xyz.me4cxy.proxy.annotation.HttpParams;
import xyz.me4cxy.proxy.entity.User;

import java.util.List;

/**
 * @author jayin
 * @since 2024/01/01
 */
public interface UserService {

    @HttpMethod
    @HttpParam(index = 0, name = "username")
    User findByUsername(String username);

    void addUsers(List<User> user);

    @HttpMethod
    @HttpParams(@HttpParam(index = 0, name = "id"))
    @HttpParam(index = 1, name = "user")
    void updateById(Long id, User user);

}
