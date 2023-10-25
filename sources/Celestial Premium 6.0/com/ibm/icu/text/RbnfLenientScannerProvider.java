/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.RbnfLenientScanner;
import com.ibm.icu.util.ULocale;

public interface RbnfLenientScannerProvider {
    public RbnfLenientScanner get(ULocale var1, String var2);
}

