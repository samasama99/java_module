import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Program {
    public static void main(String[] args) throws InterruptedException {
        int count = Integer.parseInt(args[0]);
        simulation(List.of("Egg", "Hen", "Human", "Test"), count);
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
        for (var t : threads) {
            t.join();
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
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
