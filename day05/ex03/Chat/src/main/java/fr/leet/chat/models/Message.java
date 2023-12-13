package fr.leet.chat.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private Long id;
    private Timestamp date = null;
    private User author;
    private Room room;
    private String message;


    public Message(Long id, User author, Room room, String message, LocalDateTime date) {
        this.id = id;
        this.author = author;
        this.room = room;
        this.message = message;
        this.date = Timestamp.valueOf(date);
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
               + message
               + '\''
               + ", created="
               + date
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        if (date == null)
            return null;
        return date.toLocalDateTime();
    }

    public void setDate(LocalDateTime newDate) {
        if (newDate == null)
            this.date = null;
        else
            this.date = Timestamp.valueOf(newDate);
    }

}
