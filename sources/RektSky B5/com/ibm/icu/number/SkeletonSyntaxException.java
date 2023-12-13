/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.number;

public class SkeletonSyntaxException
extends IllegalArgumentException {
    private static final long serialVersionUID = 7733971331648360554L;

    public SkeletonSyntaxException(String message, CharSequence token) {
        super("Syntax error in skeleton string: " + message + ": " + token);
    }

    public SkeletonSyntaxException(String message, CharSequence token, Throwable cause) {
        super("Syntax error in skeleton string: " + message + ": " + token, cause);
    }
}

