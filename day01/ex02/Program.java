class Program {
  public static void main(final String[] args) {
    UsersArrayList list = new UsersArrayList();
    try {
      list.addUser(new User("oussama", 500));
      list.addUser(new User("anass", 200));
      list.addUser(new User("amir", 30000));
      list.addUser(new User("oussama", 500));
      list.addUser(new User("anass", 200));
      list.addUser(new User("amir", 30000));
      list.addUser(new User("oussama", 500));
      list.addUser(new User("anass", 200));
      list.addUser(new User("amir", 30000));
      list.addUser(new User("oussama", 500));
      list.addUser(new User("anass", 200));
      list.addUser(new User("amir", 30000));
      for (int i = 0; i < list.size(); i++) {
        User index = list.getUserByIndex(i);
        User id = list.getUserById(i);
        System.out.printf(
            "%-5d %-10s %-10d\n", index.getIdentifier(), index.getName(), index.getBalance());
        System.out.printf(
            "%-5d %-10s %-10d\n", id.getIdentifier(), id.getName(), id.getBalance());
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
