package fr.leet.printer.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BmpParser {
    public BufferedImage image;
    public int width;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int height;

    public BmpParser(String path) throws IOException {
        image = ImageIO.read(new File(path));
        width = image.getWidth();
        height = image.getHeight();
    }

    public int[][] parse() {
        int[][] map = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = image.getRGB(x, y) & 0x01;
                map[y][x] = pixelValue;
            }
        }

        return map;
    }

    public static void main(String[] args) throws IOException {
        var bmpParser =
                new BmpParser(
                        "C:\\Users\\oussr\\IdeaProjects\\java\\day04\\ex00\\ImagesToChar\\it.bmp");
        int[][] map = bmpParser.parse();
        for (int y = 0; y < bmpParser.getHeight(); y++) {
            for (int x = 0; x < bmpParser.getWidth(); x++) {
                System.out.print(map[y][x] + " ");
            }
            System.out.println();
        }
    }
}
