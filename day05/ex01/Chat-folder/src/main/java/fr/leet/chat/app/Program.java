package fr.leet.chat.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.leet.chat.datasource.DataSource;
import fr.leet.chat.models.Message;
import fr.leet.chat.repositories.MessagesRepositoryJdbcImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.stream.Stream;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Program {

  private static final Logger LOGGER = LoggerFactory.getLogger(Program.class);

  public static void main(String[] args) throws Exception {
    DataSource dataSource = new DataSource();
    MessagesRepositoryJdbcImpl messagesRepositoryJdbc = new MessagesRepositoryJdbcImpl(dataSource);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    LOGGER.info("CONNECTED");

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
    LOGGER.info("CLOSING CONNECTION");
    dataSource.close();
    System.exit(0);
  }
}
