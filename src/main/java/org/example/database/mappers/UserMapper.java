package org.example.database.mappers;

import org.example.database.entity.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {

    /**
     * @param rs
     * @param ctx
     * @return
     * @throws SQLException
     */
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstname(rs.getString("firstname"));
        user.setEmailAddress(rs.getString("email"));
        return user;
    }
}
