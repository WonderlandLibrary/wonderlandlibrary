/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface Freezable<T>
extends Cloneable {
    public boolean isFrozen();

    public T freeze();

    public T cloneAsThawed();
}

