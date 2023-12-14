import java.util.Arrays;
import java.util.stream.IntStream;

public final class RangeSum implements Runnable {
    final private IntStream array;
    final private int start;
    final private int end;
    private int sum;

    public RangeSum(int[] array, int startInclusive, int endExclusive) {
        this.start = startInclusive;
        this.end = endExclusive;
        this.array = Arrays.stream(array, start, end);
    }

    int getSum() {
        return sum;
    }

    int getStart() {
        return start;
    }

    int getEnd() {
        return end;
    }

    @Override
    public void run() {
        if (sum == 0) {
            this.sum = array.sum();
        }
    }
}
