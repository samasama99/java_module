package org.example.annotation.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

@SupportedAnnotationTypes({
        "org.example.annotation.processor.OrmEntity",
        "org.example.annotation.processor.OrmColumn",
        "org.example.annotation.processor.OrmColumnId"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class OrmProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(OrmEntity.class)) {
            StringBuilder stringBuilder = new StringBuilder();

            OrmEntity entity = element.getAnnotation(OrmEntity.class);
//            System.out.println("Table name: " + entity.table());
            stringBuilder.append("create table ").append(entity.table()).append(" (\n");
            for (Element enclosedElement : element.getEnclosedElements()) {
                if (enclosedElement.getKind() != ElementKind.FIELD) {
                    continue;
                }
                OrmColumn ormColumn = enclosedElement.getAnnotation(OrmColumn.class);
                if (ormColumn == null) {
                    OrmColumnId ormColumnId = enclosedElement.getAnnotation(OrmColumnId.class);
                    if (ormColumnId == null) {
                        continue;
//                        throw new IllegalArgumentException("Unknown annotation: " + enclosedElement);
                    }
                    TypeMirror typeMirror = enclosedElement.asType();
//                    System.out.println("- " + typeMirror);
                    String type = typeMirror.toString();
                    if (type.compareTo("java.lang.Long") == 0) {
                        stringBuilder.append("id bigserial primary key,\n");
                    }
                    if (type.compareTo("java.lang.Integer") == 0) {
                        stringBuilder.append("id serial primary key,\n");
                    }
                } else {
                    TypeMirror typeMirror = enclosedElement.asType();
                    String type = typeMirror.toString();
//                    System.out.println("- " + typeMirror);
                    if (type.equals("java.lang.Long")) {
                        stringBuilder.append(ormColumn.name()).append(" bigint,\n");
                    }
                    if (type.equals("java.lang.Integer")) {
                        stringBuilder.append(ormColumn.name()).append(" int,\n");
                    }
                    if (type.equals("java.lang.Boolean")) {
                        stringBuilder.append(ormColumn.name()).append(" boolean,\n");
                    }
                    if (type.equals("java.lang.String") && ormColumn.length() <= 0) {
                        stringBuilder.append(ormColumn.name()).append(" varchar,\n");
                    }
                    if (type.equals("java.lang.String") && ormColumn.length() > 0) {
                        stringBuilder.append(ormColumn.name()).append(" varchar(").append(ormColumn.length()).append("),\n");
                    }
                }
            }
            stringBuilder.append(");\n");
            System.out.println(stringBuilder);
        }
        return true;
    }


}

//                    switch (typeMirror.getKind()) {
//        case TypeKind.INT -> System.out.println("INTEGER");
//                        case TypeKind.LONG -> System.out.println("LONG");
//                        case TypeKind.BOOLEAN -> System.out.println("BOOLEAN");
//                        case TypeKind.DOUBLE -> System.out.println("DOUBLE");
//                        case TypeKind.OTHER -> System.out.println("STRING MAYBE");
//default -> throw new IllegalArgumentException("Unsupported type");
//                    }
