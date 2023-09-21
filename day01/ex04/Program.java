import java.util.UUID;

public class Program {
        public static void main(String[] args) {
            try {
                TransactionsService service = new TransactionsService();
                service.add_user("oussama", 1000);
                service.add_user("amir", 5000);
                service.add_user("anass", 999);

                System.out.printf("name: %-10s balance: %10d\n", service.users.get_user_by_id(0).getName(), service.get_user_balance(0));
                System.out.printf("name: %-10s balance: %10d\n", service.users.get_user_by_id(1).getName(), service.get_user_balance(1));
                System.out.printf("name: %-10s balance: %10d\n", service.users.get_user_by_id(2).getName(), service.get_user_balance(2));
                System.out.println("*****************");

                service.send(0, 1, 500);
                service.send(1, 2, 2000);
                service.send(2, 0, 2500);
                service.send(2, 0, 100);
                for (Transaction t:
                        service.get_unpaired_transactions()) {
                    System.out.println(t.getIdentifier());
                }
                {
                    UUID id = service.users.get_user_by_id(0).transactions.toArray()[0].getIdentifier();
                    service.remove_transfer(0, id);
                    System.out.println("The removed ID :");
                    System.out.println(id);
                }
                {
                    UUID id = service.users.get_user_by_id(2).transactions.toArray()[2].getIdentifier();
                    service.remove_transfer(2, id);
                    System.out.println("The removed ID :");
                    System.out.println(id);
                }
                System.out.println("The invalid ID :");
                for (Transaction t:
                        service.get_unpaired_transactions()) {
                    System.out.printf("%s sender: %-10s recipient: %-10s\n", t.getIdentifier().toString(), t.getSender().getName(), t.getRecipient().getName());
                }
                System.out.println("*****************");
                System.out.printf("name: %-10s balance: %10d\n", service.users.get_user_by_id(0).getName(), service.get_user_balance(0));
                System.out.printf("name: %-10s balance: %10d\n", service.users.get_user_by_id(1).getName(), service.get_user_balance(1));
                System.out.printf("name: %-10s balance: %10d\n", service.users.get_user_by_id(2).getName(), service.get_user_balance(2));
            }
            catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }