package org.example;

import java.util.Map;

public record SimpleObject(
        SimpleClass simpleClass,
        String objectName,
        Object object,
        Map<String, SimpleMethod> simpleMethods,
        Map<String, SimpleField> simpleFields
) {
}
