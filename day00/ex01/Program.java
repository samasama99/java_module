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
        System.out.print("-> ");

        int num = 0;

        try {
            num = userInput.nextInt();
        } catch (Exception e) {
            System.out.println("IllegalArgument");
            userInput.close();
            System.exit(-1);
        }

        if (num <= 1) {
            System.out.println("IllegalArgument");
            System.exit(-1);
        }

		int i = 2;
		boolean prime = true;
		while (i < (closeSqrt(num))) {
			if (num % i == 0) {
				prime = false;
				break;
			}
			i++;
		}
		System.out.println(prime + " " + (i - 1));

        userInput.close();
    }
}
