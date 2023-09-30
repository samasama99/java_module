package fr.leet.printer.logic;

import java.io.IOException;

public class PixelPrinter {
    public static void print(
            int[][] map, char blackSymbol, char whiteSymbol, int width, int height) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(map[y][x] == 0 ? blackSymbol : whiteSymbol);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        var bmpParser =
                new BmpParser(
                        "C:\\Users\\oussr\\IdeaProjects\\java\\day04\\ex00\\ImagesToChar\\it.bmp");
        int[][] map = bmpParser.parse();
        PixelPrinter.print(map, '0', '.', bmpParser.getWidth(), bmpParser.getHeight());
    }
}
