package fr.leet.chat.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.leet.chat.datasource.DataSource;
import fr.leet.chat.models.Message;
import fr.leet.chat.models.Room;
import fr.leet.chat.models.User;
import fr.leet.chat.repositories.MessagesRepositoryJdbcImpl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Program {
    public static void main(String[] args) {
        DataSource dataSource = new DataSource();
        MessagesRepositoryJdbcImpl messagesRepositoryJdbc = new MessagesRepositoryJdbcImpl(dataSource);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        User creator = new User(4L, "user", "user", new ArrayList<>(), new ArrayList<>());
        User author = creator;
        Room room = new Room(3L, "room", creator, new ArrayList<>());
        Message message = new Message(null, author, room, "Hello!", LocalDateTime.now());
        Optional<Long> id = Optional.empty();
        try {
            messagesRepositoryJdbc.save(message);
            System.out.println(message.getId());
            id = Optional.of(message.getId());
        } catch (MessagesRepositoryJdbcImpl.NotSavedSubEntityException e) {
            System.out.println(e.getMessage());
        }

        try {
            if (id.isPresent()) {
                Optional<Message> messageOptional = messagesRepositoryJdbc.findById(id.get());
                if (messageOptional.isPresent()) {
                    Message tmp = messageOptional.get();
                    tmp.setMessage("Bye");
                    tmp.setDate(null);
//                    tmp.getAuthor().setId(20L);
                    System.out.println(gson.toJson(tmp));
                    messagesRepositoryJdbc.update(tmp);
                    System.out.println(gson.toJson(tmp));
                }
            }
        } catch (MessagesRepositoryJdbcImpl.NotSavedSubEntityException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        dataSource.close();
    }
}
