package fr.leet.chat.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.leet.chat.models.Message;
import fr.leet.chat.models.Room;
import fr.leet.chat.models.User;
import fr.leet.chat.repositories.MessagesRepositoryJdbcImpl;
import io.github.cdimascio.dotenv.Dotenv;

import java.time.LocalDateTime;

public class Program {
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

        User user = new User(4L, "user", "user", null, null);
        Room room = new Room(3L, "room", user, null);
        Message message = new Message(null, user, room, "Hello!", LocalDateTime.now());
        try {
            messagesRepositoryJdbc.save(message);
            System.out.println("Insert new element with ID: " + message.getId());
        } catch (MessagesRepositoryJdbcImpl.NotSavedSubEntityException e) {
            System.out.println(e.getMessage());
        }

        dataSource.close();
    }
}
