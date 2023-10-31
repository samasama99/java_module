package fr.leet.printer.logic;

public class PixelPrinter {
    public static void print(
            int[][] map, char blackSymbol, char whiteSymbol, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map[y][x] == 0xFF000000)
                    System.out.print(blackSymbol);
                if (map[y][x] == 0xFFFFFFFF)
                    System.out.print(whiteSymbol);
            }
            System.out.println();
        }
    }
}
