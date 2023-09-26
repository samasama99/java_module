import java.util.stream.IntStream;

public class Program {

    static Runnable printStringCountTimesFactory(final String str, final int count) {
        return () -> IntStream.range(0, count).forEach((int c) -> System.out.println(str));
    }

    public static void main(final String[] args) throws InterruptedException {
        final int count = Integer.parseInt(args[0]);
        Runnable hen = printStringCountTimesFactory("Hen", count);
        Runnable egg = printStringCountTimesFactory("Egg", count);
        Runnable human = printStringCountTimesFactory("Human", count);

        Thread[] threads =
                new Thread[] {
                    new Thread(hen), new Thread(egg), new Thread(human),
                };

        for (var t : threads) {
            t.start();
        }

        for (var t : threads) {
            t.join();
        }
    }
}
