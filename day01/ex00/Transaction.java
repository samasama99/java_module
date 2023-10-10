import java.util.UUID;

public class Transaction {
    UUID identifier = null;
    User recipient = null;
    User sender = null;
    TransferCategory transferCategory = null;
    Integer transferAmount = 0;

    public Transaction(
            final User user1,
            final User user2,
            final TransferCategory transferCategory,
            final Integer transferAmount) {
        switch (transferCategory) {
            case debits -> {
                if (transferAmount < 0) {
                    System.out.println(
                            "Transform amount should be positive be transfer category is debits !");
                    System.exit(-1);
                    return;
                }
                sender = user1;
                recipient = user2;
                if (sender.getBalance() < transferAmount) {
                    System.out.println("The sender doesn't have enough money !");
                    System.exit(-1);
                    return;
                }
                recipient.addBalance(transferAmount);
                sender.deductBalance(transferAmount);
            }
            case credits -> {
                if (transferAmount >= 0) {
                    System.out.println(
                            "Transform amount should be negative be transfer category is credits !");
                    System.exit(-1);
                    return;
                }
                sender = user2;
                if (sender.getBalance() < transferAmount * -1) {
                    System.out.println("The sender doesn't have enough money !");
                    System.exit(-1);
                    return;
                }
                recipient = user1;
                recipient.addBalance(transferAmount * -1);
                sender.deductBalance(transferAmount * -1);
            }
            default -> {
                System.out.println("The wrong transfer category !");
                System.exit(-1);
                return;
            }
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
