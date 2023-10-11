import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Program {
  public static void main(final String[] args) {
    try {
      FileInputStream signaturesFile = new FileInputStream("./signatures.txt");
      String bytes = new String(signaturesFile.readAllBytes());
      HashMap<String, String> signatures = new HashMap<>();
      for (String line : bytes.split("\n", 0)) {
        String[] typeSignature = line.split(",", 0);
        String type = typeSignature[0];
        StringBuilder signature = new StringBuilder();
        for (String s : typeSignature[1].split(" ")) {
          signature.append(s);
        }
        signatures.put(signature.toString(), type);
      }

      ArrayList<String> files = new ArrayList<>();
      for (String arg : args) {
        FileInputStream tmp = new FileInputStream(arg);
        int byteRead;
        StringBuilder str = new StringBuilder();
        while ((byteRead = tmp.read()) != -1) {
          char c = (char) byteRead;
          if (c == '\n') {
            break;
          }
          str.append(String.format("%02x", byteRead).toUpperCase());
        }
        String res = "UNDEFINED";
        for (var entry : signatures.entrySet()) {
          String key = entry.getKey();
          String value = entry.getValue();

          if (str.toString().contains(key) || key.contains(str)) {
            res = value;
            break;
          }
        }

        System.out.print("-> ");
        System.out.println(arg);
        System.out.println("PROCESSED");
        files.add(res);
        tmp.close();
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
      throw new RuntimeException(e);
    }
  }
}
