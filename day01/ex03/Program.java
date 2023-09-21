class Program {
    public static void main(String[] args) {
        try {
            TransactionsLinkedList list = new TransactionsLinkedList();
            User u1 = (new User("oussama", 5000));
            User u2 = (new User("anass", 200));
            User u3 = (new User("ayoub", 0));
            Transaction t1 = new Transaction(u1, u2, Transaction.TransferCategory.debits, 1000);
            Transaction t2 = new Transaction(u1, u2, Transaction.TransferCategory.debits, 550);
            Transaction t3 = new Transaction(u1, u2, Transaction.TransferCategory.debits, 355);
            Transaction t4 = new Transaction(u2, u3, Transaction.TransferCategory.debits, 1000);
            list.add_transaction(t1);
            list.add_transaction(t2);
            list.add_transaction(t3);
            Transaction[] arr = list.toArray();
            for (Transaction a:
                 arr) {
                System.out.println(a.getIdentifier().toString());
            }
            System.out.println("****");
            list.remove_by_id(t2.getIdentifier());
            Transaction[] arr2 = list.toArray();
            for (Transaction a:
                    arr2) {
                System.out.println(a.getIdentifier().toString());
            }
            list.add_transaction(t2);
            list.remove_by_id(t1.getIdentifier());
            list.remove_by_id(t3.getIdentifier());

            System.out.println("****");
            Transaction[] arr3 = list.toArray();
            for (Transaction a:
                    arr3) {
                System.out.println(a.getIdentifier().toString());
            }
            System.out.println("****");
            Transaction[] arr4  = u1.transactions.toArray();
            for (Transaction a:
                    arr4) {
                printTransaction(a);
            }
            System.out.println("****");
            Transaction[] arr5  = u2.transactions.toArray();
            for (Transaction a:
                    arr5) {
                printTransaction(a);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private static void printTransaction(Transaction t) {
        if (t.getTransfer_category() == Transaction.TransferCategory.debits) {
            System.out.printf("%-10s -> %-10s, %10d,%-7s, %-10s\n", t.getSender().getName(), t.getRecipient().getName(), -t.getTransfer_amount(),"OUTCOME", t.getIdentifier().toString());
            System.out.printf("%-10s -> %-10s, %10d,%-7s, %-10s\n", t.getRecipient().getName(), t.getSender().getName(), t.getTransfer_amount(),"INCOME", t.getIdentifier().toString());
        } else {
            System.out.printf("%-10s -> %-10s, %10d,%-7s, %-10s\n", t.getSender().getName(), t.getRecipient().getName(), -t.getTransfer_amount(),"INCOME", t.getIdentifier().toString());
            System.out.printf("%-10s -> %-10s, %10d,%-7s, %-10s\n", t.getRecipient().getName(), t.getSender().getName(), t.getTransfer_amount(),"OUTCOME", t.getIdentifier().toString());
        }
    }
}