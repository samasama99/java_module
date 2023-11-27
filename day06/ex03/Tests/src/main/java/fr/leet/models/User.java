package fr.leet.models;

public class User {
  private long identifier;
  private String login;
  private String password;
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

  public void setIdentifier(long identifier) {
    this.identifier = identifier;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isAuthenticated() {
    return isAuthenticated;
  }

  public void setAuthenticated(boolean isAuthenticated) {
    this.isAuthenticated = isAuthenticated;
  }
}
