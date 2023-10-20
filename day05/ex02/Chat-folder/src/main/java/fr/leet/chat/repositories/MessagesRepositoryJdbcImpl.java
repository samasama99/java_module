package fr.leet.chat.repositories;

import fr.leet.chat.models.Message;
import fr.leet.chat.models.Room;
import fr.leet.chat.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import javax.sql.DataSource;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {

 static public class NotSavedSubEntityException extends RuntimeException {
   public NotSavedSubEntityException(String message) {
     super(message);
   }

 }
  DataSource datasource;

  public MessagesRepositoryJdbcImpl(DataSource dataSource) {
    this.datasource = dataSource;
  }

  @Override
  public Optional<Message> findById(Long id) throws SQLException {
    String SQL_QUERY =
        """
                   SELECT m.*,
                   u.id       as author_id,
                   u.login    as author_login,
                   u.password as author_password,
                   r.id       as room_id,
                   r.name        room_name
            FROM messages m
                     INNER JOIN rooms r on r.id = m.room_id
                     INNER JOIN users u on m.author_id = u.id
            where m.id = ?
            """;

    try (Connection con = datasource.getConnection();
        PreparedStatement pst = con.prepareStatement(SQL_QUERY) ) {
      pst.setLong(1, id);
      ResultSet rs = pst.executeQuery();
      if (rs.next()) {
        Message message = new Message();
        message.setId(rs.getLong("id"));
        message.setText(rs.getString("message"));
        message.setDateTime(rs.getTimestamp("date").toLocalDateTime());

        User owner = new User();
        owner.setId(rs.getLong("author_id"));
        owner.setLogin(rs.getString("author_login"));
        owner.setPassword(rs.getString("author_password"));
        owner.setRooms(new ArrayList<>());
        owner.setCreatedRooms(new ArrayList<>());

        Room room = new Room();
        room.setId(rs.getLong("room_id"));
        room.setName(rs.getString("room_name"));
        room.setCreator(new User());

        message.setAuthor(owner);
        message.setRoom(room);
        return Optional.of(message);
      } else {
        return Optional.empty();
      }
    }
  }

  @Override
  public void save(Message message) {
      String SQL_INSERT = "INSERT INTO messages (message, date, author_id, room_id) VALUES (?, ?, ?, ?)";

      try (Connection con = datasource.getConnection();
           PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
        pst.setString(1, message.getText());
        pst.setTimestamp(2, Timestamp.valueOf(message.getDateTime()));
        pst.setLong(3, message.getAuthor().getId());
        pst.setLong(4, message.getRoom().getId());

        int rowsAffected = pst.executeUpdate();
        if (rowsAffected == 0) {
          throw new SQLException("Inserting message failed, no rows affected.");
        }

        try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            message.setId(generatedKeys.getLong(1));
          } else {
            throw new SQLException("Inserting message failed, no ID obtained.");
          }
        }
      } catch (SQLException e) {
          throw new NotSavedSubEntityException(e.getMessage());
      }
  }
}