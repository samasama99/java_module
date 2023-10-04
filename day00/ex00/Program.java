public class Program {

  public static void main(final String[] args) {
    int num = 479598;
    int total = (num / 1) % 10 +
        (num / 10) % 10 +
        (num / 100) % 10 +
        (num / 1000) % 10 +
        (num / 10000) % 10 +
        (num / 100000) % 10;
    System.out.println(total);
  }

}
