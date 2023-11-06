package fr.leet.printer.logic;

public class PixelPrinter {
    static final int BLACK = 0xFF000000;
    static final int WHITE = 0xFFFFFFFF;

    public static void print(
            int[][] map, char blackSymbol, char whiteSymbol, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(map[y][x] == BLACK ? blackSymbol : whiteSymbol);
            }
            System.out.println();
        }
    }
}
