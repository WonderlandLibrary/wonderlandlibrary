package wtf.expensive.modules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface FunctionAnnotation {
    String name();

    String desc() default "";

    int key() default 0;

    Type type();
}
