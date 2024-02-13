package org.example.user;

public class User {
    private String fName;
    private String lName;
    private int age;
    private boolean isActive = true;
    private double wallet;
    public User(String fName, String lName, int age, boolean isActive, double wallet) {
        this.fName = fName;
        this.lName = lName;
        this.age = age;
        this.isActive = isActive;
        this.wallet = wallet;
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
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", age=" + age +
                ", isActive=" + isActive +
                ", wallet=" + wallet +
                '}';
    }
}
