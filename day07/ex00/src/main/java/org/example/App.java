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
import java.lang.reflect.Method;
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
    private static final Map<String, SimpleClass> classes = Map.of(
            "USER", SimpleClass.createNewSimpleClass(User.class),
            "ITEM", SimpleClass.createNewSimpleClass(Item.class)
    );
    private static final Map<String, SimpleObject> objects = new HashMap<>();
    static Map<String, Function<String, ?>> parsers = Map.of(
            "INTEGER", Integer::parseInt,
            "LONG", Long::parseLong,
            "DOUBLE", Double::parseDouble,
            "STRING", Function.identity(),
            "BOOLEAN", Boolean::parseBoolean
    );

    static void parseCommand(String input) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        String lscRegex = "lsc";
        String lsoRegex = "lso";
        String newClassRegex = "new\\s+(\\w+)";
        String editObjectRegex = "edit\\s+(\\w+)";
        String lsmRegex = "lsm\\s+(\\w+)";
//        String lsvClassRegex = "lsvc\\s+(\\w+)";
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
                SimpleClass simpleClass = classes.get(className);
                if (simpleClass != null) {
                    SimpleObject simpleObject = simpleClass.newInstance();
                    objects.put(simpleObject.objectName(), simpleObject);
                    System.out.println("New instance of class: " + className + " hash code: " + simpleObject.objectName());
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
            }
        } else if (input.matches(lsvObjectRegex)) {
            Matcher matcher = Pattern.compile(lsvObjectRegex).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                if (objects.containsKey(className)) {
                    Object object = objects.get(className);
                    Field[] declaredFields = object.getClass().getDeclaredFields();
                    Arrays.stream(declaredFields)
                            .map(f -> SimpleField.fromField(f, object)).
                            forEach(System.out::println);
                } else System.out.println("NO");
            }
        } else if (input.matches(lsmRegex)) {
            Matcher matcher = Pattern.compile(lsmRegex).matcher(input);
            if (matcher.find()) {
                String className = matcher.group(1).toUpperCase();
                if (objects.containsKey(className)) {
                    objects.get(className).simpleMethods().keySet().forEach(System.out::println);
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

                SimpleObject object = objects.get(objectName);
                if (object != null) {
                    Optional<SimpleMethod> methodOpt = object.simpleMethods().entrySet().stream().filter(m -> m.getKey().toUpperCase().equals(methodName)).findFirst().map(Map.Entry::getValue);
                    if (methodOpt.isPresent()) {
//                        java.lang.reflect.Method method = methodOpt.get();
//                        method.setAccessible(true);
                        SimpleMethod simpleMethod = methodOpt.get();
                        List<Parameter> parameters = simpleMethod.params();

                        String[] parsedParams = paramsGroup.split(" ");
                        if (parameters.isEmpty()) {
                            if (simpleMethod.returnType.equals("VOID")) {
                                simpleMethod.invoke();
                            } else
                                System.out.println(simpleMethod.invoke());
                        } else {
                            if (parameters.size() != parsedParams.length) {
                                System.out.println("wrong number if parameters");
                            } else {
                                List<Object> parsedValue = new ArrayList<>();
                                try {
                                    for (int i = 0; i < parsedParams.length; i++) {
                                        parsedValue.add(parsers.get(parameters.get(i).type().toUpperCase()).apply(parsedParams[i]));
                                    }
                                    if (simpleMethod.returnType.equals("VOID"))
                                        simpleMethod.invoke( parsedValue.toArray());
                                    else
                                        System.out.println(simpleMethod.invoke(parsedValue.toArray()));
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
//                    .filter(constructor -> constructor.getParameterCount() == numberOfParams)
                    .filter(constructor -> constructor.getParameterCount() == 0)
                    .findFirst()
                    .orElseThrow(NoParameterizedConstructor::new);

            return new SimpleClass(name, parameterizedConstructor, c);
        }

        SimpleObject newInstance() {
            try {
                Object object = constructor.newInstance();
                Map<String, SimpleMethod> simpleMethods = Arrays.stream(originalClass.getMethods())
                        .filter(isMethodFromObject.negate())
                        .map((m) -> SimpleMethod.fromMethod(m, object))
                        .collect(Collectors.toMap(
                                SimpleMethod::name,
                                Function.identity()
                        ));
                Map<String, SimpleField> simpleFields = Arrays.stream(originalClass.getDeclaredFields())
                        .map((f) -> SimpleField.fromField(f, object))
                        .collect(Collectors.toMap(
                                SimpleField::name,
                                Function.identity()
                        ));
                return new SimpleObject(this, name.toUpperCase() + object.hashCode(), object, simpleMethods, simpleFields);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        static public class NoParameterizedConstructor extends RuntimeException {
            public NoParameterizedConstructor() {
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
            List<Parameter> list = Arrays.stream(parameters).map(Parameter::fromParameter).toList();
            return new SimpleMethod(method.getName(), method.getReturnType().getSimpleName(), list, (Object... args) -> {
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
            return name + " : " + type;
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