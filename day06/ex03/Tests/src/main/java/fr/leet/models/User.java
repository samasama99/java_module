package fr.leet.models;

public class User {
    private final long identifier;
    private final String login;
    private final String password;
    private boolean isAuthenticated;

    public User(long identifier, String login, String password, boolean isAuthenticated) {
        this.identifier = identifier;
        this.login = login;
        this.password = password;
        this.isAuthenticated = isAuthenticated;
    }

    public long getIdentifier() {
        return identifier;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
}
