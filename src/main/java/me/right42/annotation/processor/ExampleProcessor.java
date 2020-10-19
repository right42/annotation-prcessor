package me.right42.annotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileManager;
import java.io.IOException;
import java.util.Set;

@SupportedAnnotationTypes(
        "me.right42.annotation.ExampleGetter")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class ExampleProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedWiths = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element annotatedWith : annotatedWiths) {
                try {
                    generateJavFile(annotatedWith);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private void generateJavFile(Element element) throws IOException {
        TypeSpec.Builder example = TypeSpec.classBuilder(element.getSimpleName().toString());

        for (Element enclosedElement : element.getEnclosedElements()) {
            ElementKind kind = enclosedElement.getKind();

            ClassName className = ClassName.get(enclosedElement.asType().getClass());

            if(kind.equals(ElementKind.FIELD)) {
                MethodSpec methodSpec = MethodSpec
                        .methodBuilder("get" + enclosedElement.getSimpleName())
                        .addModifiers(Modifier.PUBLIC)
                        .returns(className.getClass())
                        .build();

                example.addMethod(methodSpec);
            }
        }

        JavaFile.builder(element.getEnclosingElement().asType().toString(), example.build())
                    .build()
                    .writeTo(processingEnv.getFiler());
    }
}
