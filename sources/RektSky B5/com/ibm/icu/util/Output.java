/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

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

