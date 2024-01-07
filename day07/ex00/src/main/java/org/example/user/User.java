package org.example.user;

import java.util.UUID;

public class User {
    private final UUID uuid = UUID.randomUUID();
    private String fName;
    private String lName;
    private int age;
    private boolean isActive = true;
    private double wallet;

    public User(String fName, String lName, int age) {
        this.fName = fName;
        this.lName = lName;
        this.age = age;
        this.wallet = 0;
    }

    public User() {
    }

    public void deposit(double value) {
        if (!isActive) return;
        wallet += value;
    }

    public void withdraw(double value) {
        if (!isActive || wallet < value) return;
        wallet -= value;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", age=" + age +
                ", isActive=" + isActive +
                ", wallet=" + wallet +
                '}';
    }

//    @Override
//    public String toString() {
//        String fullName = "full name: %s %s (%d)\n".formatted(lName, fName, age);
//        String wallet = "wallet: " + this.wallet + "\n";
//        String accountState = "The account is " + (isActive ? "active" : "inactive");
//
//        return fullName + wallet + accountState;
//    }
}
