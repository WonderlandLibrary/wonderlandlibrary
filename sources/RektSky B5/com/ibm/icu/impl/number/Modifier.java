/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.NumberStringBuilder;

public interface Modifier {
    public int apply(NumberStringBuilder var1, int var2, int var3);

    public int getPrefixLength();

    public int getCodePointCount();

    public boolean isStrong();
}

