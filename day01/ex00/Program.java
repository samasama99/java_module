class Program {
    public static void main(final String[] args) {
        User u1 = new User("oussama", 500);
        User u2 = new User("anass", 0);

        System.out.printf("%-5d %-10s %-10d\n", u1.getIdentifier(), u1.getName(), u1.getBalance());
        System.out.printf("%-5d %-10s %-10d\n", u2.getIdentifier(), u2.getName(), u2.getBalance());
        Transaction t = new Transaction(u1, u2, Transaction.TransferCategory.debits, 500);
        printTransaction(t);
        System.out.printf("%-5d %-10s %-10d\n", u1.getIdentifier(), u1.getName(), u1.getBalance());
        System.out.printf("%-5d %-10s %-10d\n", u2.getIdentifier(), u2.getName(), u2.getBalance());
        Transaction t2 = new Transaction(u1, u2, Transaction.TransferCategory.credits, -320);
        printTransaction(t2);
        System.out.printf("%-5d %-10s %-10d\n", u1.getIdentifier(), u1.getName(), u1.getBalance());
        System.out.printf("%-5d %-10s %-10d\n", u2.getIdentifier(), u2.getName(), u2.getBalance());
    }

    private static void printTransaction(final Transaction t) {
        if (t.getTransferCategory() == Transaction.TransferCategory.debits) {
            System.out.printf(
                    "%-10s -> %-10s, %10d, %-7s, %-10s\n",
                    t.getSender().getName(),
                    t.getRecipient().getName(),
                    -t.getTransferAmount(),
                    "OUTCOME",
                    t.getIdentifier().toString());
            System.out.printf(
                    "%-10s -> %-10s, %10d, %-7s, %-10s\n",
                    t.getRecipient().getName(),
                    t.getSender().getName(),
                    t.getTransferAmount(),
                    "INCOME",
                    t.getIdentifier().toString());
        } else {
            System.out.printf(
                    "%-10s -> %-10s, %10d, %-7s, %-10s\n",
                    t.getSender().getName(),
                    t.getRecipient().getName(),
                    -t.getTransferAmount(),
                    "INCOME",
                    t.getIdentifier().toString());
            System.out.printf(
                    "%-10s -> %-10s, %10d, %-7s, %-10s\n",
                    t.getRecipient().getName(),
                    t.getSender().getName(),
                    t.getTransferAmount(),
                    "OUTCOME",
                    t.getIdentifier().toString());
        }
    }
}
