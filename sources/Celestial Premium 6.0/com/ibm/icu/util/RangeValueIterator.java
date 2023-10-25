/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

public interface RangeValueIterator {
    public boolean next(Element var1);

    public void reset();

    public static class Element {
        public int start;
        public int limit;
        public int value;
    }
}

