import java.util.UUID;

public interface TransactionsList {
    void add_transaction(Transaction transaction);
    Transaction remove_by_id(UUID id) throws Exception;
    Transaction[] toArray();
}
