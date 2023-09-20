class Program {
    public static void main(String[] args) {
        try {
            TransactionsLinkedList list = new TransactionsLinkedList();
            User u1 = (new User("oussama", 5000));
            User u2 = (new User("anass", 200));
            Transaction t1 = new Transaction(u1, u2, Transaction.TransferCategory.debits, 1000);
            Transaction t2 = new Transaction(u1, u2, Transaction.TransferCategory.debits, 550);
            Transaction t3 = new Transaction(u1, u2, Transaction.TransferCategory.debits, 355);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}