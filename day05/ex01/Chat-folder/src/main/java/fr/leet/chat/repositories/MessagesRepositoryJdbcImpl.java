package fr.leet.chat.repositories;

import fr.leet.chat.models.Message;
import fr.leet.chat.models.Room;
import fr.leet.chat.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

public class MessagesRepositoryJdbcImpl implements MessagesRepository {
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
        PreparedStatement pst = con.prepareStatement(SQL_QUERY); ) {
      pst.setLong(1, id);
      ResultSet rs = pst.executeQuery();
      if (rs.next()) {
        Message message = new Message();
        message.setId(rs.getLong("id"));
        message.setText(rs.getString("message"));
        message.setDateTime(rs.getDate("date"));

        User owner = new User();
        owner.setId(rs.getLong("author_id"));
        owner.setLogin(rs.getString("author_login"));
        owner.setPassword(rs.getString("author_password"));

        Room room = new Room();
        room.setId(rs.getLong("room_id"));
        room.setName(rs.getString("room_name"));

        message.setAuthor(owner);
        message.setRoom(room);
        return Optional.of(message);
      } else {
        return Optional.empty();
      }
    }
  }
}
