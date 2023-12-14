package fr.leet.chat.repositories;

import fr.leet.chat.models.Room;
import fr.leet.chat.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public record UserRepositoryJdbcImpl(DataSource datasource) implements UserRepository {
    @Override
    public List<User> findAll(int page, int size) throws SQLException {
        String SQL_INSERT = """
                SELECT users.id       as user_id,
                       users.login    as user_login,
                       users.password as user_password,
                       rooms.id as room_id,
                       rooms.name as room_name,
                       rooms.owner_id as room_owner
                FROM users
                         JOIN users_rooms on users.id = users_rooms.user_id
                         JOIN rooms on users_rooms.room_id = rooms.id
                ORDER BY users.id;
                """;
        List<User> users;
        try (Connection con = datasource.getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_INSERT)) {

            ResultSet rs = pst.executeQuery();
            Set<User> users_set = new HashSet<>();
            Map<User, List<Room>> created_rooms = new HashMap<>();
            Map<User, List<Room>> joined_rooms = new HashMap<>();
            while (rs.next()) {
                long user_id = rs.getLong("user_id");
                long owner_id = rs.getLong("room_owner");

                User user = new User();
                user.setId(user_id);
                Room room = new Room();
                room.setName(rs.getString("room_name"));
                room.setId(owner_id);
                if (user_id == owner_id) {
                    addTo(rs, users_set, created_rooms, user, room);
                } else {
                    addTo(rs, users_set, joined_rooms, user, room);
                }
            }
            users = users_set.stream()
                    .skip((long) size * page)
                    .map(u -> new User(
                            u.getId(),
                            u.getLogin(),
                            u.getPassword(),
                            created_rooms.getOrDefault(u, List.of()),
                            joined_rooms.getOrDefault(u, List.of())
                    ))
                    .limit(size)
                    .toList();
        }
        return users;
    }

    private void addTo(ResultSet rs, Set<User> users_set, Map<User, List<Room>> created_rooms, User user, Room room) throws SQLException {
        if (created_rooms.containsKey(user)) {
            created_rooms.get(user).add(room);
        } else {
            user.setLogin(rs.getString("user_login"));
            user.setPassword(rs.getString("user_password"));
            List<Room> list = new ArrayList<>();
            list.add(room);
            created_rooms.put(user, list);
            users_set.add(user);
        }
    }
}
