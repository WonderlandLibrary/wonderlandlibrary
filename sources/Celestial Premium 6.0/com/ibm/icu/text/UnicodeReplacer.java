/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.UnicodeSet;

interface UnicodeReplacer {
    public int replace(Replaceable var1, int var2, int var3, int[] var4);

    public String toReplacerPattern(boolean var1);

    public void addReplacementSetTo(UnicodeSet var1);
}

