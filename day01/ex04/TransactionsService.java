import java.util.UUID;

public class TransactionsService {
  private final UsersList users = new UsersArrayList();

  public UsersList getUsers() {
    return users;
  }

  //  • Adding a user
  void addUser(final String name, final int balance) throws Exception {
    users.addUser(new User(name, balance));
  }

  //  • Retrieving a user’s balance
  int getUserBalance(final int id) throws Exception {
    return users.getUserById(id).getBalance();
  }

  //  • Performing a transfer transaction (user IDs and transfer amount are specified). In
  //      this case, two transactions of DEBIT/CREDIT types are created and added to
  //      recipient and sender. IDs of both transactions must be equal
  void send(final int senderId, final int recipientId, final int amount) throws Exception {
    new Transaction(
        users.getUserById(senderId),
        users.getUserById(recipientId),
        Transaction.TransferCategory.debits,
        amount);
  }

  //  • Retrieving transfers of a specific user (an ARRAY of transfers is returned).
  //      Removing a transaction by ID for a specific user (transaction ID and user ID are
  //      specified)
  Transaction[] retrievingTransfers(final int id) throws Throwable {
    return this.users.getUserById(id).getTransactions().toArray();
  }

  void removeTransfer(final int id, final UUID tid) throws Exception {
    users.getUserById(id).getTransactions().removeById(tid);
  }

  //  • Check validity of transactions (returns an ARRAY of unpaired transactions).
  boolean invalid(final Transaction t, final Transaction[] arr) {
    for (Transaction t2 : arr) {
      if (t == t2) {
        return false;
      }
    }
    return true;
  }

  Transaction[] getUnpairedTransactions() throws Exception {
    TransactionsLinkedList unpaired = new TransactionsLinkedList();

    for (int i = 0; i < users.size(); i++) {
      for (Transaction t : users.getUserByIndex(i).getTransactions().toArray()) {
        if (t.getRecipient() == users.getUserByIndex(i)) {
          if (invalid(t, t.getSender().getTransactions().toArray())) {
            unpaired.addTransaction(t);
          }
        } else if (t.getSender() == users.getUserByIndex(i)) {
          if (invalid(t, t.getRecipient().getTransactions().toArray())) {
            unpaired.addTransaction(t);
          }
        }
      }
    }
    return unpaired.toArray();
  }
}
