import java.util.Scanner;

public class Program {
  public static void printResult(final long nums) {
    if (nums == 0) {
      return;
    }

    printResult(nums / 10);

    long num = nums % 10;

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
      System.out.print("-> ");
      String week = userInput.next();

      if (week.equals("42")) {
        break;
      }

      int weekNum = userInput.nextInt();

      if (!week.equals("Week")) {
        System.err.println("illegalArgument");
        userInput.close();
        return;
      }

      if (currentWeek >= weekNum) {
        System.err.println("illegalArgument");
        userInput.close();
        return;
      }

      currentWeek = weekNum;

      int min = Integer.MAX_VALUE;
      System.out.print("-> ");
      for (int j = 0; j < 5; j++) {
        int num = userInput.nextInt();
        min = min < num ? min : num;
      }
      weekMinimalGrade = weekMinimalGrade * 10 + min;
    }

    printResult(weekMinimalGrade);

    userInput.close();
  }
}
