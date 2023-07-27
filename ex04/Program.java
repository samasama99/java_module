import java.util.Scanner;

class Program {
    public static void main(String[] args) {
        int[] histogram = new int[65535];

        Scanner user_input = new Scanner(System.in);

        System.out.print("-> ");
        String input = user_input.next();
        char[] input_chars = input.toCharArray();
        int number_of_numbers = 0;

        for (int i = 0; i < input_chars.length; i++) {
            if (i + 1 < input_chars.length
                    && input_chars[i] == '4'
                    && input_chars[i + 1] == '2')
                break;

            histogram[input_chars[i]]++;
            if (histogram[input_chars[i]] == 1)
                number_of_numbers++;
        }

        int max = Integer.MIN_VALUE;
        int max_index = -1;
        int first_max = -1;
        String[] result = new String[12];
        int position = 0;

        for (int i = 0; i < result.length; i++) {
            result[i] = "";
        }
        while (true) {
            max = Integer.MIN_VALUE;

            for (int i = 0; i < histogram.length; i++) {
                if (histogram[i] > max) {
                    max = histogram[i];
                    max_index = i;
                }
            }

            if (max == 0 || max_index == -1)
                break;

            if (first_max == -1) {
                first_max = max;
            }

            double ratio;

            if (first_max <= 10)
                ratio = max;
            else
                ratio = max / (double) first_max * 10;

            if (first_max > 10) {
                result[10 - (int) ratio] += max;
                result[10 - (int) ratio] += ' ';
                result[10 - (int) ratio] += ' ';
            } else {
                result[(int) ratio] += max;
                result[(int) ratio] += ' ';
                result[(int) ratio] += ' ';
            }

            // System.out.println("debug " + (int) ratio);
            // System.out.println("position " + position);

            for (int i = 0; i < (int) ratio; i++) {
                result[position + i + 1] += '#';
                result[position + i + 1] += ' ';
                result[position + i + 1] += ' ';
            }

            result[(first_max < 10 ? first_max : 10) + 1] += (char) max_index;
            result[(first_max < 10 ? first_max : 10) + 1] += ' ';
            result[(first_max < 10 ? first_max : 10) + 1] += ' ';
            histogram[max_index] = 0;
            position++;
        }

        for (int i = 0; i < (first_max < 10 ? first_max : 10) + 2; i++) {
            System.out.println(result[i]);
        }

        user_input.close();
    }
}