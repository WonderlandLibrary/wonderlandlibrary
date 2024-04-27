package me.aquavit.liquidsense.module;

import me.aquavit.liquidsense.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    String name();

    String description();

    ModuleCategory category();

    int keyBind() default Keyboard.CHAR_NONE;

    boolean canEnable() default true;

    boolean array() default true;
}
