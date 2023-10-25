/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.KeyValue
 */
package com.sun.javafx.animation;

import com.sun.javafx.animation.KeyValueType;
import com.sun.javafx.util.Utils;
import javafx.animation.KeyValue;

public class KeyValueHelper {
    private static KeyValueAccessor keyValueAccessor;

    private KeyValueHelper() {
    }

    public static KeyValueType getType(KeyValue keyValue) {
        return keyValueAccessor.getType(keyValue);
    }

    public static void setKeyValueAccessor(KeyValueAccessor keyValueAccessor) {
        if (KeyValueHelper.keyValueAccessor != null) {
            throw new IllegalStateException();
        }
        KeyValueHelper.keyValueAccessor = keyValueAccessor;
    }

    static {
        Utils.forceInit(KeyValue.class);
    }

    public static interface KeyValueAccessor {
        public KeyValueType getType(KeyValue var1);
    }
}

