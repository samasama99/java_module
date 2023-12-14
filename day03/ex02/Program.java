import javax.naming.SizeLimitExceededException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Program {
    public static <T> Optional<T> parseCommandLineArgument(String[] args, String argName, Function<String, T> parser) {
        return Stream
                .of(args)
                .filter(arg -> arg.startsWith("--" + argName + "="))
                .findFirst()
                .map(arg -> arg.substring(argName.length() + 3))
                .map(parser);
    }


    static Random randomGenerator(Optional<Integer> seed) {
        return seed.map(Random::new).orElseGet(Random::new);
    }

    public static void main(final String[] args) {
        try {
            final Optional<Integer> arraySizeOptional = parseCommandLineArgument(
                    args,
                    "arraySize",
                    Integer::parseInt);
            final Optional<Integer> threadCountOptional = parseCommandLineArgument(
                    args,
                    "threadCount",
                    Integer::parseInt);
            final Optional<Integer> seedOptional = parseCommandLineArgument(
                    args,
                    "seed",
                    Integer::parseInt);

            final int arraySize = arraySizeOptional.orElseThrow(
                    () -> new NoSuchElementException("please provide the size of the array (--arraySize={number})"));
            final int threadCount = threadCountOptional.orElseThrow(
                    () -> new NoSuchElementException(
                            "please provide the number of the thread (--threadCount={number})"));

            final Random random = randomGenerator(seedOptional);

            if (arraySize > 2_000_000 || arraySize <= 0) {
                throw new SizeLimitExceededException("the size should be between 1 and 2_000_000");
            }
            if (threadCount <= 0 || threadCount > arraySize) {
                throw new SizeLimitExceededException(
                        "the number of threads should be between 1 and the size of the array");
            }

            final int step = (int) Math.ceil((float) arraySize / (float) threadCount);

            final int[] array = IntStream
                    .range(0, arraySize)
                    .map(i -> random.nextInt() % 1000)
                    .toArray();

            List<RangeSum> ranges = IntStream.range(0, threadCount)
                    .map(n -> n * step)
                    .mapToObj(start -> new Range(start, Integer.min(start + step, arraySize)))
                    .map(range -> new RangeSum(
                            array,
                            range.start,
                            range.end
                    ))
                    .toList();

            List<Thread> threads = ranges.stream().map(Thread::new).toList();

            threads.forEach(Thread::start);

            System.out.printf("Sum: %d\n", Arrays.stream(array).sum());

            for (Thread t : threads) {
                t.join();
            }

            int index = 0;
            int multiThreadingSum = 0;
            for (RangeSum range : ranges) {
                System.out.printf(
                        "Thread %d: from %d to %d sum is %d\n",
                        index, range.getStart(), range.getEnd() - 1, range.getSum());
                multiThreadingSum += range.getSum();
                index++;
            }
            System.out.printf("Sum by threads: %d\n", multiThreadingSum);
            Stream.zip

        } catch (NoSuchElementException e) {
            System.out.println("Please provide the argument: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid argument: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private record Range(
            int start,
            int end
    ) {
    }
}
