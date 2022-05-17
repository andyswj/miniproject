package edu.nus.iss.vttpfinalapplication.repositories;

import static edu.nus.iss.vttpfinalapplication.repositories.Queries.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate template;

    public int createUser(String username, String password) {
        int count = template.update(
            SQL_CREATE_USER,
            username,
            password
        );

        return count;
    }

    public Boolean selectUser(String username, String password) {
        final SqlRowSet rs = template.queryForRowSet(
            SQL_GET_USER, username, password
        );

        if(rs.next())
            return true;
        
        return false;
    }

    public Boolean selectUserByUsername(String username) {
        final SqlRowSet rs = template.queryForRowSet(
            SQL_GET_USER_BY_USERNAME, username
        );

        if(rs.next())
            return true;
        
        return false;
    }

    public int selectUserIdByUsername(String username) {
        final SqlRowSet rs = template.queryForRowSet(
            SQL_GET_USERID_BY_USERNAME, username
        );
        if(rs.next()) {
            int id = rs.getInt("user_id");
            return id;
        }
        return 0;
    }
}
