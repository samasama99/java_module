import java.util.stream.IntStream;

public class RangeSum {
    final private IntStream array;
    final private int start;
    final private int end;
    private int sum;

    public RangeSum(IntStream array, int startInclusive, int endExclusive) {
        this.start = startInclusive;
        this.end = endExclusive;
        this.array = array;
    }

    void calculateSum() {
        if (sum == 0) {
            this.sum = array.sum();
        }
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
}
