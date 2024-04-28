package org.example.annotation.processor;

import java.util.Set;

public class SupportedType {
    public final Set<Class<?>> types = Set.of(
            Integer.class,
            Long.class,
            Double.class,
            String.class,
            Boolean.class
    );
}
