/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

public interface RbnfLenientScanner {
    public boolean allIgnorable(String var1);

    public int prefixLength(String var1, String var2);

    public int[] findText(String var1, String var2, int var3);
}

