package xyz.me4cxy.account.api.expose.impl;

import org.apache.dubbo.config.annotation.DubboService;
import xyz.me4cxy.account.entity.User;
import xyz.me4cxy.account.api.expose.UserService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jayin
 * @since 2024/01/01
 */
@DubboService(group = "test", version = "1.2")
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
    public void addUser(User user) {
        System.out.println(user.getUsername());
    }

    @Override
    public void deleteAll(List<String> usernames) {
        usernames.forEach(USERS::remove);
    }

    @Override
    public void updateById(Long id, User user) {
        USERS.values().stream().filter(u -> u.getId().equals(id)).findFirst().ifPresent(u -> {
            u.setAge(user.getAge());
            u.setUsername(user.getUsername());
        });
    }

    @Override
    public void addUser(Map<String, List<Integer>> user) {

    }
}
