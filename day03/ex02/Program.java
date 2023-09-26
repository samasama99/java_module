import java.util.*;
import java.util.stream.IntStream;

public class Program {
    public static void main(final String[] args) throws InterruptedException {
        final int size = Integer.parseInt(args[0]);
        final int threadCount = Integer.parseInt(args[1]);
        int step = size / threadCount;
        int rest = size % threadCount;
        Random random = new Random();
        int[] array = IntStream.range(0, size).map((int i) -> random.nextInt() % 1000).toArray();
        System.out.printf("Sum: %d\n", Arrays.stream(array).sum());
        List<Thread> threads = new ArrayList<>(threadCount);
        RangeSum[] res = new RangeSum[threadCount];

        int startIndex = 0;
        for (int i = 0; i < threadCount; i++) {
            int endIndex = startIndex + step;
            final int start = startIndex;
            final int end = endIndex;

            int finalI = i;
            threads.add(
                    new Thread(
                            () -> {
                                int sum = Arrays.stream(array, start, end).sum();
                                res[finalI] = new RangeSum(start, end, sum);
                            }));
            startIndex = endIndex;
        }
        if (rest != 0) {
            int finalStartIndex = startIndex;
            Runnable r =
                    () -> {
                        int sum =
                                Arrays.stream(array, finalStartIndex, finalStartIndex + rest + 1)
                                        .sum();
                        res[threadCount - 1] =
                                new RangeSum(finalStartIndex, finalStartIndex + rest + 1, sum);
                    };
            threads.add(new Thread(r));
        }

        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }

        int index = 0;
        for (RangeSum r : res) {
            System.out.printf("Thread %d: from %d to %d sum is %d\n", index, r.start, r.end, r.sum);
            index++;
        }
        System.out.printf("Sum by threads: %d", Arrays.stream(res).mapToInt(x -> x.sum).sum());
    }
}
