package service;

import entity.User;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public class UserServiceImpl implements UserService {

    public User getUser(int id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }
}
