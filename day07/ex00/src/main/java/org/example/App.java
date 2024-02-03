package org.example;

import org.example.item.Item;
import org.example.user.User;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Hello world!
 */
public class App {

    private static final Map<String, Class<?>> classes = Map.of(
            "USER", User.class,
            "ITEM", Item.class
    );
    private static final Map<String, Object> objects = new HashMap<>();
    // supported types are String, Integer, Double, Boolean, Long
//    static Map<Class<?>, Function<String, ?>> parsers = Map.of(
//            Integer.class, Integer::parseInt,
//            Long.class, Long::parseLong,
//            Double.class, Double::parseDouble,
//            String.class, Function.identity(),
//            Boolean.class, Boolean::parseBoolean
//    );

    static Map<String, Function<String, ?>> parsers = Map.of(
            "INTEGER", Integer::parseInt,
            "LONG", Long::parseLong,
            "DOUBLE", Double::parseDouble,
            "STRING", Function.identity(),
            "BOOLEAN", Boolean::parseBoolean
    );

    public static <T> T parseLine(String line, Function<String, T> parser) {
        return parser.apply(line);
    }

    static void parseCommand(String input) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        String lscRegex = "lsc";
        String lsoRegex = "lso";
        String newClassRegex = "new\\s+(\\w+)";
        String editObjectRegex = "edit\\s+(\\w+)";
        String lsmRegex = "lsm\\s+(\\w+)";
        String lsvClassRegex = "lsvc\\s+(\\w+)";
        String lsvObjectRegex = "lsvo\\s+(\\w+)";
        String callMethodRegex = "call\\s+(\\w+)\\s+(\\w+)(\\s+([\\S\\s]+))*";
        String rmObjectRegex = "rm\\s+(\\w+)";
        String breakRegex = "break";


        if (input.matches(lscRegex)) {
            classes.keySet().forEach(System.out::println);
        } else if (input.matches(lsoRegex)) {
            objects.keySet().forEach(System.out::println);
        } else if (input.matches(newClassRegex)) {
            Matcher matcher = Pattern.compile(newClassRegex).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                Optional<Constructor<?>[]> constructors = classes.entrySet().stream()
                        .filter((c) -> c.getKey().equals(className))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .map(Class::getConstructors);
                if (constructors.isPresent()) {
                    Constructor<?> defaultConstructor = Arrays.stream(constructors.get()).filter(c -> c.getParameterCount() == 0).findFirst().get();
                    Object object = defaultConstructor.newInstance();
                    objects.put(className + object.hashCode(), object);
                    System.out.println("New instance of class: " + className + " hash code: " + className + object.hashCode());
                } else {
                    System.out.println("Failed to make instance of class: " + className + " Cause: no class with that name");
                }
            }
        } else if (input.matches(rmObjectRegex)) {
            Matcher matcher = Pattern.compile(rmObjectRegex).matcher(input);
            if (matcher.find()) {
                String objectName = matcher.group(1).toUpperCase();
                if (objects.remove(objectName) != null) {
                    System.out.println(objectName + " was removed");
                } else {
                    System.out.println("no object was removed");
                }
            } else {
                System.out.println("Trash");
            }
        } else if (input.matches(lsvClassRegex)) {
            Matcher matcher = Pattern.compile(lsvClassRegex).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                if (classes.containsKey(className))
                    Arrays.stream(classes.get(className).getDeclaredFields()).map(Param::fromField).forEach(System.out::println);
                else System.out.println("NO");
            }
        } else if (input.matches(lsvObjectRegex)) {
            Matcher matcher = Pattern.compile(lsvObjectRegex).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                if (objects.containsKey(className)) {

                    Object object = objects.get(className);
                    Field[] declaredFields = object.getClass().getDeclaredFields();
                    Arrays.stream(declaredFields)
                            .map(f -> ParamWithValue.fromField(f, object)).
                            forEach(System.out::println);
                } else System.out.println("NO");
            }
        } else if (input.matches(lsmRegex)) {
            Matcher matcher = Pattern.compile(lsmRegex).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                if (classes.containsKey(className)) {
                    Predicate<Object> isObject = Object.class::equals;
                    Predicate<java.lang.reflect.Method> isMethodFromObject = method -> isObject.test(method.getDeclaringClass());
                    java.lang.reflect.Method[] methods = classes.get(className).getMethods();
                    Arrays.stream(methods)
                            .filter(isMethodFromObject.negate())
                            .map(Method::fromMethod)
                            .forEach(System.out::println);
                } else System.out.println("NO");
            }
        } else if (input.matches(callMethodRegex)) {
            Pattern pattern = Pattern.compile(callMethodRegex);
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches()) {
                String objectName = matcher.group(1).toUpperCase();
                String methodName = matcher.group(2).toUpperCase();
                String paramsGroup = matcher.group(4);

                System.out.println("Object Name: " + objectName);
                System.out.println("Method Name: " + methodName);
                System.out.println("Parameter: " + paramsGroup.trim());

                Object object = objects.get(objectName);
                if (object != null) {
                    java.lang.reflect.Method[] methods = object.getClass().getMethods();
                    Optional<java.lang.reflect.Method> methodOpt = Arrays.stream(methods).filter(m -> m.getName().toUpperCase().equals(methodName)).findFirst();
                    if (methodOpt.isPresent()) {
                        java.lang.reflect.Method method = methodOpt.get();
                        method.setAccessible(true);
                        Method simpleMethod = Method.fromMethod(method);
                        String[] parsedParams = paramsGroup.split(" ");
                        List<Param> params = simpleMethod.params();
                        if (params.isEmpty())
                        {
                            if (method.getDeclaringClass() == Void.class)
                                method.invoke(object);
                            else
                                System.out.println(method.invoke(object));
                        } else {
                            if (params.size() != parsedParams.length) {
                                System.out.println("wrong number if parameters");
                            } else {
                                List<Object> parsedValue = new ArrayList<>();
                                try {
                                    for (int i = 0; i < parsedParams.length; i++) {
                                        parsedValue.add(parsers.get(params.get(i).type().toUpperCase()).apply(parsedParams[i]));
                                    }
                                    if (method.getDeclaringClass() == Void.class)
                                        method.invoke(object, parsedValue.toArray());
                                    else
                                        System.out.println(method.invoke(object, parsedValue.toArray()));
                                } catch (Exception e) {
                                    System.out.println("Wrong type of the param error: " + e.getMessage());
                                }
                            }
                        }
                    } else {
                        System.out.println("no method with this name: " + methodName);
                    }

                } else {
                    System.out.println("no object with this name: " + objectName);
                }
            } else {
                System.out.println("Input does not match the pattern.");
            }
        } else if (input.matches(breakRegex)) {
            System.exit(0);
        } else {
            System.out.println("Please enter a valid command");
        }

    }

    //    static Boolean  parsePram(Class<Boolean> paramType, String value) {
//        // supported types are String, Integer, Double, Boolean, Long
//    }
//    static Integer  parsePram(Class<Integer> paramType, String value) {
//        // supported types are String, Integer, Double, Boolean, Long
//        return (Integer) parsers.get(paramType).apply(value);
//    }
//
    public static void main(String[] args) {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {

            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

            while (true) {
                System.out.print("-> ");
                String line = lineReader.readLine();
                if (line.isBlank()) continue;
                parseCommand(line.strip());
            }
        } catch (IOException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println(e.getMessage());
        } catch (EndOfFileException e) {
            System.out.println("Ctrl-D was pressed, Exiting.");
        }

    }

//    static private enum State {
//        CLASS_INTRODUCTION,
//        FIELD_INITIALIZATION,
//        FIELD_MANIPULATION,
//        METHOD_CALLS
//    }


    record Method(String name, String returnType, List<Param> params) {
        static Method fromMethod(java.lang.reflect.Method method) {
            Parameter[] parameters = method.getParameters();
            List<Param> list = Arrays.stream(parameters).map(Param::fromParameter).toList();
            return new Method(method.getName(), method.getReturnType().getSimpleName(), list);
        }

        @Override
        public String toString() {
            return returnType + " : " + name + " " + params.stream().map(Param::toString).collect(Collectors.joining(", ", "(", ")"));
        }
    }

    record Param(String type, String name) {
        static Param fromField(Field field) {
            return new Param(field.getType().getSimpleName(), field.getName());
        }

        static Param fromParameter(Parameter parameter) {
            return new Param(parameter.getType().getSimpleName(), parameter.getName());
        }

        @Override
        public String toString() {
            return name + " : " + type;
        }
    }

    record ParamWithValue<T>(String type, String name, T value) {
        static <T> ParamWithValue<T> fromField(Field field, Object object) {
            field.setAccessible(true);
            try {

                return new ParamWithValue<T>(field.getType().getSimpleName(), field.getName(), (T) field.get(object));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return name + " : " + type + " == " + value;
        }
    }
}
