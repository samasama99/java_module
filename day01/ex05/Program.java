import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class Program {
  public static void main(final String[] args) {

    Scanner userInput = new Scanner(System.in);
    TransactionsService service = new TransactionsService();
    while (true) {
      System.out.println("1. Add a user                               ");
      System.out.println("2. View user balances                       ");
      System.out.println("3. Perform a transfer                       ");
      System.out.println("4. View all transactions for a specific user");
      System.out.println("5. DEV - remove a transfer by ID            ");
      System.out.println("6. DEV - check transfer validity            ");
      System.out.println("7. Finish execution                         ");
      System.out.print("-> ");

      boolean validInput = false;
      int command = 0;
      try {
        while (!validInput) {
          try {
            if (!userInput.hasNextInt()) {
              throw new InputMismatchException("Please enter a valid command");
            }
          } catch (InputMismatchException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please try again.");
            userInput.nextLine();
            System.out.print("-> ");
          }
          command = userInput.nextInt();
          if (command >= 1 && command <= 7) {
            validInput = true;
          } else {
            System.out.println("Error: " + "Please enter a valid command");
            System.out.println("Please try again.");
            userInput.nextLine();
            System.out.print("-> ");
          }
        }
        switch (command) {
          case 1 -> {
            System.out.println("Enter a user name and a balance");
            System.out.print("-> ");
            String name = null;
            int balance = 0;
            validInput = false;
            while (!validInput) {
              try {
                if (!userInput.hasNext("[a-zA-Z]+")) {
                  throw new InputMismatchException("Please enter a valid name");
                }

                name = userInput.next();

                if (!userInput.hasNextInt()) {
                  throw new InputMismatchException("Please enter a valid amount");
                }

                balance = userInput.nextInt();

                validInput = true;
              } catch (InputMismatchException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
                userInput.nextLine();
                System.out.print("-> ");
              }
            }
            User user = service.addUser(name, balance);
            System.out.printf("User with id = %d is added", user.getIdentifier());
          }
          case 2 -> {
            System.out.println("Enter a user ID");
            System.out.print("-> ");
            int id = 0;
            validInput = false;
            while (!validInput) {
              try {
                if (!userInput.hasNextInt()) {
                  throw new InputMismatchException("Please enter a valid id");
                }

                id = userInput.nextInt();

                validInput = true;
              } catch (InputMismatchException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
                userInput.nextLine();
                System.out.print("-> ");
              }
            }

            System.out.printf(
                "%s - %d\n",
                service.getUsers().getUserById(id).getName(), service.getUserBalance(id));
          }
          case 3 -> {
            System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");
            System.out.print("-> ");
            int id1 = 0;
            int id2 = 0;
            int amount = 0;

            validInput = false;
            while (!validInput) {
              try {
                if (!userInput.hasNextInt()) {
                  throw new InputMismatchException("Please enter a valid id1");
                }
                id1 = userInput.nextInt();
                if (!userInput.hasNextInt()) {
                  throw new InputMismatchException("Please enter a valid id2");
                }
                id2 = userInput.nextInt();
                if (!userInput.hasNextInt()) {
                  throw new InputMismatchException("Please enter a valid amount");
                }
                amount = userInput.nextInt();

                validInput = true;
              } catch (InputMismatchException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
                userInput.nextLine();
                System.out.print("-> ");
              }
            }
            service.send(id1, id2, amount);
            System.out.println("The transfer is completed ");
          }
          case 4 -> {
            System.out.println("Enter a user ID");
            int id = 0;
            validInput = false;
            while (!validInput) {
              try {
                if (!userInput.hasNextInt()) {
                  throw new InputMismatchException("Please enter a valid id");
                }

                id = userInput.nextInt();

                validInput = true;
              } catch (InputMismatchException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
                userInput.nextLine();
                System.out.print("-> ");
              }
            }
            for (Transaction t : service.retrievingTransfers(id)) {
              switch (t.getTransferCategory()) {
                case debits -> {
                  if (t.getSender().getIdentifier() == id) {
                    System.out.printf(
                        "To %s(id = %d) -%d with id = %s\n",
                        t.getRecipient().getName(),
                        t.getRecipient().getIdentifier(),
                        t.getTransferAmount(),
                        t.getIdentifier());
                  } else {
                    System.out.printf(
                        "From %s(id = %d) %d with id = %s\n",
                        t.getSender().getName(),
                        t.getSender().getIdentifier(),
                        t.getTransferAmount(),
                        t.getIdentifier());
                  }
                }
                case credits -> {
                  if (t.getSender().getIdentifier() == id) {
                    System.out.printf(
                        "From %s(id = %d) %d with id = %s\n",
                        t.getRecipient().getName(),
                        t.getRecipient().getIdentifier(),
                        t.getTransferAmount(),
                        t.getIdentifier());
                  } else {
                    System.out.printf(
                        "To %s(id = %d) -%d with id = %s\n",
                        t.getSender().getName(),
                        t.getSender().getIdentifier(),
                        t.getTransferAmount(),
                        t.getIdentifier());
                  }
                }
                default -> throw new IllegalStateException(
                    "Unexpected value: " + t.getTransferCategory());
              }
            }
          }
          case 5 -> {
            System.out.println("Enter a user ID and a transfer ID");
            int id = userInput.nextInt();
            String uuid = userInput.next();
            Transaction t = service.removeTransfer(id, UUID.fromString(uuid));
            switch (t.getTransferCategory()) {
              case debits -> {
                if (t.getSender().getIdentifier() == id) {
                  System.out.printf(
                      "Transfer To %s(id = %d) -%d removed\n",
                      t.getRecipient().getName(),
                      t.getRecipient().getIdentifier(),
                      t.getTransferAmount());
                } else {
                  System.out.printf(
                      "Transfer From %s(id = %d) %d removed\n",
                      t.getRecipient().getName(),
                      t.getRecipient().getIdentifier(),
                      t.getTransferAmount());
                }
              }
              case credits -> {
                if (t.getSender().getIdentifier() == id) {
                  System.out.printf(
                      "Transfer From %s(id = %d) %d removed\n",
                      t.getRecipient().getName(),
                      t.getRecipient().getIdentifier(),
                      t.getTransferAmount());
                } else {
                  System.out.printf(
                      "Transfer To %s(id = %d) -%d removed\n",
                      t.getRecipient().getName(),
                      t.getRecipient().getIdentifier(),
                      t.getTransferAmount());
                }
              }
              default -> throw new IllegalStateException(
                  "Unexpected value: " + t.getTransferCategory());
            }
          }
          case 6 -> {
            System.out.println("Check results:");
            for (Transaction t : service.getUnpairedTransactions()) {
              switch (t.getTransferCategory()) {
                case debits -> System.out.printf(
                    "%s(id = %d) has an unacknowledged transfer id = %s from %s(id = %d) for -%d\n",
                    t.getRecipient().getName(),
                    t.getRecipient().getIdentifier(),
                    t.getIdentifier(),
                    t.getSender().getName(),
                    t.getSender().getIdentifier(),
                    t.getTransferAmount());
                case credits -> System.out.printf(
                    "%s(id = %d) has an unacknowledged transfer id = %s from %s(id = %d) for %d\n",
                    t.getRecipient().getName(),
                    t.getRecipient().getIdentifier(),
                    t.getIdentifier(),
                    t.getSender().getName(),
                    t.getSender().getIdentifier(),
                    t.getTransferAmount());
                default -> throw new IllegalStateException(
                    "Unexpected value: " + t.getTransferCategory());
              }
            }
          }
          case 7 -> {
            return;
          }
          default -> throw new IllegalStateException("Unexpected value: " + command);
        }
      } catch (Throwable e) {
        System.out.println(e.getMessage());
      }
      System.out.println("---------------------------------------------------------");
    }
  }
}
