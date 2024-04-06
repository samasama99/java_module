package org.example.config;

import org.example.annotation.HtmlInput;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

@SupportedAnnotationTypes("CustomAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_22)
public class HtmlProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // Iterate over annotated elements
        for (Element element : roundEnv.getElementsAnnotatedWith(HtmlInput.class)) {
            // Get the package name and class name of the annotated class
            String packageName = processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
            String className = element.getSimpleName().toString();

            // Generate the implementation class name
            String implementationClassName = className + "Impl";

            // Generate the implementation class content
            String implementationClassContent = generateImplementationClass(packageName, className, implementationClassName);

            // Write the implementation class to a file
            writeImplementationClass(packageName, implementationClassName, implementationClassContent);
        }
        return true;
    }

    private String generateImplementationClass(String packageName, String className, String implementationClassName) {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("public class ").append(implementationClassName).append(" {\n\n");
        sb.append("    public ").append(implementationClassName).append("() {\n");
        sb.append("        System.out.println(\"").append(className).append(" implementation created\");\n");
        sb.append("    }\n");
        sb.append("}\n");
        return sb.toString();
    }

    private void writeImplementationClass(String packageName, String implementationClassName, String content) {
        try {
            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(packageName + "." + implementationClassName);
            try (Writer writer = fileObject.openWriter()) {
                writer.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}