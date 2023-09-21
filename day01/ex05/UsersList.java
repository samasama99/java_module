public interface UsersList {
    void add_user(User user);

    User get_user_by_index(int index) throws Exception;

    User get_user_by_id(int id) throws Exception;

    int size();
}
