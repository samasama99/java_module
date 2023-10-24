import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

import javax.naming.SizeLimitExceededException;

public class Program {

    public static <T> T parseCommandLineArgument(String[] args, String name, Function<String, T> parser) {
        String target = Arrays
                .stream(args)
                .filter(arg -> arg.startsWith("--" + name + "="))
                .findFirst()
                .orElse(null);
        if (target == null)
            throw new NoSuchElementException(name);
        String value = target.substring(name.length() + 3);
        return parser.apply(value);
    }

    static Random getRandomGenerator(final String[] args) {
        try {
            final int seed = parseCommandLineArgument(args, "seed", Integer::parseInt);
            System.out.println("Random" + seed);
            return new Random(seed);
        } catch (NoSuchElementException e) {
            System.out.println("Random");
            return new Random();
        }
    }

    public static void main(final String[] args) {
        try {
            final int size = parseCommandLineArgument(args, "arraySize", Integer::parseInt);
            final int threadCount = parseCommandLineArgument(args, "threadCount", Integer::parseInt);
            final Random random = getRandomGenerator(args);

            if (size > 2_000_000) {
                throw new SizeLimitExceededException("the maximum size of an array is 2'000'000");
            }
            if (threadCount > size) {
                throw new SizeLimitExceededException(
                        "the number of threads is bigger than the number of elements in the given array");
            }

            final int step = (int) Math.ceil((float) size / (float) threadCount);

            final int[] array = IntStream
                    .range(0, size)
                    .map(i -> random.nextInt() % 1000)
                    .toArray();

            List<Thread> threads = new ArrayList<>(threadCount);

            RangeSum[] ranges = new RangeSum[threadCount];

            for (int index = 0; index < threadCount; index++) {
                final int start = index * step;
                final int end = Integer.min(start + step, size);

                final RangeSum rangeSum = ranges[index] = new RangeSum(
                        Arrays.stream(array, start, end),
                        start,
                        end);

                threads.add(new Thread(() -> rangeSum.calculateSum()));
            }

            for (Thread t : threads) {
                t.start();
            }

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
        } catch (NoSuchElementException e) {
            System.out.println("Please provide the argument: " + e.getMessage());
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid argument: " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
