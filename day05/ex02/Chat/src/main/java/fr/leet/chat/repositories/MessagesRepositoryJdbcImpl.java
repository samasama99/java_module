package fr.leet.chat.repositories;

import fr.leet.chat.models.Message;
import fr.leet.chat.models.Room;
import fr.leet.chat.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Optional;

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
             PreparedStatement pst = con.prepareStatement(SQL_QUERY)) {
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User owner = new User(
                        rs.getLong("author_id"),
                        rs.getString("author_login"),
                        rs.getString("author_password"),
                        null,
                        null
                );

                Room room = new Room(
                        rs.getLong("room_id"),
                        rs.getString("room_name"),
                        null,
                        null
                );

                Message message = new Message(
                        rs.getLong("id"),
                        owner,
                        room,
                        rs.getString("message"),
                        rs.getTimestamp("date").toLocalDateTime()
                );

                return Optional.of(message);
            }
            return Optional.empty();
        }
    }

    @Override
    public void save(Message message) {
        String SQL_INSERT = "INSERT INTO messages (message, date, author_id, room_id) VALUES (?, ?, ?, ?)";
        if (message.getAuthor().getId() == null || message.getRoom().getId() == null) {
            throw new NotSavedSubEntityException((message.getAuthor().getId() == null ? "Author's " : "Room's ") + "Id is null");
        }

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
