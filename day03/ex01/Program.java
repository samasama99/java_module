import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Program {
    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1 || !args[0].startsWith("--count=")) {
            System.out.println("Please provide the count option!");
            System.exit(-1);
        }
        try {
            String value = args[0].substring("--count=".length());
            final int count = Integer.parseInt(value);
            simulation(List.of("Egg", "Hen"), count);
        } catch (Exception e) {
            System.out.println("Please provide a valid count option!");
            System.exit(-1);
        }
    }

    public static void simulation(List<String> wordList, int count) throws InterruptedException {
        List<String> buffer = new LinkedList<>();

        for (int i = 0; i < count; i++) {
            buffer.addAll(wordList);
        }

        List<Thread> threads = new ArrayList<>(wordList.size());

        for (var w : wordList) {
            Thread thread = consumerFunction(buffer);
            thread.start();
            threads.add(thread);
        }
        for (var thread : threads) {
            thread.join();
        }

    }

    private static Thread consumerFunction(List<String> buffer) {
        return new Thread(() -> {
            try {
                synchronized (buffer) {
                    while (!buffer.isEmpty()) {
                        String value = buffer.remove(0);
                        System.out.println(value);
                        buffer.notify();
                        buffer.wait();
                    }
                    buffer.notify();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
