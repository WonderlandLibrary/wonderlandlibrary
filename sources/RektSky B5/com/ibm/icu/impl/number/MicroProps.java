/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.DecimalQuantity;
import com.ibm.icu.impl.number.Grouper;
import com.ibm.icu.impl.number.MicroPropsGenerator;
import com.ibm.icu.impl.number.Modifier;
import com.ibm.icu.impl.number.Padder;
import com.ibm.icu.number.IntegerWidth;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.number.Precision;
import com.ibm.icu.text.DecimalFormatSymbols;

public class MicroProps
implements Cloneable,
MicroPropsGenerator {
    public NumberFormatter.SignDisplay sign;
    public DecimalFormatSymbols symbols;
    public Padder padding;
    public NumberFormatter.DecimalSeparatorDisplay decimal;
    public IntegerWidth integerWidth;
    public Modifier modOuter;
    public Modifier modMiddle;
    public Modifier modInner;
    public Precision rounder;
    public Grouper grouping;
    public boolean useCurrency;
    private final boolean immutable;
    private volatile boolean exhausted;

    public MicroProps(boolean immutable) {
        this.immutable = immutable;
    }

    @Override
    public MicroProps processQuantity(DecimalQuantity quantity) {
        if (this.immutable) {
            return (MicroProps)this.clone();
        }
        if (this.exhausted) {
            throw new AssertionError((Object)"Cannot re-use a mutable MicroProps in the quantity chain");
        }
        this.exhausted = true;
        return this;
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e2) {
            throw new AssertionError((Object)e2);
        }
    }
}

