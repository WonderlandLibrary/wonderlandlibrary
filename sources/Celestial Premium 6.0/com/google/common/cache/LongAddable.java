/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.cache;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
interface LongAddable {
    public void increment();

    public void add(long var1);

    public long sum();
}

