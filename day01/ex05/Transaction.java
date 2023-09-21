import java.util.UUID;


public class Transaction {
    public enum TransferCategory {debits, credits}

    public Transaction(User user1, User user2, TransferCategory transfer_category, Integer transfer_amount) throws Exception {
        switch (transfer_category) {
            case debits -> {
                if (transfer_amount < 0) throw new Exception("Transform amount should be positive be transfer category is debits !");
                Sender = user1;
                Recipient = user2;
                if (Sender.getBalance() < transfer_amount) {
                    throw new Exception("The sender doesn't have enough money !");
                }
                Recipient.addBalance(transfer_amount);
                Sender.deductBalance(transfer_amount);
                Recipient.transactions.add_transaction(this);
                Sender.transactions.add_transaction(this);
            }
            case credits -> {
                if (transfer_amount >= 0) throw new Exception("Transform amount should be negative be transfer category is credits !");
                Sender = user2;
                if (Sender.getBalance() < transfer_amount * - 1) {
                    throw new Exception("The sender doesn't have enough money !");
                }
                Recipient = user1;
                Recipient.addBalance(transfer_amount * -1);
                Sender.deductBalance(transfer_amount * -1);
                Recipient.transactions.add_transaction(this);
                Sender.transactions.add_transaction(this);
            }
            default -> throw new Exception("The wrong transfer category !");
        }
        Transfer_category = transfer_category;
        Transfer_amount = transfer_amount;
        Identifier = UUID.randomUUID();
    }

    public UUID getIdentifier() {
        return Identifier;
    }

    public User getRecipient() {
        return Recipient;
    }

    public User getSender() {
        return Sender;
    }

    public TransferCategory getTransfer_category() {
        return Transfer_category;
    }

    public Integer getTransfer_amount() {
        return Transfer_amount;
    }

    private final UUID Identifier;
    private final User Recipient;
    private final User Sender;
    private final TransferCategory Transfer_category;
    private final Integer Transfer_amount;
}