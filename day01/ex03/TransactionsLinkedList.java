import java.util.UUID;

class TransactionNode {
    Transaction value;
    TransactionNode next;
}

public class TransactionsLinkedList implements TransactionsList {
    int size = 0;
    TransactionNode head;

    public void add_transaction(Transaction transaction) {
        if (head == null) {
            head = new TransactionNode();
            head.value = transaction;
            head.next = null;
        } else {
            TransactionNode tmp = head;
            while (tmp.next != null) {
                tmp = tmp.next;
            }
            tmp.next = new TransactionNode();
            tmp.next.value = transaction;
            tmp.next.next = null;
        }
        size++;
    }

    public void remove_by_id(UUID id) {
        if (head.value.getIdentifier() == id) {
            head = head.next;
        } else {
            TransactionNode tmp = head;
            while (tmp != null && !tmp.value.getIdentifier().equals(id)) {
                tmp = tmp.next;
            }
            TransactionNode tmp2 = head;
            if (tmp != null) {
                while (tmp2.next != tmp) {
                    tmp2 = tmp2.next;
                }
                tmp2.next = tmp.next;
                size--;
            }
        }
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
}
