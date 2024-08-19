package org.example;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

final class SimpleMethod {
    private final String name;
    private final String returnType;
    private final Parameter[] parameters;
    private final Function<Object[], ?> invokable;

    SimpleMethod(String name, String returnType, Parameter[] parameters,
                 Function<Object[], ?> invokable) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.invokable = invokable;
    }

    static SimpleMethod fromMethod(Method method, Object object) {
        Parameter[] parameters = method.getParameters();
        return new SimpleMethod(method.getName(), method.getReturnType().getSimpleName(), parameters, (Object[] args) -> {
            try {
                return method.invoke(object, args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Object invoke(Object... args) {
        return invokable.apply(args);
    }


    @Override
    public String toString() {
        return returnType + " : " + name + " " + Arrays.stream(parameters).peek(System.out::println).map(p -> p.getType().getSimpleName()).collect(Collectors.joining(", ", "(", ")"));
    }

    public String name() {
        return name;
    }

    public String returnType() {
        return returnType;
    }

    public Parameter[] params() {
        return parameters;
    }
}
