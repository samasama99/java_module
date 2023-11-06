package fr.leet.printer.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageMatrix {

    static final int BLACK = 0xFF000000;
    static final int WHITE = 0xFFFFFFFF;

    private BufferedImage image;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageMatrix(String path) throws IOException {
        image = ImageIO.read(new File(path));
        width = image.getWidth();
        height = image.getHeight();
    }

    public int[][] matrix() throws Exception {
        int[][] map = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = image.getRGB(x, y);
                if (pixelValue != WHITE && pixelValue != BLACK) {
                    throw new Exception("provide a white and black bmp image");
                }
                map[y][x] = pixelValue;
            }
        }

        return map;
    }
}
