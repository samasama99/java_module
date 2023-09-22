import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class Program {
    public static void main(String[] args) {

        Scanner user_input = new Scanner(System.in);
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
                        if (!user_input.hasNextInt()) {
                            throw new InputMismatchException("Please enter a valid command");
                        }
                    }
                    catch (InputMismatchException e) {
                        System.out.println("Error: " + e.getMessage());
                        System.out.println("Please try again.");
                        user_input.nextLine();
                        System.out.print("-> ");
                    }
                    command = user_input.nextInt();
                    if (command >= 1 && command <= 7) {
                        validInput = true;
                    } else {
                        System.out.println("Error: " + "Please enter a valid command");
                        System.out.println("Please try again.");
                        user_input.nextLine();
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
                                if (!user_input.hasNext("[a-zA-Z]+")) {
                                    throw new InputMismatchException("Please enter a valid name");
                                }

                                name = user_input.next();

                                if (!user_input.hasNextInt()) {
                                    throw new InputMismatchException("Please enter a valid amount");
                                }

                                balance = user_input.nextInt();

                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Error: " + e.getMessage());
                                System.out.println("Please try again.");
                                user_input.nextLine();
                                System.out.print("-> ");
                            }
                        }
                        User user = service.add_user(name, balance);
                        System.out.printf("User with id = %d is added", user.getIdentifier());

                    }
                    case 2 -> {
                        System.out.println("Enter a user ID");
                        System.out.print("-> ");
                        int id = 0;
                        validInput = false;
                        while (!validInput) {
                            try {
                                if (!user_input.hasNextInt()) {
                                    throw new InputMismatchException("Please enter a valid id");
                                }

                                id = user_input.nextInt();

                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Error: " + e.getMessage());
                                System.out.println("Please try again.");
                                user_input.nextLine();
                                System.out.print("-> ");
                            }
                        }


                        System.out.printf("%s - %d\n", service.users.get_user_by_id(id).getName(), service.get_user_balance(id));
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
                                if (!user_input.hasNextInt()) {
                                    throw new InputMismatchException("Please enter a valid id1");
                                }
                                id1 = user_input.nextInt();
                                if (!user_input.hasNextInt()) {
                                    throw new InputMismatchException("Please enter a valid id2");
                                }
                                id2 = user_input.nextInt();
                                if (!user_input.hasNextInt()) {
                                    throw new InputMismatchException("Please enter a valid amount");
                                }
                                amount = user_input.nextInt();

                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Error: " + e.getMessage());
                                System.out.println("Please try again.");
                                user_input.nextLine();
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
                                if (!user_input.hasNextInt()) {
                                    throw new InputMismatchException("Please enter a valid id");
                                }

                                id = user_input.nextInt();

                                validInput = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Error: " + e.getMessage());
                                System.out.println("Please try again.");
                                user_input.nextLine();
                                System.out.print("-> ");
                            }
                        }
                        for (Transaction t : service.retrieving_transfers(id)) {
                            switch (t.getTransfer_category())  {
                                case debits -> {
                                    if (t.getSender().getIdentifier() == id) {
                                        System.out.printf("To %s(id = %d) -%d with id = %s\n", t.getRecipient().getName(), t.getRecipient().getIdentifier(), t.getTransfer_amount(), t.getIdentifier());
                                    } else {
                                        System.out.printf("From %s(id = %d) %d with id = %s\n", t.getSender().getName(), t.getSender().getIdentifier(), t.getTransfer_amount(), t.getIdentifier());
                                    }
                                }
                                case credits -> {
                                    if (t.getSender().getIdentifier() == id) {
                                        System.out.printf("From %s(id = %d) %d with id = %s\n", t.getRecipient().getName(), t.getRecipient().getIdentifier(), t.getTransfer_amount(), t.getIdentifier());
                                    } else {
                                        System.out.printf("To %s(id = %d) -%d with id = %s\n", t.getSender().getName(), t.getSender().getIdentifier(), t.getTransfer_amount(), t.getIdentifier());
                                    }
                                }
                            }
                        }
                    }
                    case 5 -> {
                        System.out.println("Enter a user ID and a transfer ID");
                        int id = user_input.nextInt();
                        String uuid = user_input.next();
                        Transaction t =  service.remove_transfer(id, UUID.fromString(uuid));
                        switch (t.getTransfer_category())  {
                            case debits -> {
                                if (t.getSender().getIdentifier() == id) {
                                    System.out.printf("Transfer To %s(id = %d) -%d removed\n", t.getRecipient().getName(), t.getRecipient().getIdentifier(), t.getTransfer_amount());
                                } else {
                                    System.out.printf("Transfer From %s(id = %d) %d removed\n", t.getRecipient().getName(), t.getRecipient().getIdentifier(), t.getTransfer_amount());
                                }
                            }
                            case credits -> {
                                if (t.getSender().getIdentifier() == id) {
                                    System.out.printf("Transfer From %s(id = %d) %d removed\n", t.getRecipient().getName(), t.getRecipient().getIdentifier(), t.getTransfer_amount());
                                } else {
                                    System.out.printf("Transfer To %s(id = %d) -%d removed\n", t.getRecipient().getName(), t.getRecipient().getIdentifier(), t.getTransfer_amount());
                                }
                            }
                        }
                    }
                    case 6 -> {
                        System.out.println("Check results:");
                        for (Transaction t : service.get_unpaired_transactions()) {
                            switch (t.getTransfer_category()) {
                                case debits -> {
                                    System.out.printf("%s(id = %d) has an unacknowledged transfer id = %s from %s(id = %d) for -%d\n", t.getRecipient().getName(), t.getRecipient().getIdentifier(), t.getIdentifier(), t.getSender().getName(), t.getSender().getIdentifier(), t.getTransfer_amount());
                                }
                                case credits -> {
                                    System.out.printf("%s(id = %d) has an unacknowledged transfer id = %s from %s(id = %d) for %d\n", t.getRecipient().getName(), t.getRecipient().getIdentifier(), t.getIdentifier(), t.getSender().getName(), t.getSender().getIdentifier(), t.getTransfer_amount());
                                }
                            }
                        }
                    }
                    case 7 -> {
                        return;
                    }
                }
            } catch (Throwable e) {
                System.out.println(e.getMessage());
            }
            System.out.println("---------------------------------------------------------");
        }

    }
}

//$> java Program --profile=dev
//        1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 1
//        Enter a user name and a balance
//        -> Jonh 777
//        User with id = 1 is added
//        ---------------------------------------------------------
//1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 1
//        Enter a user name and a balance
//        -> Mike 100
//        User with id = 2 is added
//        ---------------------------------------------------------
//        1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 3
//        Enter a sender ID, a recipient ID, and a transfer amount
//        -> 1 2 100
//        The transfer is completed
//        ---------------------------------------------------------
//        1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 3
//        Enter a sender ID, a recipient ID, and a transfer amount
//        -> 1 2 150
//        The transfer is completed
//        ---------------------------------------------------------
//        1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 3
//        Enter a sender ID, a recipient ID, and a transfer amount
//        -> 1 2 50
//        The transfer is completed
//        ---------------------------------------------------------
//        1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 2
//        Enter a user ID
//        -> 2
//        Mike - 400
//        ---------------------------------------------------------
//        1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 4
//        Enter a user ID
//        -> 1
//        To Mike(id = 2) -100 with id = cc128842-2e5c-4cca-a44c-7829f53fc31f
//        To Mike(id = 2) -150 with id = 1fc852e7-914f-4bfd-913d-0313aab1ed99
//        TO Mike(id = 2) -50 with id = ce183f49-5be9-4513-bd05-8bd82214eaba
//---------------------------------------------------------
//        1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 5
//        Enter a user ID and a transfer ID
//        -> 1 1fc852e7-914f-4bfd-913d-0313aab1ed99
//        Transfer To Mike(id = 2) 150 removed
//        ---------------------------------------------------------
//        1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 6
//        Check results:
//        Mike(id = 2) has an unacknowledged transfer id = 1fc852e7-914f-4bfd-913d-0313aab1ed99 from John(id =
//        1) for 150
//        ---------------------------------------------------------
//        1. Add a user
//        2. View user balances
//        3. Perform a transfer
//        4. View all transactions for a specific user
//        5. DEV - remove a transfer by ID
//        6. DEV - check transfer validity
//        7. Finish execution
//        -> 7
//        $>