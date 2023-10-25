/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.dto;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class ValueObject {
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Field f : this.getClass().getFields()) {
            if (ValueObject.isStatic(f)) continue;
            try {
                sb.append(f.getName()).append("=").append(f.get(this)).append(" ");
            }
            catch (IllegalAccessException ignore) {
                // empty catch block
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append('}');
        return sb.toString();
    }

    private static boolean isStatic(Field f) {
        return Modifier.isStatic(f.getModifiers());
    }
}

