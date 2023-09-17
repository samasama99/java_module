import java.util.Arrays;
import java.util.Scanner;

class Program {
    public static void main(String[] args) {
        int[] histogram = new int[65535];

        Scanner user_input = new Scanner(System.in);
        System.out.print("-> ");

        boolean stop = false;
        while (!stop) {
            String input = user_input.next();
            char[] input_chars = input.toCharArray();

            for (int i = 0; i < input_chars.length; i++) {
                if (i + 1 < input_chars.length
                        && input_chars[i] == '4'
                        && input_chars[i + 1] == '2') {
                    stop = true;
                    break;
                }

                histogram[input_chars[i]]++;
            }
        }

        int max = Integer.MIN_VALUE;
        for (int num :
                histogram) {
            if (num == 0)
                continue;
            max = Integer.max(max, num);
        }

        char[] chars = new char[10];
        int[] appearance = new int[10];
        int current = 0;
        System.out.println();
        while (true) {
            int index = -1;
            int max_index = -1;
            for (int num : histogram) {
                index++;
                if (num == 0)
                    continue;
                if (max_index == -1) {
                    max_index = index;
                } else if (num > histogram[max_index]) {
                    max_index = index;
                }
            }

            if (max_index == -1 || current == 10)
                break;

            chars[current] = (char) max_index;
            appearance[current] = histogram[max_index];
            current++;
            histogram[max_index] = 0;
        }

        int real_max = max >= 10 ? 10 : max;
        int[][] graph = new int[current][real_max + 1];
        for (int i = 0; i < current; i++) {
            for (int j = 0; j < real_max + 1; j++) {
                graph[i][j] = -1;
            }
        }

        for (int i = 0; i < current; i++) {
            int start = real_max - (appearance[i] * 10 / max);
            if (max < 10)
                start = max - appearance[i];
            graph[i][start] = appearance[i];

            for (int j = start + 1; j < real_max + 1; j++) {
                graph[i][j] = -2;
            }
        }

        for (int i = 0; i < real_max + 1; i++) {
            for (int j = 0; j < current; j++) {
                if (graph[j][i] == -1) {
                    System.out.print(' ');
                }
                else if (graph[j][i] == -2) {
                    System.out.print('#');
                } else {
                    System.out.print(graph[j][i]);
                }
                System.out.print('\t');
                System.out.print('\t');
            }
            System.out.println();
        }

        int i = 0;
        for (char c :
                chars) {
            if (i == current)
                break;
            System.out.print(c);
            System.out.print('\t');
            System.out.print('\t');
            i++;
        }
        System.out.println();


        user_input.close();
    }
}