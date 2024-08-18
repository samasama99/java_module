package org.example;

import java.lang.reflect.Field;

record Parameter(Class<?> type, String name) {
    static Parameter fromField(Field field) {
        return new Parameter(field.getType(), field.getName());
    }

    static Parameter fromParameter(java.lang.reflect.Parameter parameter) {
        return new Parameter(parameter.getType(), parameter.getName());
    }

    @Override
    public String toString() {
        return type.getSimpleName();
    }
}
