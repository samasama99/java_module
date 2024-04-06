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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
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
        String lscRegex = "list classes";
        String lsoRegex = "list objects";
        String lsmRegex = "list methods\\s+(\\w+)";
        String lsvObjectRegex = "list fields\\s+(\\w+)";
        String rmObjectRegex = "remove\\s+(\\w+)";
        String infoObjectRegex = "info\\s+(\\w+)";
        String newClassRegex = "new\\s+(\\w+)(\\s+([\\S\\s]+))*";
        String editObjectRegex = "edit\\s+(\\w+)\\s+(\\w+)\\s+(\\w+)";
        String callMethodRegex = "call\\s+(\\w+)\\s+(\\w+)(\\s+([\\S\\s]+))*";


        if (input.matches(lscRegex)) {
            classes.values().stream().map(c -> c.name + Arrays.stream(c.constructor().getParameters()).map(p -> p.getName() + " : " + p.getType().getSimpleName()).collect(Collectors.joining(" , ", "( ", " )"))).forEach(System.out::println);
        } else if (input.matches(lsoRegex)) {
            objects.keySet().forEach(System.out::println);
        } else if (input.matches(infoObjectRegex)) {
            Matcher matcher = Pattern.compile(infoObjectRegex).matcher(input);
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
        } else if (input.matches(newClassRegex)) {
            Matcher matcher = Pattern.compile(newClassRegex).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                String group = matcher.group(3);
                String paramsGroup = group == null ? null : group.trim();
                SimpleClass simpleClass = classes.get(className);

                if (simpleClass != null) {
                    Object[] args = new Object[simpleClass.constructor().getParameterCount()];
                    String[] parameters = paramsGroup.split(" ");
                    if (parameters.length != args.length) {
                        System.out.println("not enough arguments for the constructor");
                        return;
                    }
                    int index = 0;
                    for (var type : simpleClass.constructor().getParameterTypes()) {
                        System.out.println(type.getSimpleName().toUpperCase());
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
        } else if (input.matches(rmObjectRegex)) {
            Matcher matcher = Pattern.compile(rmObjectRegex).matcher(input);
            if (matcher.find()) {
                String group = matcher.group(1);
                String objectName = group == null ? null : group.toUpperCase();
                if (objects.remove(objectName) != null) {
                    System.out.println(objectName + " was removed");
                } else {
                    System.out.println("no object was removed");
                }
            }
        } else if (input.matches(lsvObjectRegex)) {
            Matcher matcher = Pattern.compile(lsvObjectRegex).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                if (objects.containsKey(className)) {
                    SimpleObject object = objects.get(className);
                    object.simpleFields.values().forEach(System.out::println);
                } else System.out.println("NO");
            }
        } else if (input.matches(lsmRegex)) {
            Matcher matcher = Pattern.compile(lsmRegex).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                if (objects.containsKey(className)) {
                    objects.get(className).simpleMethods().values().forEach(System.out::println);
                } else System.out.println("NO");
            }
        } else if (input.matches(editObjectRegex)) {

            Pattern pattern = Pattern.compile(editObjectRegex);
            Matcher matcher = pattern.matcher(input);

            if (matcher.find()) {
                String objectName = matcher.group(1).toUpperCase().trim();
                String fieldName = matcher.group(2).toUpperCase().trim();
                String value = matcher.group(3);

                SimpleObject simpleObject = objects.get(objectName);
                if (simpleObject != null) {
                    SimpleField simpleField = simpleObject.simpleField().get(fieldName);
                    Object apply = parsers.get(simpleField.type().toUpperCase()).apply(value);
                    System.out.println(apply);
                    simpleField.setValue(apply);
                } else {
                    System.out.println("no object with this name: " + objectName);
                }
            }

        } else if (input.matches(callMethodRegex)) {
            Pattern pattern = Pattern.compile(callMethodRegex);
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

    @FunctionalInterface
    public interface VarArgFunction {
        Object invoke(Object... args);
    }

    @FunctionalInterface
    interface SimpleMethodInterface {
        Object invoke(Object... args);
    }

    record SimpleClass(String name,
                       Constructor<?> constructor,
                       Class<?> originalClass) {
        static private final Predicate<Object> isObject = Object.class::equals;
        static private final Predicate<java.lang.reflect.Method> isMethodFromObject = method -> isObject.test(method.getDeclaringClass());

        static public SimpleClass createNewSimpleClass(Class<?> c) {
            String name = c.getSimpleName();

            List<Parameter> parameters = Arrays.stream(c.getDeclaredFields()).map(Parameter::fromField).toList();

            int numberOfParams = parameters.size();

            Constructor<?> parameterizedConstructor = Arrays.stream(c.getConstructors())
                    .filter(constructor -> constructor.getParameterCount() == numberOfParams)
                    .findFirst()
                    .orElseThrow(NoValidParameterizedConstructor::new);

            return new SimpleClass(name, parameterizedConstructor, c);
        }

        SimpleObject newInstance(Object... args) {
            try {
                Object object = constructor.newInstance(args);
                Map<String, SimpleMethod> simpleMethods = Arrays
                        .stream(originalClass.getMethods())
                        .filter(isMethodFromObject.negate())
                        .map((m) -> SimpleMethod.fromMethod(m, object))
                        .collect(Collectors.toMap(
                                (method) -> method.name().toUpperCase(),
                                Function.identity()
                        ));
                Map<String, SimpleField> simpleFields = Arrays
                        .stream(originalClass.getDeclaredFields())
                        .map((f) -> SimpleField.fromField(f, object))
                        .collect(Collectors.toMap(
                                (field) -> field.name().toUpperCase(),
                                Function.identity()
                        ));

                return new SimpleObject(this, name.toUpperCase() + object.hashCode(), object, simpleMethods, simpleFields);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


        static public class NoValidParameterizedConstructor extends RuntimeException {
            public NoValidParameterizedConstructor() {
                super();
            }
        }
    }

    static final class SimpleObject {
        private final SimpleClass simpleClass;
        private final String objectName;
        private final Object object;
        private final Map<String, SimpleMethod> simpleMethods;
        private final Map<String, SimpleField> simpleFields;

        SimpleObject(SimpleClass simpleClass,
                     String objectName,
                     Object object,
                     Map<String, SimpleMethod> simpleMethods,
                     Map<String, SimpleField> simpleFields
        ) {
            this.simpleClass = simpleClass;
            this.objectName = objectName;
            this.object = object;
            this.simpleMethods = simpleMethods;
            this.simpleFields = simpleFields;
        }

        public SimpleClass simpleClass() {
            return simpleClass;
        }

        public String objectName() {
            return objectName;
        }

        public Map<String, SimpleMethod> simpleMethods() {
            return simpleMethods;
        }

        public Map<String, SimpleField> simpleField() {
            return simpleFields;
        }

    }

    static final class SimpleMethod implements SimpleMethodInterface {
        private final String name;
        private final String returnType;
        private final List<Parameter> parameters;
        private final VarArgFunction invokable;

        SimpleMethod(String name, String returnType, List<Parameter> parameters,
                     VarArgFunction invokable) {
            this.name = name;
            this.returnType = returnType;
            this.parameters = parameters;
            this.invokable = invokable;
        }

        static SimpleMethod fromMethod(Method method, Object object) {
            java.lang.reflect.Parameter[] parameters = method.getParameters();
            List<Parameter> parameterList = Arrays.stream(parameters).map(Parameter::fromParameter).toList();
            return new SimpleMethod(method.getName(), method.getReturnType().getSimpleName(), parameterList, (Object... args) -> {
                try {
                    return method.invoke(object, args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        public Object invoke(Object... args) {
            return invokable.invoke(args);
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