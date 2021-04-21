package service;

import entity.User;

/**
 * @description:
 * @author: Stroke
 * @date: 2021/04/21
 */
public interface UserService {
    User getUser(int id, String username);
}
