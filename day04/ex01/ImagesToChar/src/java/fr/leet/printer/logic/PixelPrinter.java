package fr.leet.printer.logic;

public class PixelPrinter {

    static private final int WHITE = 0xFFFFFFFF;
    static private final int BLACK = 0xFF000000;

    public static void print(
            int[][] map, char blackSymbol, char whiteSymbol, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map[y][x] == BLACK)
                    System.out.print(blackSymbol);
                if (map[y][x] == WHITE)
                    System.out.print(whiteSymbol);
            }
            System.out.println();
        }
    }
}
