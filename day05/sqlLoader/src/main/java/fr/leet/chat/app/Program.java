package fr.leet.chat.app;

import fr.leet.chat.datasource.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Program {

  private static final Logger LOGGER = LoggerFactory.getLogger(Program.class);

  static void runSQL(Connection connection, String instrcution) throws Exception {
    connection.createStatement().execute(instrcution);
  }

  static void loadAndRunSQLFile(Connection connection, Path path) throws Exception {
    Stream.of(Files.readString(path).split(";", 0))
        .map(String::trim).forEach((instrcution) -> {
          try {
            // LOGGER.info(instrcution);
            runSQL(connection, instrcution);
          } catch (Exception e) {
            LOGGER.error(instrcution, e);
          }
        });
  }

  public static void main(String[] args) throws Exception {
    DataSource dataSource = new DataSource();
    LOGGER.info("CONNECTED");
    loadAndRunSQLFile(dataSource.getConnection(), Path.of("src/main/java/resources/schema.sql"));
    LOGGER.info("SCHEMA SQL LOADED");
    loadAndRunSQLFile(dataSource.getConnection(), Path.of("src/main/java/resources/data.sql"));
    LOGGER.info("DATA SQL LOADED");
    LOGGER.info("CLOSING CONNECTION");
    dataSource.close();
  }
}
