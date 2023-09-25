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

  public static void main(final String[] args) {
    Scanner userInput = new Scanner(System.in);
    try {
      if (args.length > 0) {
        cd(args[0]);
        System.out.println(args[0]);
      } else {
        System.out.println(pwd());
      }
      while (true) {
        try {
          String command = null;
          System.out.print("-> ");
          boolean validInput = false;
          while (!validInput) {
            try {
              if (!userInput.hasNext()) {
                throw new InputMismatchException("Please enter a valid command");
              }
              command = userInput.next().strip();
              validInput = true;
            } catch (InputMismatchException e) {
              System.out.println("Error: " + e.getMessage());
              System.out.println("Please try again.");
              userInput.nextLine();
              System.out.print("-> ");
            }
          }
          switch (command) {
            case "ls" -> ls();
            case "pwd" -> System.out.println(pwd());
            case "cd" -> {
              String dir = userInput.next().strip();
              cd(dir);
            }
            case "mv" -> {
              String source = userInput.next().strip();
              String target = userInput.next().strip();
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
          System.out.println("Exception");
          System.out.println(e.getMessage());
        }
      }
    } catch (Exception e) {
      System.out.println("Exception");
      System.out.println(e.getMessage());
    } finally {
      userInput.close();
    }
  }
}
//      $> java Program --current-folder=/Users/Admin
