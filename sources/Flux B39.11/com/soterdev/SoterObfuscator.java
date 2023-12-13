package com.soterdev;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class SoterObfuscator {
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.METHOD, ElementType.TYPE})
    public @interface Obfuscation {
        boolean exclude() default false;
        String flags();
    }

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.METHOD})
    public @interface Entry {}
}
