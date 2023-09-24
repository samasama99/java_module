import java.util.Scanner;

public class Program {
    public static void main(final String[] args) {
        Scanner userInput = new Scanner(System.in);
        System.out.print("-> ");
        int num;
        try {
            num = userInput.nextInt();
        } catch (Exception e) {
            System.out.println("IllegalArgument");
            userInput.close();
            return;
        }
        if (num <= 1) {
            System.out.println("IllegalArgument");
        } else {
            int iteration = 2;
            boolean prime = true;
            while (iteration < num / 2) {
                if (num % iteration == 0) {
                    prime = false;
                    break;
                }
                iteration++;
            }
            System.out.println(prime + " " + (iteration - 1));
        }

        userInput.close();
    }
}
