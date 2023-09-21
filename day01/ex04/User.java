public class User {
    private final Integer Identifier;
    public TransactionsLinkedList transactions = new TransactionsLinkedList();

    public Integer getIdentifier() {
        return Identifier;
    }

    public String getName() {
        return Name;
    }

    public Integer getBalance() {
        return Balance;
    }

    private final String Name;
    private Integer Balance;

    public User(String name, Integer balance) throws Exception {
        Name = name;
        if (balance < 0) throw new Exception("The balance cant be negative !");
        Balance = balance;
        Identifier = UserIdsGenerator.getInstance().generateId();
    }

    public void addBalance(Integer transferAmount) {
        Balance += transferAmount;
    }

    public void deductBalance(Integer transferAmount) {
        Balance -= transferAmount;
    }
}
