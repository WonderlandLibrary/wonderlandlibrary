/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.lang3.concurrent;

public interface Computable<I, O> {
    public O compute(I var1) throws InterruptedException;
}

