package org.example.annotation.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes({
        "org.example.annotation.processor.HtmlForm",
        "org.example.annotation.processor.HtmlInput"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class HtmlProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "DEBUG !!!");
        try {
            for (Element element : roundEnv.getElementsAnnotatedWith(HtmlForm.class)) {
                HtmlForm htmlForm = element.getAnnotation(HtmlForm.class);
                String fileName = htmlForm.fileName();

                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "generating form: " + fileName);

                FileWriter writer = new FileWriter(fileName, false);
                generateHtmlFormStart(writer, htmlForm.action(), htmlForm.method());
                for (Element enclosedElement : element.getEnclosedElements()) {
                    HtmlInput annotation = enclosedElement.getAnnotation(HtmlInput.class);
                    if (enclosedElement.getKind() == ElementKind.FIELD && annotation != null) {
                        generateHtmlInput(writer, annotation.type(), annotation.name(), annotation.placeholder());
                    }
                }
                generateSubmit(writer);
                closeForm(writer);
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void generateHtmlFormStart(FileWriter writer, String action, String method) throws IOException {
//    <form action = "/users" method = "post">
        writer.write(String.format("<form action = \"/%s\" method = \"%s\">\n", action, method));
    }

    private void closeForm(FileWriter writer) throws IOException {
//</form>
        writer.write("</form>\n");
    }

    private void generateSubmit(FileWriter writer) throws IOException {
//<input type = "submit" value = "Send">
        writer.write("<input type = \"submit\" value = \"Send\">\n");
    }


    private void generateHtmlInput(FileWriter writer, String type, String name, String placeholder) throws IOException {
//<input type = "text" name = "first_name" placeholder = "Enter First Name">
//<input type = "text" name = "last_name" placeholder = "Enter Last Name">
//<input type = "password" name = "password" placeholder = "Enter Password">
        writer.write(String.format("<input type = \"%s\" name = \"%s\" placeholder = \"%s\">\n", type, name, placeholder));
    }

}
