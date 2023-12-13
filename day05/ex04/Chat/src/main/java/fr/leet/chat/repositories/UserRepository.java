package fr.leet.chat.repositories;

import fr.leet.chat.models.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    List<User> findAll(int page, int size) throws SQLException;
}
