public interface UsersList {
    void addUser(User user);

    User getUserByIndex(int index) throws Throwable;

    User getUserById(int id) throws Throwable;

    int size();
}
