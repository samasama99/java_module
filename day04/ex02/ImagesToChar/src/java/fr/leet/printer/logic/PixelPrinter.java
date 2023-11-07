package fr.leet.printer.logic;

import com.diogonunes.jcolor.Attribute;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PixelPrinter {
    public static void print(
            int[][] map, Attribute black, Attribute white, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map[y][x] == 0xFF000000)
                    System.out.print(colorize("  ", black));
                if (map[y][x] == 0xFFFFFFFF)
                    System.out.print(colorize("  ", white));
            }
            System.out.println();
        }
    }
}
