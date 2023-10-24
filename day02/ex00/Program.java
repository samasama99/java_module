import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.nio.file.Path;

public class Program {
  public static void main(final String[] args) {
    try {
      int maxSignatureLenght = 0;
      FileInputStream signaturesFile = new FileInputStream("./signatures.txt");
      String bytes = new String(signaturesFile.readAllBytes());
      HashMap<String, String> signatures = new HashMap<>();
      for (String line : bytes.split("\n", 0)) {
        String[] typeSignature = line.split(",", 0);
        String type = typeSignature[0];
        StringBuilder signature = new StringBuilder();
        String[] splited = typeSignature[1].split(" ");
        maxSignatureLenght = maxSignatureLenght > splited.length ? maxSignatureLenght : splited.length;
        for (String s : splited) {
          signature.append(s.toUpperCase());
        }

        signatures.put(signature.toString(), type);
      }

      ArrayList<String> files = new ArrayList<>();
      try (Scanner user_input = new Scanner(System.in)) {
        while (true) {
          System.out.print("-> ");
          if (!user_input.hasNextLine()) {
            System.out.println("exit");
            System.exit(-1);
          }
          String arg = user_input.nextLine();
          if (arg.equals("42"))
            break;
          if (!Files.isRegularFile(Path.of(arg))) {
            System.out.println("please provide a valid file");
            continue;
          }
          try {
            try (FileInputStream tmp = new FileInputStream(arg)) {
              int byteRead;
              StringBuilder str = new StringBuilder();
              int len = 0;
              while (((byteRead = tmp.read()) != -1) && len < maxSignatureLenght) {
                str.append(String.format("%02x", byteRead).toUpperCase());
                len++;
              }
              String res = "UNDEFINED";
              for (var entry : signatures.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (str.toString().startsWith(key)) {
                  res = value;
                  break;
                }
              }

              System.out.print("-> ");
              System.out.println(arg);
              System.out.println("PROCESSED");
              files.add(res);
            }
          } catch (Exception e) {
            System.out.println(e.getMessage());
            continue;
          }

        }
      }

      var out = new FileOutputStream("result.txt");
      files.forEach(
          (value) -> {
            String tmp = value + "\n";
            try {
              out.write(tmp.getBytes());
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
      System.out.print("-> ");
      System.out.println(42);
      out.close();
      signaturesFile.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
