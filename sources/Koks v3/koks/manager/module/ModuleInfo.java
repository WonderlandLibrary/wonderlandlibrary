package koks.manager.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kroko
 * @created on 07.10.2020 : 15:30
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {

    int key() default 0;
    String name();
    String description();
    Module.Category category();
}
