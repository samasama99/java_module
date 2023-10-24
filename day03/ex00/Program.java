import java.util.stream.IntStream;

public class Program {
    static Runnable printStringCountTimesFactory(final String str, final int count) {
        return () -> IntStream.range(0, count).forEach((int c) -> System.out.println(str));
    }

    public static void main(final String[] args) throws InterruptedException {
        if (args.length < 1 || !args[0].startsWith("--count=")) {
            System.out.println("Please provide the count option!");
            System.exit(-1);
        }
        try {
            String value = args[0].substring("--count=".length());
            final int count = Integer.parseInt(value);
            Runnable hen = printStringCountTimesFactory("Hen", count);
            Runnable egg = printStringCountTimesFactory("Egg", count);

            Thread[] threads = new Thread[] {
                    new Thread(hen), new Thread(egg)
            };

            for (var t : threads) {
                t.start();
            }

            for (var t : threads) {
                t.join();
            }

            IntStream.range(0, count).forEach((int c) -> System.out.println("human"));
        } catch (Exception e) {
            System.out.println("Please provide a valid count option!");
            System.exit(-1);
        }
    }
}