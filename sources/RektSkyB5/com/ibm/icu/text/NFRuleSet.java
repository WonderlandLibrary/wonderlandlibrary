/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NFRule;
import com.ibm.icu.text.RuleBasedNumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.LinkedList;

final class NFRuleSet {
    private final String name;
    private NFRule[] rules;
    final NFRule[] nonNumericalRules = new NFRule[6];
    LinkedList<NFRule> fractionRules;
    static final int NEGATIVE_RULE_INDEX = 0;
    static final int IMPROPER_FRACTION_RULE_INDEX = 1;
    static final int PROPER_FRACTION_RULE_INDEX = 2;
    static final int MASTER_RULE_INDEX = 3;
    static final int INFINITY_RULE_INDEX = 4;
    static final int NAN_RULE_INDEX = 5;
    final RuleBasedNumberFormat owner;
    private boolean isFractionRuleSet = false;
    private final boolean isParseable;
    private static final int RECURSION_LIMIT = 64;

    public NFRuleSet(RuleBasedNumberFormat owner, String[] descriptions, int index) throws IllegalArgumentException {
        this.owner = owner;
        String description = descriptions[index];
        if (description.length() == 0) {
            throw new IllegalArgumentException("Empty rule set description");
        }
        if (description.charAt(0) == '%') {
            int pos = description.indexOf(58);
            if (pos == -1) {
                throw new IllegalArgumentException("Rule set name doesn't end in colon");
            }
            String name = description.substring(0, pos);
            boolean bl = this.isParseable = !name.endsWith("@noparse");
            if (!this.isParseable) {
                name = name.substring(0, name.length() - 8);
            }
            this.name = name;
            while (pos < description.length() && PatternProps.isWhiteSpace(description.charAt(++pos))) {
            }
            descriptions[index] = description = description.substring(pos);
        } else {
            this.name = "%default";
            this.isParseable = true;
        }
        if (description.length() == 0) {
            throw new IllegalArgumentException("Empty rule set description");
        }
    }

    public void parseRules(String description) {
        int p2;
        ArrayList<NFRule> tempRules = new ArrayList<NFRule>();
        NFRule predecessor = null;
        int oldP = 0;
        int descriptionLen = description.length();
        do {
            if ((p2 = description.indexOf(59, oldP)) < 0) {
                p2 = descriptionLen;
            }
            NFRule.makeRules(description.substring(oldP, p2), this, predecessor, this.owner, tempRules);
            if (tempRules.isEmpty()) continue;
            predecessor = (NFRule)tempRules.get(tempRules.size() - 1);
        } while ((oldP = p2 + 1) < descriptionLen);
        long defaultBaseValue = 0L;
        for (NFRule rule : tempRules) {
            long baseValue = rule.getBaseValue();
            if (baseValue == 0L) {
                rule.setBaseValue(defaultBaseValue);
            } else {
                if (baseValue < defaultBaseValue) {
                    throw new IllegalArgumentException("Rules are not in order, base: " + baseValue + " < " + defaultBaseValue);
                }
                defaultBaseValue = baseValue;
            }
            if (this.isFractionRuleSet) continue;
            ++defaultBaseValue;
        }
        this.rules = new NFRule[tempRules.size()];
        tempRules.toArray(this.rules);
    }

    void setNonNumericalRule(NFRule rule) {
        long baseValue = rule.getBaseValue();
        if (baseValue == -1L) {
            this.nonNumericalRules[0] = rule;
        } else if (baseValue == -2L) {
            this.setBestFractionRule(1, rule, true);
        } else if (baseValue == -3L) {
            this.setBestFractionRule(2, rule, true);
        } else if (baseValue == -4L) {
            this.setBestFractionRule(3, rule, true);
        } else if (baseValue == -5L) {
            this.nonNumericalRules[4] = rule;
        } else if (baseValue == -6L) {
            this.nonNumericalRules[5] = rule;
        }
    }

    private void setBestFractionRule(int originalIndex, NFRule newRule, boolean rememberRule) {
        NFRule bestResult;
        if (rememberRule) {
            if (this.fractionRules == null) {
                this.fractionRules = new LinkedList();
            }
            this.fractionRules.add(newRule);
        }
        if ((bestResult = this.nonNumericalRules[originalIndex]) == null) {
            this.nonNumericalRules[originalIndex] = newRule;
        } else {
            DecimalFormatSymbols decimalFormatSymbols = this.owner.getDecimalFormatSymbols();
            if (decimalFormatSymbols.getDecimalSeparator() == newRule.getDecimalPoint()) {
                this.nonNumericalRules[originalIndex] = newRule;
            }
        }
    }

    public void makeIntoFractionRuleSet() {
        this.isFractionRuleSet = true;
    }

    public boolean equals(Object that) {
        int i2;
        if (!(that instanceof NFRuleSet)) {
            return false;
        }
        NFRuleSet that2 = (NFRuleSet)that;
        if (!this.name.equals(that2.name) || this.rules.length != that2.rules.length || this.isFractionRuleSet != that2.isFractionRuleSet) {
            return false;
        }
        for (i2 = 0; i2 < this.nonNumericalRules.length; ++i2) {
            if (Utility.objectEquals(this.nonNumericalRules[i2], that2.nonNumericalRules[i2])) continue;
            return false;
        }
        for (i2 = 0; i2 < this.rules.length; ++i2) {
            if (this.rules[i2].equals(that2.rules[i2])) continue;
            return false;
        }
        return true;
    }

    public int hashCode() {
        assert (false) : "hashCode not designed";
        return 42;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.name).append(":\n");
        for (NFRule rule : this.rules) {
            result.append(rule.toString()).append("\n");
        }
        for (NFRule rule : this.nonNumericalRules) {
            if (rule == null) continue;
            if (rule.getBaseValue() == -2L || rule.getBaseValue() == -3L || rule.getBaseValue() == -4L) {
                for (NFRule fractionRule : this.fractionRules) {
                    if (fractionRule.getBaseValue() != rule.getBaseValue()) continue;
                    result.append(fractionRule.toString()).append("\n");
                }
                continue;
            }
            result.append(rule.toString()).append("\n");
        }
        return result.toString();
    }

    public boolean isFractionSet() {
        return this.isFractionRuleSet;
    }

    public String getName() {
        return this.name;
    }

    public boolean isPublic() {
        return !this.name.startsWith("%%");
    }

    public boolean isParseable() {
        return this.isParseable;
    }

    public void format(long number, StringBuilder toInsertInto, int pos, int recursionCount) {
        if (recursionCount >= 64) {
            throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
        }
        NFRule applicableRule = this.findNormalRule(number);
        applicableRule.doFormat(number, toInsertInto, pos, ++recursionCount);
    }

    public void format(double number, StringBuilder toInsertInto, int pos, int recursionCount) {
        if (recursionCount >= 64) {
            throw new IllegalStateException("Recursion limit exceeded when applying ruleSet " + this.name);
        }
        NFRule applicableRule = this.findRule(number);
        applicableRule.doFormat(number, toInsertInto, pos, ++recursionCount);
    }

    NFRule findRule(double number) {
        if (this.isFractionRuleSet) {
            return this.findFractionRuleSetRule(number);
        }
        if (Double.isNaN(number)) {
            NFRule rule = this.nonNumericalRules[5];
            if (rule == null) {
                rule = this.owner.getDefaultNaNRule();
            }
            return rule;
        }
        if (number < 0.0) {
            if (this.nonNumericalRules[0] != null) {
                return this.nonNumericalRules[0];
            }
            number = -number;
        }
        if (Double.isInfinite(number)) {
            NFRule rule = this.nonNumericalRules[4];
            if (rule == null) {
                rule = this.owner.getDefaultInfinityRule();
            }
            return rule;
        }
        if (number != Math.floor(number)) {
            if (number < 1.0 && this.nonNumericalRules[2] != null) {
                return this.nonNumericalRules[2];
            }
            if (this.nonNumericalRules[1] != null) {
                return this.nonNumericalRules[1];
            }
        }
        if (this.nonNumericalRules[3] != null) {
            return this.nonNumericalRules[3];
        }
        return this.findNormalRule(Math.round(number));
    }

    private NFRule findNormalRule(long number) {
        if (this.isFractionRuleSet) {
            return this.findFractionRuleSetRule(number);
        }
        if (number < 0L) {
            if (this.nonNumericalRules[0] != null) {
                return this.nonNumericalRules[0];
            }
            number = -number;
        }
        int lo = 0;
        int hi = this.rules.length;
        if (hi > 0) {
            while (lo < hi) {
                int mid = lo + hi >>> 1;
                long ruleBaseValue = this.rules[mid].getBaseValue();
                if (ruleBaseValue == number) {
                    return this.rules[mid];
                }
                if (ruleBaseValue > number) {
                    hi = mid;
                    continue;
                }
                lo = mid + 1;
            }
            if (hi == 0) {
                throw new IllegalStateException("The rule set " + this.name + " cannot format the value " + number);
            }
            NFRule result = this.rules[hi - 1];
            if (result.shouldRollBack(number)) {
                if (hi == 1) {
                    throw new IllegalStateException("The rule set " + this.name + " cannot roll back from the rule '" + result + "'");
                }
                result = this.rules[hi - 2];
            }
            return result;
        }
        return this.nonNumericalRules[3];
    }

    private NFRule findFractionRuleSetRule(double number) {
        long leastCommonMultiple = this.rules[0].getBaseValue();
        for (int i2 = 1; i2 < this.rules.length; ++i2) {
            leastCommonMultiple = NFRuleSet.lcm(leastCommonMultiple, this.rules[i2].getBaseValue());
        }
        long numerator = Math.round(number * (double)leastCommonMultiple);
        long difference = Long.MAX_VALUE;
        int winner = 0;
        for (int i3 = 0; i3 < this.rules.length; ++i3) {
            long tempDifference = numerator * this.rules[i3].getBaseValue() % leastCommonMultiple;
            if (leastCommonMultiple - tempDifference < tempDifference) {
                tempDifference = leastCommonMultiple - tempDifference;
            }
            if (tempDifference >= difference) continue;
            difference = tempDifference;
            winner = i3;
            if (difference == 0L) break;
        }
        if (winner + 1 < this.rules.length && this.rules[winner + 1].getBaseValue() == this.rules[winner].getBaseValue() && (Math.round(number * (double)this.rules[winner].getBaseValue()) < 1L || Math.round(number * (double)this.rules[winner].getBaseValue()) >= 2L)) {
            ++winner;
        }
        return this.rules[winner];
    }

    private static long lcm(long x2, long y2) {
        long x1 = x2;
        long y1 = y2;
        int p2 = 0;
        while ((x1 & 1L) == 0L && (y1 & 1L) == 0L) {
            ++p2;
            x1 >>= 1;
            y1 >>= 1;
        }
        long t2 = (x1 & 1L) == 1L ? -y1 : x1;
        while (t2 != 0L) {
            while ((t2 & 1L) == 0L) {
                t2 >>= 1;
            }
            if (t2 > 0L) {
                x1 = t2;
            } else {
                y1 = -t2;
            }
            t2 = x1 - y1;
        }
        long gcd = x1 << p2;
        return x2 / gcd * y2;
    }

    public Number parse(String text, ParsePosition parsePosition, double upperBound, int nonNumericalExecutedRuleMask) {
        Number tempResult;
        ParsePosition highWaterMark = new ParsePosition(0);
        Number result = NFRule.ZERO;
        if (text.length() == 0) {
            return result;
        }
        for (int nonNumericalRuleIdx = 0; nonNumericalRuleIdx < this.nonNumericalRules.length; ++nonNumericalRuleIdx) {
            NFRule nonNumericalRule = this.nonNumericalRules[nonNumericalRuleIdx];
            if (nonNumericalRule == null || (nonNumericalExecutedRuleMask >> nonNumericalRuleIdx & 1) != 0) continue;
            tempResult = nonNumericalRule.doParse(text, parsePosition, false, upperBound, nonNumericalExecutedRuleMask |= 1 << nonNumericalRuleIdx);
            if (parsePosition.getIndex() > highWaterMark.getIndex()) {
                result = tempResult;
                highWaterMark.setIndex(parsePosition.getIndex());
            }
            parsePosition.setIndex(0);
        }
        for (int i2 = this.rules.length - 1; i2 >= 0 && highWaterMark.getIndex() < text.length(); --i2) {
            if (!this.isFractionRuleSet && (double)this.rules[i2].getBaseValue() >= upperBound) continue;
            tempResult = this.rules[i2].doParse(text, parsePosition, this.isFractionRuleSet, upperBound, nonNumericalExecutedRuleMask);
            if (parsePosition.getIndex() > highWaterMark.getIndex()) {
                result = tempResult;
                highWaterMark.setIndex(parsePosition.getIndex());
            }
            parsePosition.setIndex(0);
        }
        parsePosition.setIndex(highWaterMark.getIndex());
        return result;
    }

    public void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols) {
        for (NFRule rule : this.rules) {
            rule.setDecimalFormatSymbols(newSymbols);
        }
        if (this.fractionRules != null) {
            for (int nonNumericalIdx = 1; nonNumericalIdx <= 3; ++nonNumericalIdx) {
                if (this.nonNumericalRules[nonNumericalIdx] == null) continue;
                for (NFRule rule : this.fractionRules) {
                    if (this.nonNumericalRules[nonNumericalIdx].getBaseValue() != rule.getBaseValue()) continue;
                    this.setBestFractionRule(nonNumericalIdx, rule, false);
                }
            }
        }
        for (NFRule rule : this.nonNumericalRules) {
            if (rule == null) continue;
            rule.setDecimalFormatSymbols(newSymbols);
        }
    }
}

