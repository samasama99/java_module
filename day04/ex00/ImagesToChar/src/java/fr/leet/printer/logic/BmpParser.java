package fr.leet.printer.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.MissingFormatArgumentException;

public class BmpParser {
    private BufferedImage image;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BmpParser(String path) throws IOException {
        image = ImageIO.read(new File(path));
        width = image.getWidth();
        height = image.getHeight();
    }

    public int[][] parse() throws Exception {
        int[][] map = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = image.getRGB(x, y);
                if (pixelValue != 0xFF000000 && pixelValue != 0xFFFFFFFF) {
                    throw new Exception("provide white and black bmp image");
                }
                map[y][x] = pixelValue;
            }
        }

        return map;
    }
}
