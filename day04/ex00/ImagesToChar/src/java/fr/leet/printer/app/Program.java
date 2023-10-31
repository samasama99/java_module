package fr.leet.printer.app;

import fr.leet.printer.logic.BmpParser;
import fr.leet.printer.logic.PixelPrinter;

class Program {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough arguments !");
            System.exit(1);
        }

        char blackSymbol = args[0].length() == 1 ? args[0].charAt(0) : (char) -1;
        char whiteSymbol = args[1].length() == 1 ? args[1].charAt(0) : (char) -1;
        if (blackSymbol == (char) -1) {
            System.err.println("Wrong black symbol [b w bmp]");
            System.exit(1);
        }
        if (whiteSymbol == (char) -1) {
            System.err.println("Wrong white symbol [b w bmp]");
            System.exit(1);
        }
        String path = args[2];

        try {
            BmpParser bmpParser = new BmpParser(path);
            PixelPrinter.print(
                    bmpParser.parse(),
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
