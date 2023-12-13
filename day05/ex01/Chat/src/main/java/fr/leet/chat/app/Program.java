package fr.leet.chat.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.leet.chat.repositories.MessagesRepository;
import fr.leet.chat.repositories.MessagesRepositoryJdbcImpl;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Scanner;

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

        try (Scanner scanner = new Scanner(System.in)) {
            Thread.sleep(Duration.ofMillis(1500));
            LOGGER.info("CONNECTED");
            MessagesRepository messagesRepositoryJdbc = new MessagesRepositoryJdbcImpl(dataSource);
            System.out.println("Enter a message ID");
            System.out.print("-> ");
            long id;
            if (scanner.hasNextLong()) {
                id = scanner.nextLong();
            } else {
                dataSource.close();
                System.err.println("Enter a valid ID next Time");
                return;
            }
            messagesRepositoryJdbc
                    .findById(id)
                    .ifPresentOrElse(
                            System.out::println,
                            () -> System.out.printf("There is no message with ID %d", id)
                    );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        dataSource.close();
    }
}
