import java.util.Scanner;

/**
 * Program
 */
public class Program {

    public static void main(String[] args) {
        int num = 0;
        int querie = 0;
        Scanner user_input = new Scanner(System.in);

        while (true) {
            System.out.print("-> ");
            
            try {
                num = user_input.nextInt();
            } catch (Exception e) {
                break;
            }

            if (num == 42)
                break;

            int total = 0;
            while (num != 0) {
                total += num % 10;
                num /= 10;
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

            if (prime)
                querie++;
        }
        System.out.println("Count of coffee-request : " + querie);
        user_input.close();
    }
}