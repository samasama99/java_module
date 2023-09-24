import java.util.Scanner;

public class Program {

    static final int END = 42;
    static final int BASE = 10;

    public static void main(final String[] args) {
        int num;
        int query = 0;
        Scanner userInput = new Scanner(System.in);

        while (true) {
            System.out.print("-> ");

            try {
                num = userInput.nextInt();
            } catch (Exception e) {
                break;
            }

            if (num == END) {
                break;
            }

            int total = 0;
            while (num != 0) {
                total += num % BASE;
                num /= BASE;
            }

            int iteration = 2;
            boolean prime = true;
            while (iteration < total / 2) {
                if (total % iteration == 0) {
                    prime = false;
                    break;
                }
                iteration++;
            }

            if (prime) {
                query++;
            }
        }
        System.out.println("Count of coffee-request : " + query);
        userInput.close();
    }
}
