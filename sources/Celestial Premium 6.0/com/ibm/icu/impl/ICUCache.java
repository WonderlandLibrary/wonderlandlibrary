/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface ICUCache<K, V> {
    public static final int SOFT = 0;
    public static final int WEAK = 1;
    public static final Object NULL = new Object();

    public void clear();

    public void put(K var1, V var2);

    public V get(Object var1);
}

