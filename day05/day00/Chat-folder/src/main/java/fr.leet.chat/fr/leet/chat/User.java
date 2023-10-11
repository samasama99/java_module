package fr.leet.chat;

import java.util.List;

public class User {
    private Long id;
    private String login;
    private String password;
    private List<Room> ownedRooms;
    private List<Room> joinedRooms;
}
