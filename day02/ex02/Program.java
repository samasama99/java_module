import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Program {
  static void cd(String path) throws FileNotFoundException, NotDirectoryException {
    Path current_path = pwd();
    String real_path = String.valueOf(current_path.resolve(path).normalize());
    File newDirectory = new File(real_path);
    if (newDirectory.exists() && newDirectory.isDirectory()) {
      System.setProperty("user.dir", real_path);
    } else if (newDirectory.exists()) {
      throw new NotDirectoryException(path);
    } else {
      throw new FileNotFoundException();
    }
  }

  static Path pwd() {
    return Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
  }

  static void ls() throws FileNotFoundException, NotDirectoryException {
    File dir = new File(String.valueOf(pwd()));
    if (dir.exists() && dir.isDirectory()) {
      Arrays.stream(Objects.requireNonNull(dir.listFiles()))
          .forEach(file -> System.out.printf("%s %d KB\n", file.getName(), file.length()));
    } else if (dir.exists()) {
      throw new NotDirectoryException(dir.getName());
    } else {
      throw new FileNotFoundException();
    }
  }

  static void mv(String source, String target) throws IOException {
    Files.move(Path.of(source), Path.of(target), StandardCopyOption.REPLACE_EXISTING);
  }

  static String readLine(String err, Scanner userInput) {
    String line = null;
    boolean validInput = false;
    while (!validInput) {
      try {
        if (!userInput.hasNext()) {
          throw new InputMismatchException(err);
        }
        line = userInput.next().strip();
        validInput = true;
      } catch (InputMismatchException e) {
        System.out.println("Error: " + e.getMessage());
        System.out.println("Please try again.");
        userInput.nextLine();
        System.out.print("-> ");
      }
    }
    return line;
  }

  public static void main(final String[] args) {
    try (Scanner userInput = new Scanner(System.in)) {
      if (args.length > 0) {
        String stripped = args[0].strip();
        String[] split = stripped.split("=", 0);
        if (!split[0].strip().equals("--current-folder")) {
          throw new Exception("Please provide a valid arg !");
        }
        String path = split[1].strip();
        File newDirectory = new File(String.valueOf(Path.of(path).toAbsolutePath()));
        if (newDirectory.exists() && newDirectory.isDirectory()) {
          System.setProperty("user.dir", path);
        } else if (newDirectory.exists()) {
          throw new NotDirectoryException(path);
        } else {
          throw new FileNotFoundException();
        }
        System.out.println(pwd());
      } else {
        System.out.println(pwd());
      }
      while (true) {
        try {
          String command = null;
          System.out.print("-> ");
          command = readLine("Please enter a valid command", userInput);
          switch (command) {
            case "ls" -> ls();
            case "pwd" -> System.out.println(pwd());
            case "cd" -> {
              String dir = readLine("Please enter a valid directory", userInput).strip();
              cd(dir);
            }
            case "mv" -> {
              String source = readLine("Please enter a valid source", userInput).strip();
              String target = readLine("Please enter a valid target", userInput).strip();
              mv(source, target);
            }
            case "exit" -> {
              return;
            }
            default -> throw new IllegalStateException("Unexpected value: " + command);
          }
        } catch (FileNotFoundException e) {
          System.out.println("FileNotFoundException");
        } catch (NotDirectoryException e) {
          System.out.println("NotDirectoryException");
        } catch (Exception e) {
          System.out.print("Exception: ");
          System.out.println(e.getMessage());
        }
      }
    } catch (Exception e) {
      System.out.print("Exception: ");
      System.out.println(e.getMessage());
    }
  }
}
// $> java Program --current-folder=/Users/Admin
