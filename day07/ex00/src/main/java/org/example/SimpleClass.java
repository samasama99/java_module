package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

record SimpleClass(String name,
                   Constructor<?> constructor,
                   Class<?> originalClass) {
    static private final Predicate<Method> isInheritedFromObject = method -> method.getDeclaringClass() == Object.class;


    static public SimpleClass createNewSimpleClass(Class<?> c) {
        String name = c.getSimpleName();

        List<App.Parameter> parameters = Arrays.stream(c.getDeclaredFields()).map(App.Parameter::fromField).toList();

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

            Map<String, App.SimpleMethod> simpleMethods = Arrays
                    .stream(originalClass.getMethods())
                    .filter(isInheritedFromObject.negate())
                    .map((m) -> App.SimpleMethod.fromMethod(m, object))
                    .collect(Collectors.toMap(
                            (method) -> method.name().toUpperCase(),
                            Function.identity()
                    ));

            Map<String, App.SimpleField> simpleFields = Arrays
                    .stream(originalClass.getDeclaredFields())
                    .map((f) -> App.SimpleField.fromField(f, object))
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
