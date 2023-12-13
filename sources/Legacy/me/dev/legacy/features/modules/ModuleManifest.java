package me.dev.legacy.features.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value= RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface ModuleManifest {
    public String label() default "";

    public Module.Category category();

    public int key() default 0;

    public boolean persistent() default false;

    public boolean enabled() default false;

    public boolean listen() default true;

    public boolean drawn() default true;
}