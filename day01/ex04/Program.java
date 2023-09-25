import java.util.UUID;

public class Program {
  public static void main(final String[] args) {
    try {
      TransactionsService service = new TransactionsService();
      service.addUser("oussama", 1000);
      service.addUser("amir", 5000);
      service.addUser("anass", 999);

      System.out.printf(
          "name: %-10s balance: %10d\n",
          service.getUsers().getUserById(0).getName(), service.getUserBalance(0));
      System.out.printf(
          "name: %-10s balance: %10d\n",
          service.getUsers().getUserById(1).getName(), service.getUserBalance(1));
      System.out.printf(
          "name: %-10s balance: %10d\n",
          service.getUsers().getUserById(2).getName(), service.getUserBalance(2));
      System.out.println("*****************");

      service.send(0, 1, 500);
      service.send(1, 2, 2000);
      service.send(2, 0, 2500);
      service.send(2, 0, 100);
      for (Transaction t : service.getUnpairedTransactions()) {
        System.out.println(t.getIdentifier());
      }
      {
        UUID id =
            service.getUsers().getUserById(0).getTransactions().toArray()[0].getIdentifier();
        service.removeTransfer(0, id);
        System.out.println("The removed ID :");
        System.out.println(id);
      }
      {
        UUID id =
            service.getUsers().getUserById(2).getTransactions().toArray()[2].getIdentifier();
        service.removeTransfer(2, id);
        System.out.println("The removed ID :");
        System.out.println(id);
      }
      System.out.println("The invalid ID :");
      for (Transaction t : service.getUnpairedTransactions()) {
        System.out.printf(
            "%s sender: %-10s recipient: %-10s\n",
            t.getIdentifier().toString(), t.getSender().getName(), t.getRecipient().getName());
      }
      System.out.println("*****************");
      System.out.printf(
          "name: %-10s balance: %10d\n",
          service.getUsers().getUserById(0).getName(), service.getUserBalance(0));
      System.out.printf(
          "name: %-10s balance: %10d\n",
          service.getUsers().getUserById(1).getName(), service.getUserBalance(1));
      System.out.printf(
          "name: %-10s balance: %10d\n",
          service.getUsers().getUserById(2).getName(), service.getUserBalance(2));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
