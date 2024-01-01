package xyz.me4cxy.proxy.service.impl;

import org.apache.dubbo.config.annotation.DubboService;
import xyz.me4cxy.proxy.entity.User;
import xyz.me4cxy.proxy.service.UserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jayin
 * @since 2024/01/01
 */
@DubboService
public class LocalUserServiceImpl implements UserService {

    Map<String, User> USERS = new HashMap<>();

    public LocalUserServiceImpl() {
        this.addUsers(Arrays.asList(
                new User(1L, "jayin", 18),
                new User(2L, "test", 18),
                new User(3L, "admin", 18)
        ));
    }

    @Override
    public User findByUsername(String username) {
        return USERS.get(username);
    }

    @Override
    public void addUsers(List<User> user) {
        user.forEach(u -> USERS.put(u.getUsername(), u));
    }

    @Override
    public void updateById(Long id, User user) {
        USERS.values().stream().filter(u -> u.getId().equals(id)).findFirst().ifPresent(u -> {
            u.setAge(user.getAge());
            u.setUsername(user.getUsername());
        });
    }
}