package xyz.me4cxy.test.service;

import xyz.me4cxy.proxy.annotation.ProxyMethod;
import xyz.me4cxy.proxy.annotation.ProxyMethodType;
import xyz.me4cxy.proxy.annotation.ProxyParam;
import xyz.me4cxy.proxy.annotation.ProxyParamType;
import xyz.me4cxy.proxy.annotation.ProxyParams;
import xyz.me4cxy.test.entity.User;

import java.util.List;

/**
 * @author jayin
 * @since 2024/01/01
 */
public interface UserService {

    @ProxyMethod(method = ProxyMethodType.GET)
    @ProxyParam(index = 0, name = "username")
    User findByUsername(String username);

    @ProxyMethod(method = ProxyMethodType.POST)
    @ProxyParam(index = 0, name = "user", paramType = ProxyParamType.BODY)
    void addUsers(List<User> user);

    @ProxyMethod(method = ProxyMethodType.DELETE)
    @ProxyParam(index = 0, name = "user", paramType = ProxyParamType.BODY)
    void deleteAll(List<String> usernames);

    @ProxyMethod(method = ProxyMethodType.PUT)
    @ProxyParams(@ProxyParam(index = 0, name = "id"))
    @ProxyParam(index = 1, name = "user")
    void updateById(Long id, User user);

}