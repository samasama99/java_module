package fr.leet.printer.app;

import fr.leet.printer.logic.ImageMatrix;
import fr.leet.printer.logic.PixelPrinter;

import java.io.FileInputStream;
import java.io.InputStream;

class Program {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Not enough arguments !");
            System.exit(1);
        }

        char whiteSymbol = args[0].length() == 1 ? args[0].charAt(0) : (char) -1;
        char blackSymbol = args[1].length() == 1 ? args[1].charAt(0) : (char) -1;

        if (blackSymbol == (char) -1) {
            System.err.println("Wrong black symbol [black white bmp]");
            System.exit(1);
        }
        if (whiteSymbol == (char) -1) {
            System.err.println("Wrong white symbol [black white bmp]");
            System.exit(1);
        }

        String path = args[2];

        try {
            InputStream image = new FileInputStream(args[2]);
            if (image.read() != 'B' && image.read() != 'M') {
                image.close();
                throw new Exception("please provide a valid bmp file");
            }
            image.close();

            ImageMatrix bmpParser = new ImageMatrix(path);
            PixelPrinter.print(
                    bmpParser.matrix(),
                    blackSymbol,
                    whiteSymbol,
                    bmpParser.getWidth(),
                    bmpParser.getHeight());
        } catch (Exception e) {
            System.err.println("[Error] " + e.getMessage());
            System.exit(-1);
        }
    }
}
