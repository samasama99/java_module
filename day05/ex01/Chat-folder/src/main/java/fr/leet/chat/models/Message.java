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
        + text
        + '\''
        + ", created="
        + dateTime
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
  private String text;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }

  private Date dateTime;
}
