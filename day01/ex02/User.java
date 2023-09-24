public class User {
  private final Integer identifier;
  private final String name;
  private Integer balance;

  public User(final String n, final Integer b) throws Exception {
    name = n;
    if (b < 0) {
      throw new Exception("The b cant be negative !");
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
