package fr.leet.chat.models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
  private Long id;

  public Message(Long id, User author, Room room, String text, LocalDateTime dateTime) {
    this.id = id;
    this.author = author;
    this.room = room;
    this.text = text;
    this.dateTime = Timestamp.valueOf(dateTime);
  }

  public Message() {

  }

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

  public LocalDateTime getDateTime() {
    return dateTime.toLocalDateTime();
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = Timestamp.valueOf(dateTime);
  }

  private Timestamp dateTime;
}
