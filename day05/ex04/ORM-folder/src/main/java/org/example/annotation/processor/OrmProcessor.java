package org.example.annotation.processor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes({
        "org.example.annotation.processor.OrmEntity",
        "org.example.annotation.processor.OrmColumn"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class OrmProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(OrmEntity.class)) {
            StringBuilder stringBuilder = new StringBuilder();

            OrmEntity entity = element.getAnnotation(OrmEntity.class);
            for (Element enclosedElement : element.getEnclosedElements()) {
                OrmColumn column = enclosedElement.getAnnotation(OrmColumn.class);
                if (enclosedElement.getKind() == ElementKind.FIELD && column != null) {
                    throw new RuntimeException("TODO");
                }
            }
        }
        return true;
    }


}
