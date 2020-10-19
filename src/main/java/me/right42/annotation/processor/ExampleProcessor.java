package me.right42.annotation.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
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
            Set<? extends Element> annotatedWith = roundEnv.getElementsAnnotatedWith(annotation);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "process Test ");
        }

        return true;
    }

    private void generateJavFile() throws IOException {
        TypeSpec.Builder example = TypeSpec.classBuilder("Example");
        example.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        MethodSpec methodSpec = MethodSpec
                            .methodBuilder("test")
                            .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                            .returns(TypeName.VOID)
                            .build();

        example.addMethod(methodSpec);

        JavaFile.builder("com.example.demo.apt", example.build())
                    .build()
                    .writeTo(processingEnv.getFiler());
    }
}
