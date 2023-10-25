/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.builder.DiffResult;

public interface Diffable<T> {
    public DiffResult diff(T var1);
}

