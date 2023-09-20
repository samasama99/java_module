import java.util.UUID;

public interface TransactionsList {
// add a transaction
// remove a transaction by id (in this case, uuid string identifier is used)
// Tansactionslistansform into array (ex. transaction[ ] toarray())

    void add_transaction(Transaction transaction);
    void remove_by_id(UUID id);
    Transaction[] toArray();
}
