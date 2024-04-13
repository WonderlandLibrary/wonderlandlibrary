package wtf.dawn.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String getName();
    Category getCategory() default Category.Combat;
    String getDescription();
}
