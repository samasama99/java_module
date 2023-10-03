public class Program {
  static final int INPUT = 479598;
  static final int BASE = 10;
  public static void main(final String[] args) {
    int num = INPUT;
    int total = 0;

    while (num != 0) {
      total += num % BASE;
      num /= BASE;
    }
    System.out.println(total);
  }
}
