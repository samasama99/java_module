public interface UsersList {
    void addUser(User user);

    User getUserByIndex(int index) throws Exception;

    User getUserById(int id) throws Exception;

    int size();
}
