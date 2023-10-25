/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.openpisces.AlphaConsumer;

public interface MarlinAlphaConsumer
extends AlphaConsumer {
    public boolean supportBlockFlags();

    public void clearAlphas(int var1);

    public void setAndClearRelativeAlphas(int[] var1, int[] var2, int var3, int var4, int var5);
}

