/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.NonNull
 */
package com.github.benmanes.caffeine.cache;

import com.github.benmanes.caffeine.cache.DisabledTicker;
import com.github.benmanes.caffeine.cache.SystemTicker;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Ticker {
    public long read();

    public static @NonNull Ticker systemTicker() {
        return SystemTicker.INSTANCE;
    }

    public static @NonNull Ticker disabledTicker() {
        return DisabledTicker.INSTANCE;
    }
}

