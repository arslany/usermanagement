package org.example.database.dao;

import com.gitlab.mvysny.jdbiorm.Dao;
import org.example.database.entity.User;

import java.util.List;

/**
 * Data access object for User entity
 */

public class UserDAO extends Dao<User, Long> {

    public UserDAO() {
        super(User.class);
    }

    public List<User> getAllUsers() {
        return findAll();
    }



}
