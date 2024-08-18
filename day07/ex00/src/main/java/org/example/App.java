package org.example;

import org.example.item.Item;
import org.example.user.User;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App {
    private static final Map<String, SimpleClass> classes = Map.of(
            "USER", SimpleClass.createNewSimpleClass(User.class),
            "ITEM", SimpleClass.createNewSimpleClass(Item.class)
    );

    private static final Map<String, SimpleObject> objects = new HashMap<>();

    static Map<String, Function<String, ?>> parsers = Map.of(
            "INT", Integer::parseInt,
            "INTEGER", Integer::parseInt,
            "LONG", Long::parseLong,
            "DOUBLE", Double::parseDouble,
            "STRING", Function.identity(),
            "BOOLEAN", Boolean::parseBoolean
    );

    static void parseCommand(String input) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        String lsc = "lsc";
        String lso = "lso";
        String infoObject = "io\\s+(\\w+)";
        String rmObject = "rm\\s+(\\w+)";
        String infoClass = "ic\\s+(\\w+)";
        String newClass = "new\\s+(\\w+)(\\s+([\\S\\s]+))*";
        String editObject = "edit\\s+(\\w+)\\s+(\\w+)\\s+(\\w+)";
        String callMethod = "call\\s+(\\w+)\\s+(\\w+)(\\s+([\\S\\s]+))*";


        if (input.equals(lsc)) {
            classes.values().stream().map(c -> c.name() + Arrays.stream(c.constructor().getParameters()).map(p -> p.getName() + " : " + p.getType().getSimpleName()).collect(Collectors.joining(" , ", "( ", " )"))).forEach(System.out::println);
        } else if (input.equals(lso)) {
            objects.keySet().forEach(System.out::println);
        } else if (input.matches(infoClass)) {
            Matcher matcher = Pattern.compile(infoClass).matcher(input);
            if (matcher.find()) {
                String group = matcher.group(1);
                String className = group == null ? null : group.toUpperCase();
                SimpleClass simpleClass = classes.get(className);
                if (simpleClass != null) {
                    System.out.println(simpleClass);
                } else {
                    System.out.println("no class is found with this name `" + className + "`");
                }
            }
        } else if (input.matches(newClass)) {
            Matcher matcher = Pattern.compile(newClass).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                String group = matcher.group(3);
                String paramsGroup = group == null ? null : group.trim();
                SimpleClass simpleClass = classes.get(className);

                if (simpleClass != null) {
                    Object[] args = new Object[simpleClass.constructor().getParameterCount()];
                    assert paramsGroup != null;
                    String[] parameters = paramsGroup.split(" ");
                    if (parameters.length != args.length) {
                        System.out.println("not enough arguments for the constructor");
                        return;
                    }
                    int index = 0;
                    for (var type : simpleClass.constructor().getParameterTypes()) {
                        args[index] = parsers.get(type.getSimpleName().toUpperCase()).apply(parameters[index]);
                        index++;
                    }
                    SimpleObject simpleObject = simpleClass.newInstance(args);
                    objects.put(simpleObject.objectName(), simpleObject);
                    System.out.println("New instance of class: " + className + " hash code: " + simpleObject.objectName());
                } else {
                    System.out.println("Failed to make instance of class: " + className + " Cause: no class with that name");
                }
            }
        } else if (input.matches(rmObject)) {
            Matcher matcher = Pattern.compile(rmObject).matcher(input);
            if (matcher.find()) {
                String group = matcher.group(1);
                String objectName = group == null ? null : group.toUpperCase();
                if (objects.remove(objectName) != null) {
                    System.out.println(objectName + " was removed");
                } else {
                    System.out.println("no object was removed");
                }
            }
        } else if (input.matches(infoObject)) {
            Matcher matcher = Pattern.compile(infoObject).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                if (objects.containsKey(className)) {
                    SimpleObject object = objects.get(className);
                    object.simpleFields().values().forEach(System.out::println);
                    objects.get(className).simpleMethods().values().forEach(System.out::println);
                } else System.out.println("NO");
            }
        } else if (input.matches(editObject)) {

            Pattern pattern = Pattern.compile(editObject);
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                String objectName = matcher.group(1).toUpperCase().trim();
                String fieldName = matcher.group(2).toUpperCase().trim();
                String value = matcher.group(3);

                SimpleObject simpleObject = objects.get(objectName);
                if (simpleObject != null) {
                    SimpleField simpleField = simpleObject.simpleFields().get(fieldName);
                    Object apply = parsers.get(simpleField.type().toUpperCase()).apply(value);
                    System.out.println(apply);
                    simpleField.setValue(apply);
                } else {
                    System.out.println("no object with this name: " + objectName);
                }
            }

        } else if (input.matches(callMethod)) {
            Pattern pattern = Pattern.compile(callMethod);
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                String objectName = matcher.group(1).toUpperCase().trim();
                String methodName = matcher.group(2).toUpperCase().trim();
                String group = matcher.group(4);
                String paramsGroup = group == null ? null : group.trim();

                SimpleObject object = objects.get(objectName);
                if (object != null) {
                    Optional<SimpleMethod> methodOpt = Optional.ofNullable(object.simpleMethods().get(methodName));
                    if (methodOpt.isPresent()) {
                        SimpleMethod simpleMethod = methodOpt.get();
                        List<Parameter> parameters = simpleMethod.params();

                        if (parameters.isEmpty()) {
                            if (simpleMethod.returnType.equals("VOID")) {
                                simpleMethod.invoke();
                            } else
                                System.out.println(simpleMethod.invoke());
                        } else if (paramsGroup != null) {
                            String[] parsedParams = paramsGroup.split(" ");
                            if (parameters.size() != parsedParams.length) {
                                System.out.println("wrong amount of parameters");
                            } else {
                                List<Object> parsedValue = new ArrayList<>();
                                try {
                                    for (int i = 0; i < parsedParams.length; i++) {
                                        parsedValue.add(parsers.get(parameters.get(i).type().toUpperCase()).apply(parsedParams[i]));
                                    }
                                    if (simpleMethod.returnType.equals("VOID"))
                                        simpleMethod.invoke(parsedValue.toArray());
                                    else
                                        System.out.println(simpleMethod.invoke(parsedValue.toArray()));
                                } catch (Exception e) {
                                    System.out.println("Wrong type of the param error: " + e.getMessage());
                                }
                            }
                        } else {
                            System.out.println("wrong amount of parameters");
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
        } else {
            System.out.println("Please enter a valid command");
        }

    }

    public static void main(String[] args) {
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {

            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();
            DefaultHistory history = new DefaultHistory();
            history.attach(lineReader);


            while (true) {
                System.out.print("-> ");
                String line = lineReader.readLine().strip();
                if (line.isBlank()) continue;
                if (line.equalsIgnoreCase("BREAK")) break;
                parseCommand(line);
                history.add(line);
            }
        } catch (IOException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            System.out.println(e.getMessage());
        } catch (EndOfFileException e) {
            System.out.println("Ctrl-D was pressed, Exiting.");
        }
    }

    static final class SimpleMethod {
        private final String name;
        private final String returnType;
        private final List<Parameter> parameters;
        private final Function<Object[], ?> invokable;

        SimpleMethod(String name, String returnType, List<Parameter> parameters,
                     Function<Object[], ?> invokable) {
            this.name = name;
            this.returnType = returnType;
            this.parameters = parameters;
            this.invokable = invokable;
        }

        static SimpleMethod fromMethod(Method method, Object object) {
            java.lang.reflect.Parameter[] parameters = method.getParameters();
            List<Parameter> parameterList = Arrays.stream(parameters).map(Parameter::fromParameter).toList();
            return new SimpleMethod(method.getName(), method.getReturnType().getSimpleName(), parameterList, (Object[] args) -> {
                try {
                    return method.invoke(object, args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        public Object invoke(Object... args) {
            return invokable.apply(args);
//            return invokable.invoke(args);
        }


        @Override
        public String toString() {
            return returnType + " : " + name + " " + parameters.stream().map(Parameter::toString).collect(Collectors.joining(", ", "(", ")"));
        }

        public String name() {
            return name;
        }

        public String returnType() {
            return returnType;
        }

        public List<Parameter> params() {
            return parameters;
        }
    }

    record Parameter(String type, String name) {
        static Parameter fromField(Field field) {
            return new Parameter(field.getType().getSimpleName(), field.getName());
        }

        static Parameter fromParameter(java.lang.reflect.Parameter parameter) {
            return new Parameter(parameter.getType().getSimpleName(), parameter.getName());
        }

        @Override
        public String toString() {
            return type;
        }
    }

    static final class SimpleField {
        private final String type;
        private final String name;
        private final Field field;
        private final Object object;

        private SimpleField(String type, String name, Field field, Object object) {
            this.type = type;
            this.name = name;
            this.field = field;
            this.object = object;
        }

        static SimpleField fromField(Field field, Object object) {
            field.setAccessible(true);
            try {
                return new SimpleField(field.getType().getSimpleName(), field.getName(), field, object);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            try {
                return name + " : " + type + " == " + field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        public String type() {
            return type;
        }

        public String name() {
            return name;
        }

        Object getValue() {
            try {
                return field.get(this.object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        void setValue(Object newValue) {
            try {
                field.set(this.object, newValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }
}