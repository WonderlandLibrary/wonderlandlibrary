package graphite.api.module.util;

import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String name();
    String description() default "No description provided!";
    Category category();
    int key() default Keyboard.KEY_NONE;
}