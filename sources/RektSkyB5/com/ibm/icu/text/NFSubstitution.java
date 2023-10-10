/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.AbsoluteValueSubstitution;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.FractionalPartSubstitution;
import com.ibm.icu.text.IntegralPartSubstitution;
import com.ibm.icu.text.ModulusSubstitution;
import com.ibm.icu.text.MultiplierSubstitution;
import com.ibm.icu.text.NFRule;
import com.ibm.icu.text.NFRuleSet;
import com.ibm.icu.text.NumeratorSubstitution;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.text.SameValueSubstitution;
import java.text.ParsePosition;

abstract class NFSubstitution {
    final int pos;
    final NFRuleSet ruleSet;
    final DecimalFormat numberFormat;
    private static final long MAX_INT64_IN_DOUBLE = 0x1FFFFFFFFFFFFFL;

    public static NFSubstitution makeSubstitution(int pos, NFRule rule, NFRule rulePredecessor, NFRuleSet ruleSet, RuleBasedNumberFormat formatter, String description) {
        if (description.length() == 0) {
            return null;
        }
        switch (description.charAt(0)) {
            case '<': {
                if (rule.getBaseValue() == -1L) {
                    throw new IllegalArgumentException("<< not allowed in negative-number rule");
                }
                if (rule.getBaseValue() == -2L || rule.getBaseValue() == -3L || rule.getBaseValue() == -4L) {
                    return new IntegralPartSubstitution(pos, ruleSet, description);
                }
                if (ruleSet.isFractionSet()) {
                    return new NumeratorSubstitution(pos, rule.getBaseValue(), formatter.getDefaultRuleSet(), description);
                }
                return new MultiplierSubstitution(pos, rule, ruleSet, description);
            }
            case '>': {
                if (rule.getBaseValue() == -1L) {
                    return new AbsoluteValueSubstitution(pos, ruleSet, description);
                }
                if (rule.getBaseValue() == -2L || rule.getBaseValue() == -3L || rule.getBaseValue() == -4L) {
                    return new FractionalPartSubstitution(pos, ruleSet, description);
                }
                if (ruleSet.isFractionSet()) {
                    throw new IllegalArgumentException(">> not allowed in fraction rule set");
                }
                return new ModulusSubstitution(pos, rule, rulePredecessor, ruleSet, description);
            }
            case '=': {
                return new SameValueSubstitution(pos, ruleSet, description);
            }
        }
        throw new IllegalArgumentException("Illegal substitution character");
    }

    NFSubstitution(int pos, NFRuleSet ruleSet, String description) {
        this.pos = pos;
        int descriptionLen = description.length();
        if (descriptionLen >= 2 && description.charAt(0) == description.charAt(descriptionLen - 1)) {
            description = description.substring(1, descriptionLen - 1);
        } else if (descriptionLen != 0) {
            throw new IllegalArgumentException("Illegal substitution syntax");
        }
        if (description.length() == 0) {
            this.ruleSet = ruleSet;
            this.numberFormat = null;
        } else if (description.charAt(0) == '%') {
            this.ruleSet = ruleSet.owner.findRuleSet(description);
            this.numberFormat = null;
        } else if (description.charAt(0) == '#' || description.charAt(0) == '0') {
            this.ruleSet = null;
            this.numberFormat = (DecimalFormat)ruleSet.owner.getDecimalFormat().clone();
            this.numberFormat.applyPattern(description);
        } else if (description.charAt(0) == '>') {
            this.ruleSet = ruleSet;
            this.numberFormat = null;
        } else {
            throw new IllegalArgumentException("Illegal substitution syntax");
        }
    }

    public void setDivisor(int radix, short exponent) {
    }

    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }
        if (this.getClass() == that.getClass()) {
            NFSubstitution that2 = (NFSubstitution)that;
            return this.pos == that2.pos && (this.ruleSet != null || that2.ruleSet == null) && (this.numberFormat == null ? that2.numberFormat == null : this.numberFormat.equals(that2.numberFormat));
        }
        return false;
    }

    public int hashCode() {
        assert (false) : "hashCode not designed";
        return 42;
    }

    public String toString() {
        if (this.ruleSet != null) {
            return this.tokenChar() + this.ruleSet.getName() + this.tokenChar();
        }
        return this.tokenChar() + this.numberFormat.toPattern() + this.tokenChar();
    }

    public void doSubstitution(long number, StringBuilder toInsertInto, int position, int recursionCount) {
        if (this.ruleSet != null) {
            long numberToFormat = this.transformNumber(number);
            this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos, recursionCount);
        } else if (number <= 0x1FFFFFFFFFFFFFL) {
            double numberToFormat = this.transformNumber((double)number);
            if (this.numberFormat.getMaximumFractionDigits() == 0) {
                numberToFormat = Math.floor(numberToFormat);
            }
            toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat));
        } else {
            long numberToFormat = this.transformNumber(number);
            toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat));
        }
    }

    public void doSubstitution(double number, StringBuilder toInsertInto, int position, int recursionCount) {
        double numberToFormat = this.transformNumber(number);
        if (Double.isInfinite(numberToFormat)) {
            NFRule infiniteRule = this.ruleSet.findRule(Double.POSITIVE_INFINITY);
            infiniteRule.doFormat(numberToFormat, toInsertInto, position + this.pos, recursionCount);
            return;
        }
        if (numberToFormat == Math.floor(numberToFormat) && this.ruleSet != null) {
            this.ruleSet.format((long)numberToFormat, toInsertInto, position + this.pos, recursionCount);
        } else if (this.ruleSet != null) {
            this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos, recursionCount);
        } else {
            toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat));
        }
    }

    public abstract long transformNumber(long var1);

    public abstract double transformNumber(double var1);

    public Number doParse(String text, ParsePosition parsePosition, double baseValue, double upperBound, boolean lenientParse, int nonNumericalExecutedRuleMask) {
        Number tempResult;
        upperBound = this.calcUpperBound(upperBound);
        if (this.ruleSet != null) {
            tempResult = this.ruleSet.parse(text, parsePosition, upperBound, nonNumericalExecutedRuleMask);
            if (lenientParse && !this.ruleSet.isFractionSet() && parsePosition.getIndex() == 0) {
                tempResult = this.ruleSet.owner.getDecimalFormat().parse(text, parsePosition);
            }
        } else {
            tempResult = this.numberFormat.parse(text, parsePosition);
        }
        if (parsePosition.getIndex() != 0) {
            double result = tempResult.doubleValue();
            if ((result = this.composeRuleValue(result, baseValue)) == (double)((long)result)) {
                return (long)result;
            }
            return new Double(result);
        }
        return tempResult;
    }

    public abstract double composeRuleValue(double var1, double var3);

    public abstract double calcUpperBound(double var1);

    public final int getPos() {
        return this.pos;
    }

    abstract char tokenChar();

    public boolean isModulusSubstitution() {
        return false;
    }

    public void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols) {
        if (this.numberFormat != null) {
            this.numberFormat.setDecimalFormatSymbols(newSymbols);
        }
    }
}

