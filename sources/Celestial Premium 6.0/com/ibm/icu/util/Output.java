/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Output<T> {
    public T value;

    public String toString() {
        return this.value == null ? "null" : this.value.toString();
    }

    public Output() {
    }

    public Output(T value) {
        this.value = value;
    }
}

