package org.example;

import java.lang.reflect.Field;

final class SimpleField {
    private final Class<?> type;
    private final String name;
    private final Field field;
    private final Object object;

    private SimpleField(Class<?> type, String name, Field field, Object object) {
        this.type = type;
        this.name = name;
        this.field = field;
        this.object = object;
    }

    static SimpleField fromReflectField(Field field, Object object) {
        field.setAccessible(true);
        try {
            return new SimpleField(field.getType(), field.getName(), field, object);
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

    public Class<?> type() {
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
