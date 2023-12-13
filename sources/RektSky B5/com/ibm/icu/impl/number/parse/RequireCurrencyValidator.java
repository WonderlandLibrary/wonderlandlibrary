/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.number.parse.ParsedNumber;
import com.ibm.icu.impl.number.parse.ValidationMatcher;

public class RequireCurrencyValidator
extends ValidationMatcher {
    @Override
    public void postProcess(ParsedNumber result) {
        if (result.currencyCode == null) {
            result.flags |= 0x100;
        }
    }

    public String toString() {
        return "<RequireCurrency>";
    }
}

