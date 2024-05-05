package org.example.annotation.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@SupportedAnnotationTypes({
        "org.example.annotation.processor.OrmEntity",
        "org.example.annotation.processor.OrmColumn",
        "org.example.annotation.processor.OrmColumnId"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class OrmProcessor extends AbstractProcessor {

    String generateDeletionSqlQuery(Element element) {
        StringBuilder stringBuilder = new StringBuilder();
        OrmEntity entity = element.getAnnotation(OrmEntity.class);
        stringBuilder.append("DROP TABLE ").append(entity.table()).append(";\n");
        return stringBuilder.toString();
    }

    String generateCreationSqlQuery(Element element) {
        StringBuilder stringBuilder = new StringBuilder();
        OrmEntity entity = element.getAnnotation(OrmEntity.class);
        stringBuilder.append("CREATE TABLE ").append(entity.table()).append(" (\n");
        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.FIELD) {
                continue;
            }
            OrmColumn ormColumn = enclosedElement.getAnnotation(OrmColumn.class);
            if (ormColumn == null) {
                OrmColumnId ormColumnId = enclosedElement.getAnnotation(OrmColumnId.class);
                if (ormColumnId == null) {
                    continue;
                }
                TypeMirror typeMirror = enclosedElement.asType();
                String type = typeMirror.toString();
                if (type.compareTo("java.lang.Long") == 0) {
                    stringBuilder.append("id BIGSERIAL PRIMARY KEY,\n");
                }
                if (type.compareTo("java.lang.Integer") == 0) {
                    stringBuilder.append("id SERIAL PRIMARY KEY,\n");
                }
            } else {
                TypeMirror typeMirror = enclosedElement.asType();
                String type = typeMirror.toString();
                if (type.equals("java.lang.Long")) {
                    stringBuilder.append(ormColumn.name()).append(" BIGINT,\n");
                }

                if (type.equals("java.lang.Double")) {
                    stringBuilder.append(ormColumn.name()).append(" DOUBLE PRECISION,\n");
                }
                if (type.equals("java.lang.Integer")) {
                    stringBuilder.append(ormColumn.name()).append(" INT,\n");
                }
                if (type.equals("java.lang.Boolean")) {
                    stringBuilder.append(ormColumn.name()).append(" BOOLEAN,\n");
                }
                if (type.equals("java.lang.String") && ormColumn.length() <= 0) {
                    stringBuilder.append(ormColumn.name()).append(" TEXT,\n");
                }
                if (type.equals("java.lang.String") && ormColumn.length() > 0) {
                    stringBuilder.append(ormColumn.name()).append(" VARCHAR(").append(ormColumn.length()).append("),\n");
                }
            }
        }
        if (stringBuilder.charAt(stringBuilder.length() - 2) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        }
        stringBuilder.append(");\n");
        return stringBuilder.toString();
    }


    String generateInsertionSqlQuery(Element element) {
        StringBuilder stringBuilder = new StringBuilder();
        OrmEntity entity = element.getAnnotation(OrmEntity.class);
        stringBuilder.append("INSERT INTO ").append(entity.table()).append(" (");
        List<String> names = extractColumnsNames(element);
        stringBuilder.append(String.join(", ", names));
        stringBuilder.append(")\n");
        stringBuilder.append("VALUES (");
        stringBuilder.append(names.stream().map(n -> "?").collect(Collectors.joining(",")));
        stringBuilder.append(");\n");
        return stringBuilder.toString();
    }

    String generateSelectById(Element element) {
        StringBuilder stringBuilder = new StringBuilder();
        OrmEntity entity = element.getAnnotation(OrmEntity.class);
        stringBuilder.append("SELECT * FROM ").append(entity.table()).append(" WHERE id = ?;\n");
        return stringBuilder.toString();
    }

    String generateUpdateSqlQuery(Element element) {
        StringBuilder stringBuilder = new StringBuilder();
        OrmEntity entity = element.getAnnotation(OrmEntity.class);
        stringBuilder.append("UPDATE ").append(entity.table()).append(" SET\n");
        List<String> names = extractColumnsNames(element);
        stringBuilder.append(names.stream().map(n -> n + " = ?").collect(Collectors.joining(",\n"))).append("\n");
        stringBuilder.append("WHERE id = ?;");
        return stringBuilder.toString();
    }

    private List<String> extractColumnsNames(Element element) {
        List<String> names = new ArrayList<>();
        for (Element enclosedElement : element.getEnclosedElements()) {
            if (enclosedElement.getKind() != ElementKind.FIELD) {
                continue;
            }
            OrmColumn ormColumn = enclosedElement.getAnnotation(OrmColumn.class);
            if (ormColumn == null) {
                continue;
            }
            String name = ormColumn.name();
            names.add(name);
        }
        return names;
    }


    void createDirectory(String directoryPath) {
        Path directory = Paths.get(directoryPath);

        try {
            if (Files.exists(directory)) {
                System.out.println("Directory already exists. Deleting...");
                Files.delete(directory);
            }

            System.out.println("Creating directory...");
            Files.createDirectory(directory);
            System.out.println("Directory created successfully.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void generateRepository(Element element, String rowMapper) throws IOException {
        String packageName = processingEnv.getElementUtils().getPackageOf(element).toString();
        String className = element.getSimpleName().toString();
        String fullClassName = packageName + "." + className;
        String repositoryName = className + "Repository";
        String fullRepositoryName = packageName + "." + repositoryName;
        JavaFileObject file = processingEnv.getFiler().createSourceFile(fullRepositoryName);
        System.out.println(packageName);
        System.out.println(className);
        System.out.println(fullClassName);
        System.out.println(repositoryName);
        System.out.println(fullRepositoryName);

        System.out.println();

        try (PrintWriter writer = new PrintWriter(file.openWriter())) {
            writer.println("package " + packageName + ";");
            writer.println();
            writer.println("import " + fullClassName + ";");
            writer.println();
            writer.println("public class " + repositoryName + " {");
            writer.println();
            writer.println("    public void save(" + className + " " + className.toLowerCase() + ") {");
            writer.println("        System.out.println(\"HELLO FROM SAVE\");");
            writer.println("    }");
            writer.println();
            writer.println("    public " + className + " selectById(Long id) {");
            writer.println("        System.out.println(\"HELLO FROM SELECT\");");
            writer.println("        return null;");
            writer.println("    }");
            writer.println();
            writer.println("    public void update(" + className + " " + className.toLowerCase() + ") {");
            writer.println("        System.out.println(\"HELLO FROM UPDATE\");");
            writer.println("    }");
            writer.println("\n");
            writer.println(rowMapper);
            writer.println("}");
        }
    }

    String createRowMapper(Element element) {
        String className = element.getSimpleName().toString();

        return "static class " + className + "RowMapper implements RowMapper<" + className + "> {\n" +
                "    @Override\n" +
                "    public " + className + " mapRow(ResultSet rs, int rowNum) throws SQLException {\n" +
                "        " + className + " tmp = new Product();" +
                "public class " + className + "RowMapper<T> implements RowMapper<T> {\n\n" +
                "    private final Class<T> mappedClass;\n\n" +
                "    public " + className + "(Class<T> mappedClass) {\n" +
                "        this.mappedClass = mappedClass;\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public T mapRow(ResultSet rs, int rowNum) throws SQLException {\n" +
                "        try {\n" +
                "            T instance = mappedClass.getDeclaredConstructor().newInstance();\n" +
                "            for (Field field : mappedClass.getDeclaredFields()) {\n" +
                "                if (field.isAnnotationPresent(OrmColumn.class)) {\n" +
                "                    OrmColumn ormColumnAnnotation = field.getAnnotation(OrmColumn.class);\n" +
                "                    String columnName = ormColumnAnnotation.name();\n" +
                "                    Object value = rs.getObject(columnName);\n" +
                "                    field.setAccessible(true);\n" +
                "                    field.set(instance, value);\n" +
                "                }\n" +
                "            }\n" +
                "            return instance;\n" +
                "        } catch (Exception e) {\n" +
                "            throw new SQLException(\"Failed to map row\", e);\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("PROCESS CALLED!!!");
        for (Element element : roundEnv.getElementsAnnotatedWith(OrmEntity.class)) {
            System.out.println(generateCreationSqlQuery(element));
            System.out.println(generateDeletionSqlQuery(element));
            System.out.println(generateInsertionSqlQuery(element));
            System.out.println(generateSelectById(element));
            System.out.println(generateUpdateSqlQuery(element));
            System.out.println(createRowMapper(element));
            try {
                generateRepository(element, createRowMapper(element));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }


}