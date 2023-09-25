public class User {
  private final Integer identifier;
  private final TransactionsLinkedList transactions = new TransactionsLinkedList();
  private final String name;
  private Integer balance;

  public User(final String newName, final Integer newBalance) throws Exception {
    name = newName;
    if (newBalance < 0) {
      throw new Exception("The balance cant be negative !");
    }
    this.balance = newBalance;
    identifier = UserIdsGenerator.getInstance().generateId();
  }

  public TransactionsLinkedList getTransactions() {
    return transactions;
  }

  public Integer getIdentifier() {
    return identifier;
  }

  public String getName() {
    return name;
  }

  public Integer getBalance() {
    return balance;
  }

  public void addBalance(final Integer transferAmount) {
    balance += transferAmount;
  }

  public void deductBalance(final Integer transferAmount) {
    balance -= transferAmount;
  }
}
