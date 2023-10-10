public class User {
    private static Integer idGenerator = 0;
    private final Integer identifier;
    private final String name;
    private Integer balance;

    public User(final String n, final Integer b) {
        this.name = n;
        if (b < 0) {
            System.out.println("The b cant be negative !");
            System.exit(-1);
        }
        this.balance = b;
        identifier = idGenerator++;
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
