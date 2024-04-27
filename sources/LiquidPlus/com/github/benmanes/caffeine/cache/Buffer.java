/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.DisabledBuffer;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;

interface Buffer<E> {
    public static final int FULL = 1;
    public static final int SUCCESS = 0;
    public static final int FAILED = -1;

    public static <E> Buffer<E> disabled() {
        return DisabledBuffer.INSTANCE;
    }

    public int offer(@NonNull E var1);

    public void drainTo(@NonNull Consumer<E> var1);

    default public int size() {
        return this.writes() - this.reads();
    }

    public int reads();

    public int writes();
}

