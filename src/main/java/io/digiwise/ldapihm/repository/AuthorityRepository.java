package io.digiwise.ldapihm.repository;



import io.digiwise.ldapihm.model.Authority;
import io.digiwise.ldapihm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AuthorityRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    class AuthorityRowMapper implements RowMapper<Authority> {
        @Override
        public Authority mapRow(ResultSet rs, int rowNum) throws SQLException {
            Authority authority = new Authority();
            authority.setUsername(rs.getString("username"));
            authority.setAuthority(rs.getString("authority"));
            return authority;
        }
    }

    public List<Authority> findAll(){
        return jdbcTemplate.query("select * from authorities", new AuthorityRowMapper());
    }

    public int insert(Authority authority){
        return jdbcTemplate.update("insert into authorities (username, authority) " + "values(?, ?)",
                new Object[]{authority.getUsername(), authority.getAuthority()
                });
    }
}
