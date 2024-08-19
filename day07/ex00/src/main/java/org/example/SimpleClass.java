package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
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
        var parameters = c.getDeclaredFields();
        int numberOfParams = parameters.length;

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
                    .filter(isInheritedFromObject.negate())
                    .map((m) -> SimpleMethod.fromMethod(m, object))
                    .collect(Collectors.toMap(
                            SimpleMethod::name,
                            Function.identity()
                    ));

            Map<String, SimpleField> simpleFields = Arrays
                    .stream(originalClass.getDeclaredFields())
                    .map((f) -> SimpleField.fromReflectField(f, object))
                    .collect(Collectors.toMap(
                            SimpleField::name,
                            Function.identity()
                    ));

            return new SimpleObject(name + object.hashCode(), simpleMethods, simpleFields);
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
