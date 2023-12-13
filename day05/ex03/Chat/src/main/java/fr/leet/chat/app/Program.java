package fr.leet.chat.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.leet.chat.models.Message;
import fr.leet.chat.models.User;
import fr.leet.chat.repositories.MessagesRepositoryJdbcImpl;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Optional;

public class Program {
    private static final Logger LOGGER = LoggerFactory.getLogger(Program.class);
    private static final Dotenv dotenv = Dotenv.configure().load();
    private static final String url = dotenv.get("DB_URL");
    private static final String username = dotenv.get("DB_USERNAME");
    private static final String password = dotenv.get("DB_PASSWORD");

    public static void main(String[] args) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(0);
        config.setMaximumPoolSize(1);
        HikariDataSource dataSource = new HikariDataSource(config);
        MessagesRepositoryJdbcImpl messagesRepositoryJdbc = new MessagesRepositoryJdbcImpl(dataSource);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Optional<Message> messageOptionalBefore = messagesRepositoryJdbc.findById(5L);
            if (messageOptionalBefore.isPresent()) {
                Message tmp = messageOptionalBefore.get();
                System.out.println(gson.toJson(tmp));
                tmp.setMessage("HELLO");
                tmp.setDate(null);
                User author = new User();
                author.setId(2L);
                tmp.setAuthor(author);
                messagesRepositoryJdbc.update(tmp);
            }
            messagesRepositoryJdbc
                    .findById(5L)
                    .map(gson::toJson)
                    .ifPresent(System.out::println);
        } catch (MessagesRepositoryJdbcImpl.NotSavedSubEntityException | SQLException e) {
            System.out.println(e.getMessage());
        }
        dataSource.close();
    }
}
