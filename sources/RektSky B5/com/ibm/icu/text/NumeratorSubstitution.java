/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.NFRuleSet;
import com.ibm.icu.text.NFSubstitution;
import java.text.ParsePosition;

class NumeratorSubstitution
extends NFSubstitution {
    private final double denominator;
    private final boolean withZeros;

    NumeratorSubstitution(int pos, double denominator, NFRuleSet ruleSet, String description) {
        super(pos, ruleSet, NumeratorSubstitution.fixdesc(description));
        this.denominator = denominator;
        this.withZeros = description.endsWith("<<");
    }

    static String fixdesc(String description) {
        return description.endsWith("<<") ? description.substring(0, description.length() - 1) : description;
    }

    @Override
    public boolean equals(Object that) {
        if (super.equals(that)) {
            NumeratorSubstitution that2 = (NumeratorSubstitution)that;
            return this.denominator == that2.denominator && this.withZeros == that2.withZeros;
        }
        return false;
    }

    @Override
    public void doSubstitution(double number, StringBuilder toInsertInto, int position, int recursionCount) {
        double numberToFormat = this.transformNumber(number);
        if (this.withZeros && this.ruleSet != null) {
            long nf = (long)numberToFormat;
            int len = toInsertInto.length();
            while (true) {
                long l2;
                nf *= 10L;
                if (!((double)l2 < this.denominator)) break;
                toInsertInto.insert(position + this.pos, ' ');
                this.ruleSet.format(0L, toInsertInto, position + this.pos, recursionCount);
            }
            position += toInsertInto.length() - len;
        }
        if (numberToFormat == Math.floor(numberToFormat) && this.ruleSet != null) {
            this.ruleSet.format((long)numberToFormat, toInsertInto, position + this.pos, recursionCount);
        } else if (this.ruleSet != null) {
            this.ruleSet.format(numberToFormat, toInsertInto, position + this.pos, recursionCount);
        } else {
            toInsertInto.insert(position + this.pos, this.numberFormat.format(numberToFormat));
        }
    }

    @Override
    public long transformNumber(long number) {
        return Math.round((double)number * this.denominator);
    }

    @Override
    public double transformNumber(double number) {
        return Math.round(number * this.denominator);
    }

    @Override
    public Number doParse(String text, ParsePosition parsePosition, double baseValue, double upperBound, boolean lenientParse, int nonNumericalExecutedRuleMask) {
        int zeroCount = 0;
        if (this.withZeros) {
            String workText = text;
            ParsePosition workPos = new ParsePosition(1);
            while (workText.length() > 0 && workPos.getIndex() != 0) {
                workPos.setIndex(0);
                this.ruleSet.parse(workText, workPos, 1.0, nonNumericalExecutedRuleMask).intValue();
                if (workPos.getIndex() == 0) break;
                ++zeroCount;
                parsePosition.setIndex(parsePosition.getIndex() + workPos.getIndex());
                workText = workText.substring(workPos.getIndex());
                while (workText.length() > 0 && workText.charAt(0) == ' ') {
                    workText = workText.substring(1);
                    parsePosition.setIndex(parsePosition.getIndex() + 1);
                }
            }
            text = text.substring(parsePosition.getIndex());
            parsePosition.setIndex(0);
        }
        Number result = super.doParse(text, parsePosition, this.withZeros ? 1.0 : baseValue, upperBound, false, nonNumericalExecutedRuleMask);
        if (this.withZeros) {
            long d2;
            long n2 = result.longValue();
            for (d2 = 1L; d2 <= n2; d2 *= 10L) {
            }
            while (zeroCount > 0) {
                d2 *= 10L;
                --zeroCount;
            }
            result = new Double((double)n2 / (double)d2);
        }
        return result;
    }

    @Override
    public double composeRuleValue(double newRuleValue, double oldRuleValue) {
        return newRuleValue / oldRuleValue;
    }

    @Override
    public double calcUpperBound(double oldUpperBound) {
        return this.denominator;
    }

    @Override
    char tokenChar() {
        return '<';
    }
}

