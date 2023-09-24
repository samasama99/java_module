public class User {
  private static Integer idGenerator = 0;
  private final Integer identifier;
  private final String name;
  private Integer balance;

  public User(final String n, final Integer b) throws Throwable {
    this.name = n;
    if (b < 0) {
      throw new Throwable("The b cant be negative !");
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
