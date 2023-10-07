package koks.api.registry.file;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright 2021, Koks Team
 * Please don't use the code
 */
public abstract class Files {

    @Getter
    final String name;

    public Files() {
        final Info info = getClass().getAnnotation(Info.class);
        name = info.name();
    }

    public abstract void writeFile(BufferedWriter writer) throws Exception;

    public abstract void readFile(BufferedReader reader) throws Exception;

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String name();
    }
}
