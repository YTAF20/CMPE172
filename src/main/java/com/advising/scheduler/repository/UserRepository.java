package com.advising.scheduler.repository;

import com.advising.scheduler.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<User> findStudents() {
        logger.info("Querying all users with role STUDENT");
        String sql = "SELECT user_id, name, role FROM users WHERE role = 'STUDENT'";
        return jdbc.query(sql, (rs, rowNum) -> {
            User u = new User();
            u.setUserId(rs.getLong("user_id"));
            u.setName(rs.getString("name"));
            u.setRole(rs.getString("role"));
            return u;
        });
    }
}
