import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Program {
    public static void main(String[] args) {
        try {
            FileInputStream signatures_file = new FileInputStream("./signatures.txt");
            String bytes = new String(signatures_file.readAllBytes());
            HashMap<String, String> signatures = new HashMap<>();
            for (String line : bytes.split("\n", 0)) {
                String[] type_signature = line.split(",", 0);
                String type = type_signature[0];
                StringBuilder signature = new StringBuilder();
                for (String s : type_signature[1].trim().split(" ")) {
                    signature.append((char) Integer.parseInt(s, 16));
                }
                signatures.put(signature.toString().trim(), type.trim());
                System.out.printf("Type : [%s] : Signature [%s]\n", type.trim(), signature.toString().trim());
            }
            HashMap<String, String> files = new HashMap<>();
            for (String arg : args) {
                FileInputStream tmp = new FileInputStream(arg);
                int byte_read = 0;
                StringBuilder str = new StringBuilder();
                while ((byte_read = tmp.read()) != -1) {
                    char c = (char) byte_read;
                    if (c == '\n')
                        break;
                    str.append(c);
                }
                String _str = str.toString().trim();
                for (var entry : signatures.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if (key.contains(_str) || _str.contains(key)) {
                        files.put(arg, value);
                        break;
                    } else {
                        files.put(arg, "UNDEFINED");
                    }
                }
            }

            System.out.println();
            var out = new FileOutputStream("result.txt");
            files.forEach((key, value) -> {
                String tmp = value + "\n";
                try {
                    out.write(tmp.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.printf("key: [%s], value: [%s]\n", key, value);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
