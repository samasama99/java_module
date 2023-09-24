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
        User uIndex = list.getUserByIndex(i);
        User uId = list.getUserById(i);
        System.out.printf(
            "%-5d %-10s %-10d\n", uIndex.getIdentifier(), uIndex.getName(), uIndex.getBalance());
        System.out.printf(
            "%-5d %-10s %-10d\n", uId.getIdentifier(), uId.getName(), uId.getBalance());
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
