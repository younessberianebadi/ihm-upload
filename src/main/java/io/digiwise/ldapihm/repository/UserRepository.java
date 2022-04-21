package io.digiwise.ldapihm.repository;

import io.digiwise.ldapihm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    class UserRowMapper implements RowMapper<User>{
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEnabled(rs.getBoolean("enabled"));
            return user;
        }
    }

    public List < User > findAll(){
        return jdbcTemplate.query("select * from users", new UserRowMapper());
    }

    public int insert(User user){
        return jdbcTemplate.update("insert into users (username, password, enabled) " + "values(?, ?, ?)",
                new Object[]{user.getUsername(), user.getPassword(), user.getEnabled()
                });
    }
}
