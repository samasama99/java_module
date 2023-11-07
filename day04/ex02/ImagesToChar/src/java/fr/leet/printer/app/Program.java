package fr.leet.printer.app;

import java.io.InputStream;
import java.util.*;

import com.beust.jcommander.*;
import com.diogonunes.jcolor.Attribute;
import fr.leet.printer.logic.BmpParser;
import fr.leet.printer.logic.PixelPrinter;

import static com.diogonunes.jcolor.Attribute.*;

@Parameters(separators = "=")
class Program {

    @Parameter(names = {
            "--black" }, description = "Black color replacement", validateValueWith = AcceptableValuesValidator.class, required = true)
    String black;

    @Parameter(names = {
            "--white" }, description = "White color replacement", validateValueWith = AcceptableValuesValidator.class, required = true)
    String white;

    public static void main(String[] args) {
        Program program = new Program();
        JCommander jCommander = JCommander.newBuilder().addObject(program).build();

        try {
            jCommander.parse(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        Map<String, Attribute> colors = Map.of(
                "BLACK", BLACK_BACK(),
                "RED", RED_BACK(),
                "WHITE", WHITE_BACK(),
                "BLUE", BLUE_BACK(),
                "GREEN", GREEN_BACK(),
                "YELLOW", YELLOW_BACK(),
                "CYAN", CYAN_BACK(),
                "MAGENTA", MAGENTA_BACK());

        InputStream image = Program.class.getResourceAsStream("/resources/image.bmp");
        if (image == null) {
            System.err.println("failed to open the resource (image.bmp)");
            System.exit(1);
        }

        try {
            BmpParser bmpParser = new BmpParser(image);
            PixelPrinter.print(
                    bmpParser.parse(),
                    colors.get(program.black),
                    colors.get(program.white),
                    bmpParser.getWidth(),
                    bmpParser.getHeight());
        } catch (Exception e) {
            System.err.println("[Error] " + e.getMessage());
            System.exit(-1);
        }
    }

    public static class AcceptableValuesValidator implements IValueValidator<String> {
        @Override
        public void validate(String name, String value) throws ParameterException {
            Set<String> acceptableValues = new HashSet<>(List.of(
                    "BLACK", "RED", "GREEN", "BLUE", "WHITE", "MAGENTA", "CYAN", "YELLOW"));

            if (!acceptableValues.contains(value.toUpperCase())) {
                throw new ParameterException("Invalid value for " + name + ". Please choose from: " + acceptableValues);
            }
        }
    }
}
