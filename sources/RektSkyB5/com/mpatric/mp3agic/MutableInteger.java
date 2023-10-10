/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

public class MutableInteger {
    private int value;

    public MutableInteger(int n2) {
        this.value = n2;
    }

    public void increment() {
        ++this.value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int n2) {
        this.value = n2;
    }

    public int hashCode() {
        int n2 = 1;
        n2 = 31 * n2 + this.value;
        return n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        MutableInteger mutableInteger = (MutableInteger)object;
        return this.value == mutableInteger.value;
    }
}

