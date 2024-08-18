package org.example;

import java.util.Map;

public record SimpleObject(
        SimpleClass simpleClass,
        String objectName,
        Object object,
        Map<String, App.SimpleMethod> simpleMethods,
        Map<String, App.SimpleField> simpleFields
) {
}
