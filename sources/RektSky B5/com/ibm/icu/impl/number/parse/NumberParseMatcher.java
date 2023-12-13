/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.number.parse.ParsedNumber;

public interface NumberParseMatcher {
    public boolean match(StringSegment var1, ParsedNumber var2);

    public boolean smokeTest(StringSegment var1);

    public void postProcess(ParsedNumber var1);

    public static interface Flexible {
    }
}

