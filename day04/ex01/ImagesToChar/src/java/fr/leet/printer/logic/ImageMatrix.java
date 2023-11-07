package fr.leet.printer.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageMatrix {
    static private final int WHITE = 0xFFFFFFFF;
    static private final int BLACK = 0xFF000000;

    private final BufferedImage image;
    private final int width;
    private final int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageMatrix(InputStream file) throws IOException {
        image = ImageIO.read(file);
        width = image.getWidth();
        height = image.getHeight();
    }

    public int[][] toMatrix() throws Exception {
        int[][] matrix = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelValue = image.getRGB(x, y);
                if (pixelValue != WHITE && pixelValue != BLACK) {
                    throw new Exception("provide white and black bmp image");
                }
                matrix[y][x] = pixelValue;
            }
        }

        return matrix;
    }
}
