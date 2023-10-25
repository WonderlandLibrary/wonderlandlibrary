/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class CacheBase<K, V, D> {
    public abstract V getInstance(K var1, D var2);

    protected abstract V createInstance(K var1, D var2);
}

