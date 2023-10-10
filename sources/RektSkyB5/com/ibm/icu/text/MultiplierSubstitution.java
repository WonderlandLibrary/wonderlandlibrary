/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.NFRule;
import com.ibm.icu.text.NFRuleSet;
import com.ibm.icu.text.NFSubstitution;

class MultiplierSubstitution
extends NFSubstitution {
    long divisor;

    MultiplierSubstitution(int pos, NFRule rule, NFRuleSet ruleSet, String description) {
        super(pos, ruleSet, description);
        this.divisor = rule.getDivisor();
        if (this.divisor == 0L) {
            throw new IllegalStateException("Substitution with divisor 0 " + description.substring(0, pos) + " | " + description.substring(pos));
        }
    }

    @Override
    public void setDivisor(int radix, short exponent) {
        this.divisor = NFRule.power(radix, exponent);
        if (this.divisor == 0L) {
            throw new IllegalStateException("Substitution with divisor 0");
        }
    }

    @Override
    public boolean equals(Object that) {
        return super.equals(that) && this.divisor == ((MultiplierSubstitution)that).divisor;
    }

    @Override
    public long transformNumber(long number) {
        return (long)Math.floor(number / this.divisor);
    }

    @Override
    public double transformNumber(double number) {
        if (this.ruleSet == null) {
            return number / (double)this.divisor;
        }
        return Math.floor(number / (double)this.divisor);
    }

    @Override
    public double composeRuleValue(double newRuleValue, double oldRuleValue) {
        return newRuleValue * (double)this.divisor;
    }

    @Override
    public double calcUpperBound(double oldUpperBound) {
        return this.divisor;
    }

    @Override
    char tokenChar() {
        return '<';
    }
}

