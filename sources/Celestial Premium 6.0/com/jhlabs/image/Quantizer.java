/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

public interface Quantizer {
    public void setup(int var1);

    public void addPixels(int[] var1, int var2, int var3);

    public int[] buildColorTable();

    public int getIndexForColor(int var1);
}

