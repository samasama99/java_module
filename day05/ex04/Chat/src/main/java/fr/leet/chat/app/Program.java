package fr.leet.chat.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.leet.chat.repositories.UserRepositoryJdbcImpl;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.SQLException;

public class Program {
    private static final Dotenv dotenv = Dotenv.configure().load();
    private static final String url = dotenv.get("DB_URL");
    private static final String username = dotenv.get("DB_USERNAME");
    private static final String password = dotenv.get("DB_PASSWORD");
    private static final HikariConfig config = new HikariConfig();

    static {
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(0);
        config.setMaximumPoolSize(1);
    }

    private static final HikariDataSource dataSource = new HikariDataSource(config);

    public static void main(String[] args) {
        UserRepositoryJdbcImpl userRepositoryJdbc = new UserRepositoryJdbcImpl(dataSource);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            userRepositoryJdbc
                    .findAll(0, 3)
                    .stream()
                    .map(gson::toJson)
                    .forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        dataSource.close();
    }
}
