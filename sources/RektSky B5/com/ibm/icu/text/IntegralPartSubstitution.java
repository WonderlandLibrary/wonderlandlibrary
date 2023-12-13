/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.NFRuleSet;
import com.ibm.icu.text.NFSubstitution;

class IntegralPartSubstitution
extends NFSubstitution {
    IntegralPartSubstitution(int pos, NFRuleSet ruleSet, String description) {
        super(pos, ruleSet, description);
    }

    @Override
    public long transformNumber(long number) {
        return number;
    }

    @Override
    public double transformNumber(double number) {
        return Math.floor(number);
    }

    @Override
    public double composeRuleValue(double newRuleValue, double oldRuleValue) {
        return newRuleValue + oldRuleValue;
    }

    @Override
    public double calcUpperBound(double oldUpperBound) {
        return Double.MAX_VALUE;
    }

    @Override
    char tokenChar() {
        return '<';
    }
}

