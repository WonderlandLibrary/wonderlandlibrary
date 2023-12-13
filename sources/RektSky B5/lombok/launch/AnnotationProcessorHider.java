/*
 * Decompiled with CFR 0.152.
 */
package lombok.launch;

import java.lang.reflect.Field;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import lombok.launch.Main;
import sun.misc.Unsafe;

class AnnotationProcessorHider {
    AnnotationProcessorHider() {
    }

    public static class AnnotationProcessor
    extends AbstractProcessor {
        private final AbstractProcessor instance = AnnotationProcessor.createWrappedInstance();

        @Override
        public Set<String> getSupportedOptions() {
            return this.instance.getSupportedOptions();
        }

        @Override
        public Set<String> getSupportedAnnotationTypes() {
            return this.instance.getSupportedAnnotationTypes();
        }

        @Override
        public SourceVersion getSupportedSourceVersion() {
            return this.instance.getSupportedSourceVersion();
        }

        @Override
        public void init(ProcessingEnvironment processingEnv) {
            this.disableJava9SillyWarning();
            AstModificationNotifierData.lombokInvoked = true;
            this.instance.init(processingEnv);
            super.init(processingEnv);
        }

        private void disableJava9SillyWarning() {
            try {
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                Unsafe u2 = (Unsafe)theUnsafe.get(null);
                Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
                Field logger = cls.getDeclaredField("logger");
                u2.putObjectVolatile(cls, u2.staticFieldOffset(logger), null);
            }
            catch (Throwable throwable) {}
        }

        @Override
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            return this.instance.process(annotations, roundEnv);
        }

        @Override
        public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
            return this.instance.getCompletions(element, annotation, member, userText);
        }

        private static AbstractProcessor createWrappedInstance() {
            ClassLoader cl = Main.getShadowClassLoader();
            try {
                Class<?> mc = cl.loadClass("lombok.core.AnnotationProcessor");
                return (AbstractProcessor)mc.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            }
            catch (Throwable t2) {
                if (t2 instanceof Error) {
                    throw (Error)t2;
                }
                if (t2 instanceof RuntimeException) {
                    throw (RuntimeException)t2;
                }
                throw new RuntimeException(t2);
            }
        }
    }

    public static class AstModificationNotifierData {
        public static volatile boolean lombokInvoked = false;
    }

    @SupportedAnnotationTypes(value={"lombok.*"})
    public static class ClaimingProcessor
    extends AbstractProcessor {
        @Override
        public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
            return true;
        }

        @Override
        public SourceVersion getSupportedSourceVersion() {
            return SourceVersion.latest();
        }
    }
}

