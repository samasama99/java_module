class Program {
  public static void main(String[] args) {
    try {
      User u1 = new User("oussama", 500);
      User u2 = new User("anass", 200);
      User u3 = new User("amir", 30000);
      System.out.printf("%-5d %-10s %-10d\n", u1.getIdentifier(), u1.getName(), u1.getBalance());
      System.out.printf("%-5d %-10s %-10d\n", u2.getIdentifier(), u2.getName(), u2.getBalance());
      System.out.printf("%-5d %-10s %-10d\n", u3.getIdentifier(), u3.getName(), u3.getBalance());
    } catch (Throwable e) {
      System.out.println(e.getMessage());
    }

    System.out.println(UserIdsGenerator.getInstance().generateId());
    System.out.println(UserIdsGenerator.getInstance().generateId());
    System.out.println(UserIdsGenerator.getInstance().generateId());
  }
}
