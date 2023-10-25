/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.util.Locale;

public final class EnglishEnums {
    private EnglishEnums() {
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        return EnglishEnums.valueOf(enumType, name, null);
    }

    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name, T defaultValue) {
        return name == null ? defaultValue : Enum.valueOf(enumType, name.toUpperCase(Locale.ENGLISH));
    }
}

