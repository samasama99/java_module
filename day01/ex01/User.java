public class User {
    private final String name;
    private Integer identifier;
    private Integer balance;

    public User(final String n, final Integer b) {
        name = n;
        if (b < 0) {
            System.out.println("The b cant be negative !");
            System.exit(-1);
            return;
        }
        balance = b;
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
