package org.example;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

final class SimpleMethod {
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
