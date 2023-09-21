public class TransactionsService {
    public UsersArrayList users = new UsersArrayList();

    //  • Adding a user
    void add_user(String name, int balance) throws Exception {
        users.add_user(new User(name, balance));
    }

    //  • Retrieving a user’s balance
    User get_user_balance(int id) throws Throwable {
        return users.get_user_by_id(id);
    }

    //  • Performing a transfer transaction (user IDs and transfer amount are specified). In
//      this case, two transactions of DEBIT/CREDIT types are created and added to
//      recipient and sender. IDs of both transactions must be equal
    void send(int sender_id, int recipient_id, int amount) throws Exception {
        new Transaction(users.get_user_by_id(sender_id), users.get_user_by_id(recipient_id), Transaction.TransferCategory.debits, amount);
    }

    //  • Retrieving transfers of a specific user (an ARRAY of transfers is returned).
//      Removing a transaction by ID for a specific user (transaction ID and user ID are
//      specified)
    Transaction[] retrieving_transfers(int id) throws Throwable {
        return this.users.get_user_by_id(id).transactions.toArray();
    }

    //  • Check validity of transactions (returns an ARRAY of unpaired transactions).
    boolean validate(Transaction t, Transaction[] arr) {
        for (Transaction t2 : arr) {
//            System.out.println(t.getIdentifier());
//            System.out.println(t2.getIdentifier());
//            System.out.println("************");
            if (t == t2)
                return true;
        }
        return false;
    }

    Transaction[] get_unpaired_transactions() throws Exception {
        TransactionsLinkedList unpaired = new TransactionsLinkedList();

        for (int i = 0; i < users.size(); i++) {
//            System.out.println(i);
            for (Transaction t : users.get_user_by_index(i).transactions.toArray()) {
                if (t.getRecipient() == users.get_user_by_index(i)) {
                    if (!validate(t, t.getSender().transactions.toArray())) {
                        unpaired.add_transaction(t);
                    }
                } else if (t.getSender() == users.get_user_by_index(i)) {
                    if (!validate(t, t.getRecipient().transactions.toArray())) {
                        unpaired.add_transaction(t);
                    }
                }
            }
        }
        return unpaired.toArray();
    }
}