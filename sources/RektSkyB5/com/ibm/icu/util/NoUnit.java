/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.util.MeasureUnit;

public class NoUnit
extends MeasureUnit {
    private static final long serialVersionUID = 2467174286237024095L;
    public static final NoUnit BASE = (NoUnit)MeasureUnit.internalGetInstance("none", "base");
    public static final NoUnit PERCENT = (NoUnit)MeasureUnit.internalGetInstance("none", "percent");
    public static final NoUnit PERMILLE = (NoUnit)MeasureUnit.internalGetInstance("none", "permille");

    NoUnit(String subType) {
        super("none", subType);
    }
}

