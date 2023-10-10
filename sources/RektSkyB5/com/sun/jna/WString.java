/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import java.nio.CharBuffer;

public final class WString
implements CharSequence,
Comparable {
    private String string;

    public WString(String s2) {
        if (s2 == null) {
            throw new NullPointerException("String initializer must be non-null");
        }
        this.string = s2;
    }

    public String toString() {
        return this.string;
    }

    public boolean equals(Object o2) {
        return o2 instanceof WString && this.toString().equals(o2.toString());
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public int compareTo(Object o2) {
        return this.toString().compareTo(o2.toString());
    }

    public int length() {
        return this.toString().length();
    }

    public char charAt(int index) {
        return this.toString().charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return CharBuffer.wrap(this.toString()).subSequence(start, end);
    }
}

