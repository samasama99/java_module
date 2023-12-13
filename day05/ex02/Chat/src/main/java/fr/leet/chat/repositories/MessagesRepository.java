package fr.leet.chat.repositories;

import fr.leet.chat.models.Message;

import java.sql.SQLException;
import java.util.Optional;

public interface MessagesRepository {
    Optional<Message> findById(Long id) throws SQLException;

    void save(Message message) throws SQLException;
}
