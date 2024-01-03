package org.example.database.dao;

import com.gitlab.mvysny.jdbiorm.Dao;
import com.gitlab.mvysny.jdbiorm.JdbiOrm;
import org.example.database.entity.User;
import org.example.database.mappers.UserMapper;

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

    public User findByEmailAddress(String emailAddress) {
        return JdbiOrm.jdbi().withHandle(handle -> handle.createQuery("select * from users where email = :emailAdd")
                .bind("emailAdd", emailAddress)
                //.mapToBean(User.class)
                .map(new UserMapper())
                .one());
    }

}
