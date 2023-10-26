import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.naming.SizeLimitExceededException;

public class Program {
    public static <T> Optional<T> parseCommandLineArgument(String[] args, String argName, Function<String, T> parser) {
        return Stream
                .of(args)
                .filter(arg -> arg.startsWith("--" + argName + "="))
                .findFirst()
                .map(arg -> arg.substring(argName.length() + 3))
                .map(parser);
    }

    static Path getFileName(URL url) throws URISyntaxException {
        URI uri = url.toURI();
        String path = uri.getPath();
        return Path.of(path).getFileName();
    }

    static void downloadFileFromURL(URL url, Path dst) {
        try (InputStream in = url.openStream()) {
            System.out.printf("[DOWNLOADING] %s\n", url);
            Files.copy(in, dst.resolve(getFileName(url)), StandardCopyOption.REPLACE_EXISTING);
            System.out.printf("[DONE] %s\n", url);
        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Optional<Integer> threadCountOptional = parseCommandLineArgument(args, "threadCount", Integer::parseInt);
            final int threadCount = threadCountOptional.orElseThrow(
                    () -> new NoSuchElementException(
                            "Please provide the number of the thread (--threadCount={number})"));

            if (threadCount <= 0) {
                throw new SizeLimitExceededException(
                        "The number of threads should be more than 0!");
            }

            Path filesURLs = Path.of("files_urls.txt");
            List<URL> urls = Files
                    .lines(filesURLs)
                    .filter(line -> !line.strip().isEmpty())
                    .map(line -> {
                        try {
                            return new URI(line).toURL();
                        } catch (MalformedURLException | URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            if (urls.size() == 0) {
                System.out.println("There is no urls to download!");
                return;
            }

            Path dst = Path.of("./dst/");
            if (Files.notExists(dst)) {
                Files.createDirectory(dst);
            }

            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            for (URL url : urls) {
                executor.execute(() -> downloadFileFromURL(url, dst));
            }
            executor.shutdown();
            boolean terminated = executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            if (terminated) {
                System.out.println("All tasks have completed");
            } else {
                System.err.println("Timeout elapsed before all tasks completed");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Please provide the source file (files_urls.txt)");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
