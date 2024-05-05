/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

public class CompilationException
extends RuntimeException {
    CompilationException(String description) {
        super(description);
    }

    CompilationException(Exception cause) {
        super(cause);
    }
}

