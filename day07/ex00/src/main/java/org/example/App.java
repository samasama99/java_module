package org.example;

import org.example.item.Item;
import org.example.user.User;

import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    private static final Map<String, SimpleClass> classes = Map.of(
            User.class.getSimpleName(), SimpleClass.createNewSimpleClass(User.class),
            Item.class.getSimpleName(), SimpleClass.createNewSimpleClass(Item.class)
    );

    private static final Map<String, SimpleObject> objects = new HashMap<>();

    static Map<Class<?>, Function<String, ?>> parsers = Map.of(
            int.class, Integer::parseInt,
            long.class, Long::parseLong,
            double.class, Double::parseDouble,
            String.class, Function.identity(),
            boolean.class, Boolean::parseBoolean
    );

    static void parseAndExecuteACommand(String input) {
        String trimmedInput = input.trim();
        String commandPart = trimmedInput.split("\\s+", 2)[0].toLowerCase();
        String parametersPart = trimmedInput.length() > commandPart.length() ? trimmedInput.substring(commandPart.length()).trim() : "";

        switch (commandPart) {
            case "class" -> {
                if (parametersPart.isEmpty()) {
                    classes.values().forEach(c ->
                            {
                                String className = c.name();
                                Stream<Parameter> parameterStream = Arrays.stream(c.constructor().getParameters());
                                System.out.println(
                                        className + parameterStream
                                                .map(p -> p.getName() + " : " + p.getType().getSimpleName())
                                                .collect(Collectors.joining(", ", "(", ")"))
                                );
                            }
                    );
                } else {
                    String className = parametersPart.trim();
                    SimpleClass simpleClass = classes.get(className);
                    if (simpleClass != null) {
                        System.out.println(simpleClass);
                    } else {
                        System.out.println("No class found with this name `" + className + "`");
                    }
                }
            }
            case "object" -> {
                if (parametersPart.isEmpty()) {
                    objects.keySet().forEach(System.out::println);
                } else {
                    String objectName = parametersPart.trim();
                    SimpleObject object = objects.get(objectName);
                    if (object != null) {
                        object.simpleFields().values().forEach(System.out::println);
                        object.simpleMethods().values().forEach(System.out::println);
                    } else {
                        System.out.println("NO");
                    }
                }
            }
            case "remove" -> {
                String objectName = parametersPart.trim().toUpperCase();
                if (objects.remove(objectName) != null) {
                    System.out.println(objectName + " was removed");
                } else {
                    System.out.println("No object was removed");
                }
            }
            case "new" -> {
                String[] parts = parametersPart.split("\\s+");
                if (parts.length < 1) {
                    System.out.println("Please provide the class name and arguments if needed.");
                    return;
                }

                String className = parts[0].trim();
                SimpleClass simpleClass = classes.get(className);

                if (simpleClass != null) {
                    int parameterCount = simpleClass.constructor().getParameterCount();
                    if (parts.length < parameterCount + 1) {
                        System.out.println("Too few arguments for the constructor");
                        return;
                    } else if (parts.length > parameterCount + 1) {
                        System.out.println("Too many arguments for the constructor");
                        return;
                    }

                    Object[] args = new Object[parameterCount];
                    Class<?>[] parameterTypes = simpleClass.constructor().getParameterTypes();

                    for (int i = 0; i < parameterCount; i++) {
                        Function<String, ?> parser = parsers.get(parameterTypes[i]);
                        if (parser == null) {
                            System.out.println("No parser found for type: " + parameterTypes[i].getSimpleName());
                            return;
                        }
                        args[i] = parser.apply(parts[i + 1]);
                    }

                    SimpleObject simpleObject = simpleClass.newInstance(args);
                    objects.put(simpleObject.objectName(), simpleObject);
                    System.out.println("New instance of class: " + className + " hash code: " + simpleObject.objectName());
                } else {
                    System.out.println("Failed to create instance of class: " + className + " Cause: no class with that name");
                }
            }
            case "edit" -> {
                String[] parts = parametersPart.split("\\s+", 3);
                if (parts.length < 3) {
                    System.out.println("Insufficient arguments for 'edit' command.");
                    return;
                }

                String objectName = parts[0].trim();
                String fieldName = parts[1].trim();
                String value = parts[2].trim();

                SimpleObject simpleObject = objects.get(objectName);
                if (simpleObject != null) {
                    SimpleField simpleField = simpleObject.simpleFields().get(fieldName);
                    if (simpleField != null) {
                        Object apply = parsers.get(simpleField.type()).apply(value);
                        simpleField.setValue(apply);
                        System.out.println("Field updated: " + fieldName + " = " + apply);
                    } else {
                        System.out.println("No field with this name: " + fieldName);
                    }
                } else {
                    System.out.println("No object with this name: " + objectName);
                }
            }
            case "call" -> {
                String[] parts = parametersPart.split("\\s+", 3);
                if (parts.length < 2) {
                    System.out.println("Insufficient arguments for 'call' command.");
                    return;
                }

                String objectName = parts[0].toUpperCase().trim();
                String methodName = parts[1].toUpperCase().trim();
                String paramsGroup = parts.length > 2 ? parts[2].trim() : "";

                SimpleObject object = objects.get(objectName);
                if (object != null) {
                    Optional<SimpleMethod> methodOpt = Optional.ofNullable(object.simpleMethods().get(methodName));
                    if (methodOpt.isPresent()) {
                        SimpleMethod simpleMethod = methodOpt.get();
                        java.lang.reflect.Parameter[] parameters = simpleMethod.params();
                        String[] rawParams = paramsGroup.isEmpty() ? new String[0] : paramsGroup.split("\\s+");

                        if (parameters.length == 0) {
                            System.out.println(simpleMethod.returnType().equals("VOID") ? "Method invoked" : simpleMethod.invoke());
                        } else if (rawParams.length != parameters.length) {
                            System.out.println("Wrong number of parameters.");
                        } else {
                            Object[] parsedParams = new Object[rawParams.length];
                            try {
                                for (int i = 0; i < rawParams.length; i++) {
                                    Parameter parameter = parameters[i];
                                    Function<String, ?> parser = parsers.get(parameter.getType());
                                    if (parser == null) {
                                        System.out.println("No parser found for type: " + parameter.getType().getSimpleName());
                                        return;
                                    }
                                    parsedParams[i] = parser.apply(rawParams[i]);
                                }
                                System.out.println(simpleMethod.returnType().equals("VOID") ? "Method invoked" : simpleMethod.invoke(parsedParams));
                            } catch (Exception e) {
                                System.out.println("Error invoking method: " + e.getMessage());
                            }
                        }
                    } else {
                        System.out.println("No method with this name: " + methodName);
                    }
                } else {
                    System.out.println("No object with this name: " + objectName);
                }
            }
            default -> {
                System.out.println("Please enter a valid command");
            }
        }
    }


    public static void main(String[] args) {

        System.out.println(classes);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Type 'BREAK' to exit.");

            while (true) {
                System.out.print("-> ");
                String line = scanner.nextLine().strip();

                if (line.isBlank()) continue;

                if (line.equalsIgnoreCase("BREAK")) break;

                try {
                    parseAndExecuteACommand(line);
                } catch (Exception e) {
                    System.out.println("Error executing command: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

}