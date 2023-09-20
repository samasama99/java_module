public class UsersArrayList implements  UsersList {

    User[] users = new User[10];
    int size = 0;
    public void add_user(User user) {
        if (users.length == size) {
            User[] tmp = users;
            users = new User[users.length + (users.length / 2)];
            for (int i = 0; i < tmp.length; i++) {
                users[i] = tmp[i];
            }
        }
        users[size++] = user;
    }

    public User get_user_by_index(int index) throws Exception {
        if (index >= size) {
            throw new Exception("The index is out of bound !");
        }
        return users[index];
    }

    public User get_user_by_id(int id) throws Exception {
        for (int i = 0; i < size; i++) {
            if (id == users[i].getIdentifier()) {
                return users[i];
            }
        }
        throw new Exception("UserNotFoundException!");
    }

    public int size() {
        return size;
    }
}
