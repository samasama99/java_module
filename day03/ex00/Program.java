import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Program {

    public static <T> Optional<T> parseCommandLineArgument(String[] args, String name, Function<String, T> parser) {
        return Stream
                .of(args)
                .filter(arg -> arg.startsWith("--" + name + "="))
                .findFirst()
                .map(arg -> arg.substring(name.length() + 3))
                .map(parser);
    }

    static Runnable printStringCountTimesFactory(final String str, final int count) {
        return () -> IntStream
                .range(0, count)
                .forEach(c -> System.out.println(str));
    }

    public static void main(final String[] args) {
        try {
            final int count = parseCommandLineArgument(args, "count", Integer::parseInt)
                    .orElseThrow(() -> new NoSuchElementException("Provide the count option (--count={int})"));

            if (count < 0) {
                throw new Exception(
                        "Count can't be negative!");
            }

            Runnable hen = printStringCountTimesFactory("Hen", count);
            Runnable egg = printStringCountTimesFactory("Egg", count);

            var threads = List.of(new Thread(hen), new Thread(egg));

            for (var thread : threads) {
                thread.start();
            }

            for (var thread : threads) {
                thread.join();
            }

            IntStream.range(0, count).forEach(c -> System.out.println("human"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}