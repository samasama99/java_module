class Program {
    public static void main(String[] args) {
        UsersArrayList list = new UsersArrayList();
        try {
            list.add_user(new User("oussama", 500));
            list.add_user(new User("anass", 200));
            list.add_user(new User("amir", 30000));
            list.add_user(new User("oussama", 500));
            list.add_user(new User("anass", 200));
            list.add_user(new User("amir", 30000));
            list.add_user(new User("oussama", 500));
            list.add_user(new User("anass", 200));
            list.add_user(new User("amir", 30000));
            list.add_user( new User("oussama", 500));
            list.add_user( new User("anass", 200));
            list.add_user( new User("amir", 30000));
            for (int i = 0; i < list.size(); i++) {
                User user = list.get_user_by_index(i);
                User _user = list.get_user_by_id(i);
                System.out.printf("%-5d %-10s %-10d\n", user.getIdentifier(), user.getName(), user.getBalance());
                System.out.printf("%-5d %-10s %-10d\n", _user.getIdentifier(), _user.getName(), _user.getBalance());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}