import java.util.UUID;

public interface TransactionsList {
  void addTransaction(Transaction transaction);

  Transaction removeById(UUID id) throws Exception;

  Transaction[] toArray();
}
