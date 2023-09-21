public interface UsersList {
    void add_user(User user);

    User get_user_by_index(int index) throws Throwable;

    User get_user_by_id(int id) throws Throwable;

    int size();
}
