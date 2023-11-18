package fr.leet.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmbeddedDataSourceTest {
    DataSource dataSource;

    @BeforeEach
    void init() {
        System.out.println("[INFO] Before Each init()");
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
    }

    @AfterEach
    void close() {
        System.out.println("[INFO] After Each close()");
        try {
            Connection con = dataSource.getConnection();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.execute("DROP TABLE Product");
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("[ERROR] Failed to get close connection: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to get connection: " + e.getMessage());
        }
        try {
            Thread.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException e) {
            System.out.println("[ERROR] Failed to sleep in AfterEach close(): " + e.getMessage());
        }
    }

    @Test
    public void testConnection() {
        System.out.println("[INFO] Test testConnection()");
        assertDoesNotThrow(() -> assertNotNull(dataSource.getConnection()));
    }
}