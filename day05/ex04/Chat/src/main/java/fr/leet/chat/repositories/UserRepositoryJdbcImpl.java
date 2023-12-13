package fr.leet.chat.repositories;

import fr.leet.chat.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public record UserRepositoryJdbcImpl(DataSource datasource) implements UserRepository {
    @Override
    public List<User> findAll(int page, int size) throws SQLException {
        String SQL_INSERT = "SELECT * FROM users LIMIT ? OFFSET ?";
        List<User> users = new ArrayList<>();
        try (Connection con = datasource.getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_INSERT)) {
            pst.setLong(1, size);
            pst.setLong(2, (long) size * (long) page);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        }
        return users;
    }
}
