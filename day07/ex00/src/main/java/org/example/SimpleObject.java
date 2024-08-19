package org.example;

import java.util.Map;

public record SimpleObject(
        String objectName,
        Map<String, SimpleMethod> simpleMethods,
        Map<String, SimpleField> simpleFields
) {
}
