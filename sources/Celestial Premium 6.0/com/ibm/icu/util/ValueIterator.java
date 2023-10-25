/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

public interface ValueIterator {
    public boolean next(Element var1);

    public void reset();

    public void setRange(int var1, int var2);

    public static final class Element {
        public int integer;
        public Object value;
    }
}

