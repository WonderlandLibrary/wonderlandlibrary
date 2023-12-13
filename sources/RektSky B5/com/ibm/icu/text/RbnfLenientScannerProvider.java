/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.RbnfLenientScanner;
import com.ibm.icu.util.ULocale;

@Deprecated
public interface RbnfLenientScannerProvider {
    @Deprecated
    public RbnfLenientScanner get(ULocale var1, String var2);
}

