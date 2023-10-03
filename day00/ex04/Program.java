import java.util.Scanner;

class Program {
  static final int MAX = 65535;

  public static void main(final String[] args) {
    int[] histogram = new int[MAX];

    Scanner userInput = new Scanner(System.in);
    System.out.print("-> ");

    boolean stop = false;
    int max = Integer.MIN_VALUE;
    while (!stop) {
      String input = userInput.next();
      char[] inputChars = input.toCharArray();

      for (int i = 0; i < inputChars.length; i++) {
        if (i + 1 < inputChars.length && inputChars[i] == '4' && inputChars[i + 1] == '2') {
          stop = true;
          break;
        }

        histogram[inputChars[i]]++;
        max = max > histogram[inputChars[i]] ? max : histogram[inputChars[i]];
      }
    }

    char[] chars = new char[10];
    int[] appearance = new int[10];
    int current = 0;
    System.out.println();
    while (true) {
      int index = -1;
      int maxIndex = -1;

      for (int num : histogram) {
        index++;
        if (num == 0) {continue;}
        if (maxIndex == -1) {
          maxIndex = index;
        } else if (num > histogram[maxIndex]) {
          maxIndex = index;
        }
      }

      if (maxIndex == -1 || current == 10) break;

      chars[current] = (char) maxIndex;
      appearance[current] = histogram[maxIndex];
      current++;
      histogram[maxIndex] = 0;
    }

    int realMax = max < 10 ? max : 10;
    int[][] graph = new int[current][realMax + 1];
    for (int i = 0; i < current; i++) {
      for (int j = 0; j < realMax + 1; j++) {
        graph[i][j] = -1;
      }
    }

    for (int i = 0; i < current; i++) {
      int start = realMax - (appearance[i] * 10 / max);
      if (max < 10) start = max - appearance[i];
      graph[i][start] = appearance[i];

      for (int j = start + 1; j < realMax + 1; j++) {
        graph[i][j] = -2;
      }
    }

    for (int i = 0; i < realMax + 1; i++) {
      for (int j = 0; j < current; j++) {
        if (graph[j][i] == -1) {
          System.out.printf("%4c", ' ');
        } else if (graph[j][i] == -2) {
          System.out.printf("%4c", '#');
        } else {
          System.out.printf("%4d", graph[j][i]);
        }
      }
      System.out.println();
    }

    int i = 0;
    for (char c : chars) {
      if (i == current) break;
      System.out.printf("%4c", c);
      i++;
    }
    System.out.println();

    userInput.close();
  }
}
