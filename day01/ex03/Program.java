class Program {
  public static void main(final String[] args) {
    try {
      TransactionsLinkedList list = new TransactionsLinkedList();
      User u1 = (new User("oussama", 5000));
      User u2 = (new User("anass", 200));
      User u3 = (new User("ayoub", 0));
      Transaction t1 = new Transaction(u1, u2, Transaction.TransferCategory.debits, 1000);
      Transaction t2 = new Transaction(u1, u2, Transaction.TransferCategory.debits, 550);
      Transaction t3 = new Transaction(u1, u2, Transaction.TransferCategory.debits, 355);
      Transaction t4 = new Transaction(u2, u3, Transaction.TransferCategory.debits, 1000);
      list.addTransaction(t1);
      list.addTransaction(t2);
      list.addTransaction(t3);
      Transaction[] arr = list.toArray();
      for (Transaction a : arr) {
        System.out.println(a.getIdentifier().toString());
      }
      System.out.println("****");
      list.removeById(t2.getIdentifier());
      Transaction[] arr2 = list.toArray();
      for (Transaction a : arr2) {
        System.out.println(a.getIdentifier().toString());
      }
      list.addTransaction(t2);
      list.removeById(t1.getIdentifier());
      list.removeById(t3.getIdentifier());

      System.out.println("****");
      Transaction[] arr3 = list.toArray();
      for (Transaction a : arr3) {
        System.out.println(a.getIdentifier().toString());
      }
      System.out.println("****");
      Transaction[] arr4 = u1.transactions.toArray();
      for (Transaction a : arr4) {
        printTransaction(a);
      }
      System.out.println("****");
      Transaction[] arr5 = u2.transactions.toArray();
      for (Transaction a : arr5) {
        printTransaction(a);
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void printTransaction(final Transaction t) {
    if (t.getTransferCategory() == Transaction.TransferCategory.debits) {
      System.out.printf(
          "%-10s -> %-10s, %10d,%-7s, %-10s\n",
          t.getSender().getName(),
          t.getRecipient().getName(),
          -t.getTransferAmount(),
          "OUTCOME",
          t.getIdentifier().toString());
      System.out.printf(
          "%-10s -> %-10s, %10d,%-7s, %-10s\n",
          t.getRecipient().getName(),
          t.getSender().getName(),
          t.getTransferAmount(),
          "INCOME",
          t.getIdentifier().toString());
    } else {
      System.out.printf(
          "%-10s -> %-10s, %10d,%-7s, %-10s\n",
          t.getSender().getName(),
          t.getRecipient().getName(),
          -t.getTransferAmount(),
          "INCOME",
          t.getIdentifier().toString());
      System.out.printf(
          "%-10s -> %-10s, %10d,%-7s, %-10s\n",
          t.getRecipient().getName(),
          t.getSender().getName(),
          t.getTransferAmount(),
          "OUTCOME",
          t.getIdentifier().toString());
    }
  }
}
