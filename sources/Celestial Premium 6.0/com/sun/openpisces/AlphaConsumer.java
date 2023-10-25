/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

public interface AlphaConsumer {
    public int getOriginX();

    public int getOriginY();

    public int getWidth();

    public int getHeight();

    public void setMaxAlpha(int var1);

    public void setAndClearRelativeAlphas(int[] var1, int var2, int var3, int var4);
}

