/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

public final class Booleans {
    private Booleans() {
    }

    public static boolean parseBoolean(String s, boolean defaultValue) {
        return "true".equalsIgnoreCase(s) || defaultValue && !"false".equalsIgnoreCase(s);
    }
}

