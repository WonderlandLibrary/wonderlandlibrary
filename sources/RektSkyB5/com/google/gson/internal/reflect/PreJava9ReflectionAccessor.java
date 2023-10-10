/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.reflect;

import com.google.gson.internal.reflect.ReflectionAccessor;
import java.lang.reflect.AccessibleObject;

final class PreJava9ReflectionAccessor
extends ReflectionAccessor {
    PreJava9ReflectionAccessor() {
    }

    @Override
    public void makeAccessible(AccessibleObject ao) {
        ao.setAccessible(true);
    }
}

