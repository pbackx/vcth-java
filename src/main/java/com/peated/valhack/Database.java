package com.peated.valhack;

import org.springframework.stereotype.Component;
import java.sql.SQLException;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class Database {
    private final JdbcTemplate jdbcTemplate;

    public Database(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getCurrentDateString() throws SQLException {
        return jdbcTemplate.queryForObject("SELECT CURRENT_TIMESTAMP", String.class);
    }
}
