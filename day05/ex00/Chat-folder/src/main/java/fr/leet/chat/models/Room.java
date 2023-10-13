package fr.leet.chat.models;

import java.util.List;
import java.util.Objects;

public class Room {
  @Override
  public String toString() {
    return "Room{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", owner="
        + owner
        + ", messages="
        + messages
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Room room = (Room) o;
    return Objects.equals(id, room.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  private Long id;
  private String name;
  private User owner;
  private List<Message> messages;
}
