public class UsersArrayList implements UsersList {

    private User[] users = new User[10];
    private int size = 0;

    public User[] getUsers() {
        return users;
    }

    public void addUser(final User user) {
        if (users.length == size) {
            User[] tmp = users;
            users = new User[users.length + (users.length / 2)];
            for (int i = 0; i < tmp.length; i++) {
                users[i] = tmp[i];
            }
        }
        users[size++] = user;
    }

    public User getUserByIndex(final int index) throws Exception {
        if (index >= size) {
            throw new Exception("The index is out of bound !");
        }
        return users[index];
    }

    public User getUserById(final int id) throws Exception {
        for (int i = 0; i < size; i++) {
            if (id == users[i].getIdentifier()) {
                return users[i];
            }
        }
        throw new UserNotFoundException();
    }

    public int size() {
        return size;
    }

    static class UserNotFoundException extends Exception {
        UserNotFoundException() {
            super("TransactionNotFoundException");
        }
    }
}
