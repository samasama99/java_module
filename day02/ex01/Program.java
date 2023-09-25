import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

public class Program {

  static HashMap<String, Integer> wordsFrequency(final BufferedReader reader) throws IOException {
    var dictionary = new HashMap<String, Integer>();
    String line;
    while ((line = reader.readLine()) != null) {
      for (var word : line.split(" ")) {
        var stripped = word.replaceAll("[^a-zA-Z0-9]", "");
        if (stripped.isEmpty()) {
          continue;
        }
        if (!dictionary.containsKey(word.strip())) {
          dictionary.put(stripped, 1);
        } else {
          var count = dictionary.get(stripped);
          dictionary.put(stripped, count + 1);
        }
      }
    }
    return dictionary;
  }

  public static void main(final String... args) throws IOException {
    final FileReader file1 = new FileReader(args[0]);
    final FileReader file2 = new FileReader(args[1]);

    final BufferedReader reader1 = new BufferedReader(file1);
    final BufferedReader reader2 = new BufferedReader(file2);

    final HashMap<String, Integer> dic1 = wordsFrequency(reader1);
    final HashMap<String, Integer> dic2 = wordsFrequency(reader2);

    long numerator = 0;
    long a = 0;
    long b = 0;
    var orderedSet = new TreeSet<String>();
    orderedSet.addAll(dic1.keySet());
    orderedSet.addAll(dic2.keySet());
    for (String word : orderedSet) {
      long count1 = dic1.getOrDefault(word, 0);
      long count2 = dic2.getOrDefault(word, 0);
      numerator += count1 * count2;
      a += count1 * count1;
      b += count2 * count2;
    }
    final double denominator = Math.sqrt(a) * Math.sqrt(b);
    System.out.printf("Similarity = %.2f", ((double) numerator) / denominator);
  }
}
