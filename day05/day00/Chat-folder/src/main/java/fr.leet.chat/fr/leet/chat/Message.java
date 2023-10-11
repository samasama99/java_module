package fr.leet.chat;

import java.sql.Date;

public class Message {
    private Long id;
    private User author;
    private Room room;
    private String message;
    private Date created; 
}
