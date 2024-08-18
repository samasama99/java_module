package org.example;

import java.util.Map;

final class SimpleObject {
    private final SimpleClass simpleClass;
    private final String objectName;
    private final Object object;
    private final Map<String, App.SimpleMethod> simpleMethods;
    private final Map<String, App.SimpleField> simpleFields;

    SimpleObject(SimpleClass simpleClass,
                 String objectName,
                 Object object,
                 Map<String, App.SimpleMethod> simpleMethods,
                 Map<String, App.SimpleField> simpleFields
    ) {
        this.simpleClass = simpleClass;
        this.objectName = objectName;
        this.object = object;
        this.simpleMethods = simpleMethods;
        this.simpleFields = simpleFields;
    }

    public SimpleClass simpleClass() {
        return simpleClass;
    }

    public String objectName() {
        return objectName;
    }

    public Map<String, App.SimpleMethod> simpleMethods() {
        return simpleMethods;
    }

    public Map<String, App.SimpleField> simpleFields() {
        return simpleFields;
    }

}
