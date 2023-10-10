public class User {
    private final Integer identifier;
    private final String name;
    public TransactionsLinkedList transactions = new TransactionsLinkedList();
    private Integer balance;

    public User(final String newName, final Integer newBalance) throws Exception {
        name = newName;
        if (newBalance < 0) {
            throw new Exception("The newBalance cant be negative !");
        }
        balance = newBalance;
        identifier = UserIdsGenerator.getInstance().generateId();
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

    public void addBalance(Integer transferAmount) {
        balance += transferAmount;
    }

    public void deductBalance(Integer transferAmount) {
        balance -= transferAmount;
    }
}
