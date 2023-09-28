import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Program {

  static String getFileName(String url) throws URISyntaxException {
    URI uri = new URI(url);
    String path = uri.getPath();
    Path filePath = Path.of(path).getFileName();
    return filePath.toString();
  }

  static void readFile(URL url, String dst) {
    try {
      URLConnection connection = url.openConnection();
      InputStream in = connection.getInputStream();
      FileOutputStream out =
          new FileOutputStream(String.valueOf(Path.of(dst).resolve(getFileName(url.toString()))));
      byte[] buffer = new byte[1024];
      int b;
      System.out.printf("Downloading %s\n", url);
      while ((b = in.read(buffer)) != -1) {
        out.write(buffer, 0, b);
      }
      in.close();
      out.close();
      System.out.printf("Done Downloading %s\n", url);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    ArrayList<URL> urls = new ArrayList<>();
    try {
      urls.add(new URL("https://tldp.org/LDP/intro-linux/intro-linux.pdf"));
      urls.add(
          new URI("https://i.pinimg.com/originals/11/19/2e/11192eba63f6f3aa591d3263fdb66bd5.jpg")
              .toURL());
      urls.add(
          new URI("https://pluspng.com/img-png/balloon-hd-png-balloons-png-hd-2750.png").toURL());
      urls.add(
          new URI("https://i.pinimg.com/originals/db/a1/62/dba162603c71cac00d3548420c52bac6.png")
              .toURL());
    } catch (Exception e) {
      e.printStackTrace();
    }

    String dst = "./dst/";
    ExecutorService executor = Executors.newFixedThreadPool(2);
    for (URL url : urls) {
      executor.execute(() -> readFile(url, dst));
    }
    executor.shutdown();
    try {
      boolean terminated = executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

      if (terminated) {
        System.out.println("All tasks have completed");
      } else {
        System.err.println("Timeout elapsed before all tasks completed");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
