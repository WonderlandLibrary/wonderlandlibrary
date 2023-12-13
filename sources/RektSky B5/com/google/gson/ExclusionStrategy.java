/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.FieldAttributes;

public interface ExclusionStrategy {
    public boolean shouldSkipField(FieldAttributes var1);

    public boolean shouldSkipClass(Class<?> var1);
}

