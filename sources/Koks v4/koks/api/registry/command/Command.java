package koks.api.registry.command;

import koks.Koks;
import koks.api.Methods;
import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */
public abstract class Command implements Methods {

    @Getter
    final String name, description;
    @Getter
    final String[] aliases;

    public Command() {
        final Info info = getClass().getAnnotation(Info.class);
        name = info.name();
        description = info.description();
        aliases = info.aliases().length != 0 ? info.aliases() : new String[] {name};
    }

    public abstract boolean execute(String[] args);

    public String getPrefix() {
        return Koks.getKoks().commandPrefix;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Info {
        String name();
        String description();
        String[] aliases() default {};
    }
}
