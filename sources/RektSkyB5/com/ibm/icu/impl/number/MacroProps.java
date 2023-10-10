/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.number.AffixPatternProvider;
import com.ibm.icu.impl.number.Padder;
import com.ibm.icu.number.IntegerWidth;
import com.ibm.icu.number.Notation;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.number.Precision;
import com.ibm.icu.number.Scale;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.MeasureUnit;
import com.ibm.icu.util.ULocale;
import java.math.RoundingMode;

public class MacroProps
implements Cloneable {
    public Notation notation;
    public MeasureUnit unit;
    public MeasureUnit perUnit;
    public Precision precision;
    public RoundingMode roundingMode;
    public Object grouping;
    public Padder padder;
    public IntegerWidth integerWidth;
    public Object symbols;
    public NumberFormatter.UnitWidth unitWidth;
    public NumberFormatter.SignDisplay sign;
    public NumberFormatter.DecimalSeparatorDisplay decimal;
    public Scale scale;
    public AffixPatternProvider affixProvider;
    public PluralRules rules;
    public Long threshold;
    public ULocale loc;

    public void fallback(MacroProps fallback) {
        if (this.notation == null) {
            this.notation = fallback.notation;
        }
        if (this.unit == null) {
            this.unit = fallback.unit;
        }
        if (this.perUnit == null) {
            this.perUnit = fallback.perUnit;
        }
        if (this.precision == null) {
            this.precision = fallback.precision;
        }
        if (this.roundingMode == null) {
            this.roundingMode = fallback.roundingMode;
        }
        if (this.grouping == null) {
            this.grouping = fallback.grouping;
        }
        if (this.padder == null) {
            this.padder = fallback.padder;
        }
        if (this.integerWidth == null) {
            this.integerWidth = fallback.integerWidth;
        }
        if (this.symbols == null) {
            this.symbols = fallback.symbols;
        }
        if (this.unitWidth == null) {
            this.unitWidth = fallback.unitWidth;
        }
        if (this.sign == null) {
            this.sign = fallback.sign;
        }
        if (this.decimal == null) {
            this.decimal = fallback.decimal;
        }
        if (this.affixProvider == null) {
            this.affixProvider = fallback.affixProvider;
        }
        if (this.scale == null) {
            this.scale = fallback.scale;
        }
        if (this.rules == null) {
            this.rules = fallback.rules;
        }
        if (this.loc == null) {
            this.loc = fallback.loc;
        }
    }

    public int hashCode() {
        return Utility.hash(new Object[]{this.notation, this.unit, this.perUnit, this.precision, this.roundingMode, this.grouping, this.padder, this.integerWidth, this.symbols, this.unitWidth, this.sign, this.decimal, this.affixProvider, this.scale, this.rules, this.loc});
    }

    public boolean equals(Object _other) {
        if (_other == null) {
            return false;
        }
        if (this == _other) {
            return true;
        }
        if (!(_other instanceof MacroProps)) {
            return false;
        }
        MacroProps other = (MacroProps)_other;
        return Utility.equals(this.notation, other.notation) && Utility.equals(this.unit, other.unit) && Utility.equals(this.perUnit, other.perUnit) && Utility.equals(this.precision, other.precision) && Utility.equals((Object)this.roundingMode, (Object)other.roundingMode) && Utility.equals(this.grouping, other.grouping) && Utility.equals(this.padder, other.padder) && Utility.equals(this.integerWidth, other.integerWidth) && Utility.equals(this.symbols, other.symbols) && Utility.equals((Object)this.unitWidth, (Object)other.unitWidth) && Utility.equals((Object)this.sign, (Object)other.sign) && Utility.equals((Object)this.decimal, (Object)other.decimal) && Utility.equals(this.affixProvider, other.affixProvider) && Utility.equals(this.scale, other.scale) && Utility.equals(this.rules, other.rules) && Utility.equals(this.loc, other.loc);
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

