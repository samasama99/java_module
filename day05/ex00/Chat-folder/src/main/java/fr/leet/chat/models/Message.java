package fr.leet.chat.models;

import java.sql.Date;
import java.util.Objects;

public class Message {
  private Long id;

  @Override
  public String toString() {
    return "Message{"
        + "id="
        + id
        + ", author="
        + author
        + ", room="
        + room
        + ", message='"
        + message
        + '\''
        + ", created="
        + created
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message message = (Message) o;
    return Objects.equals(id, message.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  private User author;
  private Room room;
  private String message;
  private Date created;
}
