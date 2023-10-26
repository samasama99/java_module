import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;
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

    public static void main(String[] args) throws InterruptedException {
        try {
            Optional<Integer> countOptional = parseCommandLineArgument(args, "count", Integer::parseInt);
            final int count = countOptional.orElseThrow(
                    () -> new NoSuchElementException("Please provide the count option (--count={number})"));

            if (count < 0) {
                throw new Exception(
                        "Count can't be negative!");
            }
            simulation(List.of("Egg", "Hen"), count);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public static void simulation(List<String> wordList, int count) throws InterruptedException {
        Queue<String> queue = new LinkedList<>();

        List<Thread> threads = new ArrayList<>(wordList.size());

        Thread producer = producerFunctionFactory(queue, wordList, count);

        for (int i = 0; i < wordList.size(); i++) {
            Thread thread = consumerFunctionFactory(queue);
            thread.start();
            threads.add(thread);
        }
        producer.start();
        for (var thread : threads) {
            thread.join();
        }
        producer.join();

    }

    private static Thread consumerFunctionFactory(Queue<String> buffer) {
        return new Thread(() -> {
            try {
                while (true) {
                    synchronized (buffer) {
                        if (buffer.isEmpty()) {
                            buffer.wait();
                        }
                        String value = buffer.poll();
                        if (value == null) {
                            buffer.notifyAll();
                            return;
                        }
                        System.out.println(value);
                        buffer.notifyAll();
                        buffer.wait();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Thread producerFunctionFactory(Queue<String> buffer, List<String> wordList, int count) {
        return new Thread(
                () -> {
                    try {
                        for (int i = 0; i < count; i++) {
                            synchronized (buffer) {
                                buffer.addAll(wordList);
                                buffer.notifyAll();
                                buffer.wait();
                            }
                        }
                        for (int i = 0; i < wordList.size(); i++) {
                            synchronized (buffer) {
                                buffer.add(null);
                                buffer.notifyAll();
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
