import java.util.UUID;

public class Transaction {
  private final UUID identifier;
  private final User recipient;
  private final User sender;
  private final TransferCategory transferCategory;
  private final Integer transferAmount;

  public Transaction(
      final User user1,
      final User user2,
      final TransferCategory transferCategory,
      final Integer transferAmount)
      throws Throwable {
    switch (transferCategory) {
      case debits -> {
        if (transferAmount < 0) {
          throw new Throwable(
              "Transform amount should be positive be transfer category is debits !");
        }
        sender = user1;
        recipient = user2;
        if (sender.getBalance() < transferAmount) {
          throw new Throwable("The sender doesn't have enough money !");
        }
        recipient.addBalance(transferAmount);
        sender.deductBalance(transferAmount);
      }
      case credits -> {
        if (transferAmount >= 0) {
          throw new Throwable(
              "Transform amount should be negative be transfer category is credits !");
        }
        sender = user2;
        if (sender.getBalance() < transferAmount * -1) {
          throw new Throwable("The sender doesn't have enough money !");
        }
        recipient = user1;
        recipient.addBalance(transferAmount * -1);
        sender.deductBalance(transferAmount * -1);
      }
      default -> throw new Throwable("The wrong transfer category !");
    }
    this.transferCategory = transferCategory;
    this.transferAmount = transferAmount;
    identifier = UUID.randomUUID();
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public User getRecipient() {
    return recipient;
  }

  public User getSender() {
    return sender;
  }

  public TransferCategory getTransferCategory() {
    return transferCategory;
  }

  public Integer getTransferAmount() {
    return transferAmount;
  }

  public enum TransferCategory {
    debits,
    credits
  }
}
