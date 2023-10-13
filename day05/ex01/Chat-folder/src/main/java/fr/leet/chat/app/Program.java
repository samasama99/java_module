package fr.leet.chat.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.leet.chat.datasource.DataSource;
import fr.leet.chat.models.Message;
import fr.leet.chat.repositories.MessagesRepositoryJdbcImpl;
import java.util.Optional;

public class Program {
  public static void main(String[] args) {
    DataSource dataSource = new DataSource();
    MessagesRepositoryJdbcImpl messagesRepositoryJdbc = new MessagesRepositoryJdbcImpl(dataSource);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try {
      Optional<Message> opt = messagesRepositoryJdbc.findById(1L);
      if (opt.isPresent()) {
        Message message = opt.get();
        String json = gson.toJson(message);
        System.out.println(json);
      } else {
        System.out.println("Couldn't find message !");
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
