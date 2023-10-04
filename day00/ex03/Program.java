import java.util.Scanner;

public class Program {

  static final int BASE = 10;

  public static void printResult(final long nums) {
    if (nums == 0) {
      return;
    }

    printResult(nums / BASE);
    long num = nums % BASE;
    for (int i = 0; i < num; i++) {
      System.out.print("=");
    }

    System.out.println(">");
  }

  public static void main(final String[] args) {
    Scanner userInput = new Scanner(System.in);

    int currentWeek = 0;
    long weekMinimalGrade = 0;

    for (int i = 0; i < 18; i++) {
      String week = userInput.next();

      if (week.equals("42")) {
        break;
      }

      int weekNum = userInput.nextInt();

      if (!week.equals("Week")) {
        System.err.println("illegalArgument");
        userInput.close();
        System.exit(-1);
        return;
      }

      if (weekNum <= currentWeek) {
        System.err.println("illegalArgument");
        userInput.close();
        System.exit(-1);
        return;
      }

      currentWeek = weekNum;

      int min = 9;

      for (int j = 0; j < 5; j++) {
        int num = userInput.nextInt();

        if (num > 9 || num < 1) {
          System.err.println("illegalArgument");
          userInput.close();
          System.exit(-1);
          return;
        }

        min = min < num ? min : num;
      }

      weekMinimalGrade = weekMinimalGrade * 10 + min;
    }

    printResult(weekMinimalGrade);

    userInput.close();
  }
}
