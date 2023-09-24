public class Program {
  static final int MAX = 479598;
  static final int BASE = 10;
  public static void main(final String[] args) {
    int num = MAX;
    int total = 0;

    while (num != 0) {
      total += num % BASE;
      num /= BASE;
    }
    System.out.println(total);
  }
}
