import java.util.UUID;

public class Program {
        public static void main(String[] args) {
            try {
                TransactionsService service = new TransactionsService();
                service.add_user("oussama", 1000);
                service.add_user("amir", 5000);
                service.add_user("anass", 999);
                service.send(0, 1, 500);
                service.send(1, 2, 2000);
                service.send(2, 0, 2500);
                for (Transaction t:
                        service.get_unpaired_transactions()) {
                    System.out.println(t.getIdentifier());
                }
                User user = service.users.get_user_by_index(0);
                UUID id = user.transactions.toArray()[0].getIdentifier();
                user.transactions.remove_by_id(id);
                System.out.println("The removed ID :");
                System.out.println(id);
                System.out.println("The invalid ID :");
                for (Transaction t:
                        service.get_unpaired_transactions()) {
                    System.out.println(t.getIdentifier());
                }
            }
            catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }