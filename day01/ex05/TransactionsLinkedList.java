import java.util.UUID;

public class TransactionsLinkedList implements TransactionsList {
  private int size = 0;
  private TransactionNode head;
  private TransactionNode tail;

  public void addTransaction(Transaction transaction) {
    if (head == null) {
      head = new TransactionNode();
      tail = head;
      head.value = transaction;
    } else {
      tail.next = new TransactionNode();
      tail.next.value = transaction;
      tail.next.previous = tail;
      tail = tail.next;
    }
    size++;
  }

  public Transaction removeById(UUID id) throws Exception {
    Transaction ret = null;
    if (head.value.getIdentifier() == id) {
      head = head.next;
      if (head != null) {
        head.previous = null;
      } else {
        tail = null;
      }
      size--;
    } else {
      TransactionNode tmp = head;
      while (tmp != null && !tmp.value.getIdentifier().equals(id)) {
        tmp = tmp.next;
      }
      if (tmp != null) {
        ret = tmp.value;
        if (tmp.previous != null) {
          tmp.previous.next = tmp.next;
        }
        if (tmp.next != null) {
          tmp.next.previous = tmp.previous;
        }
        size--;
      } else {
        throw new Exception("TransactionNotFoundException");
      }
    }
    return ret;
  }

  public Transaction[] toArray() {
    Transaction[] arr = new Transaction[size];
    TransactionNode tmp = head;
    int i = 0;
    while (tmp != null) {
      arr[i++] = tmp.value;
      tmp = tmp.next;
    }
    return arr;
  }

  class TransactionNode {
    private Transaction value;
    private TransactionNode next;
    private TransactionNode previous;
  }
}
