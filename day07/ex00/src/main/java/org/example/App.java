package org.example;

import org.example.classes.Classes;
import org.example.item.Item;
import org.example.user.User;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Hello world!
 */
public class App {

    // supported types are String, Integer, Double, Boolean, Long
    static Map<Class<?>, Function<String, ?>> parsers = Map.of(
            Integer.class, Integer::parseInt,
            Long.class, Long::parseLong,
            Double.class, Double::parseDouble,
            String.class, Function.identity(),
            Boolean.class, Boolean::parseBoolean
    );

    public static <T> T parseLine(String line, Function<String, T> parser) {
        return parser.apply(line);
    }


    private enum State {
        CLASS_INTRODUCTION,
        FIELD_INITIALIZATION,
        FIELD_MANIPULATION,
        METHOD_CALLS
    }

    public static void main(String[] args) {


        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {

            List<Class<?>> classes = List.of(
                    User.class,
                    Item.class
            );

            List<? extends Constructor<?>> defaultConstructors = Classes.getDefaultConstructors(classes);

            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

            State
            while (true) {
                switch ()
                System.out.print("-> ");
                String line = lineReader.readLine();
                if (line.isBlank()) continue;
                System.out.println(line);
            }
        } catch (IOException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println(e.getMessage());
        } catch (EndOfFileException e) {
            System.out.println("Ctrl-D was pressed, Exiting.");
        }

    }
}
