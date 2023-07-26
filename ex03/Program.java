import java.util.Scanner;

public class Program {

  public static void printResult(int nums) {
    if (nums == 0)
      return;
    printResult(nums / 10);
    int num = nums % 10;
    for (int i = 0; i < num; i++) {
      System.out.print("=");
    }
    System.out.println(">");
  }

  public static void main(String[] args) {
    Scanner user_input = new Scanner(System.in);

    int current_week = 0;
    int week_minimal_grade = 0;

    for (int i = 0; i < 18; i++) {
      System.out.print("-> ");
      String week = user_input.next();

      if (week.equals("42")) {
        break;
      }

      int week_num = user_input.nextInt();

      if (!week.equals("Week")) {
        System.err.println("llegalArgument");
        user_input.close();
        return;
      }
      if (current_week >= week_num) {
        System.err.println("llegalArgument");
        user_input.close();
        return;
      }
      current_week = week_num;

      int min = Integer.MAX_VALUE;
      // System.out.println();
      System.out.print("-> ");
      for (int j = 0; j < 5; j++) {
        int num = user_input.nextInt();
        min = min < num ? min : num;
      }
      week_minimal_grade = week_minimal_grade * 10 + min;
    }
    // System.out.println("total " + week_minimal_grade);
    printResult(week_minimal_grade);

    user_input.close();
  }
}
