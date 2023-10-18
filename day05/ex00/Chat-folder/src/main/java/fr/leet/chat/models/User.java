package fr.leet.chat.models;

import java.util.List;
import java.util.Objects;

public class User {
  private Long id;
  private String login;
  private String password;
  private List<Room> ownedRooms;
  private List<Room> joinedRooms;

  @Override
  public String toString() {
    return "User{"
        + "id="
        + id
        + ", login='"
        + login
        + '\''
        + ", password='"
        + password
        + '\''
        + ", ownedRooms="
        + ownedRooms
        + ", joinedRooms="
        + joinedRooms
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
