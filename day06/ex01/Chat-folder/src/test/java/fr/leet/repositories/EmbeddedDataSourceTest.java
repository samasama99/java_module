package fr.leet.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("[ERROR] Failed to get close connection: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to get connection: " + e.getMessage());
        }
    }

    @Test
    public void testConnection() {
        System.out.println("[INFO] Test testConnection()");
        assertDoesNotThrow(() -> assertNotNull(dataSource.getConnection()));
    }
}