import java.util.Scanner;

public class Program {
	static int closeSqrt(int x) {
		int a = 1;
		while (x > a * a) {
			a++;
		}
		return a;
	}

    public static void main(final String[] args) {

        Scanner userInput = new Scanner(System.in);

        int num;

        if (userInput.hasNextInt()) {
            num = userInput.nextInt();
        } else {
            System.out.println("IllegalArgument");
            userInput.close();
            System.exit(-1);
            return;
        }

        if (num <= 1) {
            System.out.println("IllegalArgument");
            userInput.close();
            System.exit(-1);
            return;
        }

        int iteration = 0;
        int divisor = 2;
        boolean prime = true;
        while (divisor * divisor <= num) {
            iteration++;
            if (num % divisor == 0) {
                prime = false;
                break;
            }
            divisor++;
        }

        System.out.println(prime + " " + iteration);

        userInput.close();
    }
}
