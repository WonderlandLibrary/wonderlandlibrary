/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import java.text.FieldPosition;
import java.text.Format;

@Deprecated
public class UFieldPosition
extends FieldPosition {
    private int countVisibleFractionDigits = -1;
    private long fractionDigits = 0L;

    @Deprecated
    public UFieldPosition() {
        super(-1);
    }

    @Deprecated
    public UFieldPosition(int field) {
        super(field);
    }

    @Deprecated
    public UFieldPosition(Format.Field attribute, int fieldID) {
        super(attribute, fieldID);
    }

    @Deprecated
    public UFieldPosition(Format.Field attribute) {
        super(attribute);
    }

    @Deprecated
    public void setFractionDigits(int countVisibleFractionDigits, long fractionDigits) {
        this.countVisibleFractionDigits = countVisibleFractionDigits;
        this.fractionDigits = fractionDigits;
    }

    @Deprecated
    public int getCountVisibleFractionDigits() {
        return this.countVisibleFractionDigits;
    }

    @Deprecated
    public long getFractionDigits() {
        return this.fractionDigits;
    }
}

