/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.number.AffixPatternProvider;
import com.ibm.icu.impl.number.AffixUtils;
import com.ibm.icu.impl.number.ConstantMultiFieldModifier;
import com.ibm.icu.impl.number.CurrencySpacingEnabledModifier;
import com.ibm.icu.impl.number.DecimalQuantity;
import com.ibm.icu.impl.number.MicroProps;
import com.ibm.icu.impl.number.MicroPropsGenerator;
import com.ibm.icu.impl.number.Modifier;
import com.ibm.icu.impl.number.NumberStringBuilder;
import com.ibm.icu.impl.number.ParameterizedModifier;
import com.ibm.icu.impl.number.PatternStringUtils;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.Currency;

public class MutablePatternModifier
implements Modifier,
AffixUtils.SymbolProvider,
MicroPropsGenerator {
    final boolean isStrong;
    AffixPatternProvider patternInfo;
    NumberFormatter.SignDisplay signDisplay;
    boolean perMilleReplacesPercent;
    DecimalFormatSymbols symbols;
    NumberFormatter.UnitWidth unitWidth;
    Currency currency;
    PluralRules rules;
    int signum;
    StandardPlural plural;
    MicroPropsGenerator parent;
    StringBuilder currentAffix;

    public MutablePatternModifier(boolean isStrong) {
        this.isStrong = isStrong;
    }

    public void setPatternInfo(AffixPatternProvider patternInfo) {
        this.patternInfo = patternInfo;
    }

    public void setPatternAttributes(NumberFormatter.SignDisplay signDisplay, boolean perMille) {
        this.signDisplay = signDisplay;
        this.perMilleReplacesPercent = perMille;
    }

    public void setSymbols(DecimalFormatSymbols symbols, Currency currency, NumberFormatter.UnitWidth unitWidth, PluralRules rules) {
        assert (rules != null == this.needsPlurals());
        this.symbols = symbols;
        this.currency = currency;
        this.unitWidth = unitWidth;
        this.rules = rules;
    }

    public void setNumberProperties(int signum, StandardPlural plural) {
        assert (plural != null == this.needsPlurals());
        this.signum = signum;
        this.plural = plural;
    }

    public boolean needsPlurals() {
        return this.patternInfo.containsSymbolType(-7);
    }

    public ImmutablePatternModifier createImmutable() {
        return this.createImmutableAndChain(null);
    }

    public ImmutablePatternModifier createImmutableAndChain(MicroPropsGenerator parent) {
        NumberStringBuilder a2 = new NumberStringBuilder();
        NumberStringBuilder b2 = new NumberStringBuilder();
        if (this.needsPlurals()) {
            ParameterizedModifier pm = new ParameterizedModifier();
            for (StandardPlural plural : StandardPlural.VALUES) {
                this.setNumberProperties(1, plural);
                pm.setModifier(1, plural, this.createConstantModifier(a2, b2));
                this.setNumberProperties(0, plural);
                pm.setModifier(0, plural, this.createConstantModifier(a2, b2));
                this.setNumberProperties(-1, plural);
                pm.setModifier(-1, plural, this.createConstantModifier(a2, b2));
            }
            pm.freeze();
            return new ImmutablePatternModifier(pm, this.rules, parent);
        }
        this.setNumberProperties(1, null);
        ConstantMultiFieldModifier positive = this.createConstantModifier(a2, b2);
        this.setNumberProperties(0, null);
        ConstantMultiFieldModifier zero = this.createConstantModifier(a2, b2);
        this.setNumberProperties(-1, null);
        ConstantMultiFieldModifier negative = this.createConstantModifier(a2, b2);
        ParameterizedModifier pm = new ParameterizedModifier(positive, zero, negative);
        return new ImmutablePatternModifier(pm, null, parent);
    }

    private ConstantMultiFieldModifier createConstantModifier(NumberStringBuilder a2, NumberStringBuilder b2) {
        this.insertPrefix(a2.clear(), 0);
        this.insertSuffix(b2.clear(), 0);
        if (this.patternInfo.hasCurrencySign()) {
            return new CurrencySpacingEnabledModifier(a2, b2, !this.patternInfo.hasBody(), this.isStrong, this.symbols);
        }
        return new ConstantMultiFieldModifier(a2, b2, !this.patternInfo.hasBody(), this.isStrong);
    }

    public MicroPropsGenerator addToChain(MicroPropsGenerator parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public MicroProps processQuantity(DecimalQuantity fq) {
        MicroProps micros = this.parent.processQuantity(fq);
        if (this.needsPlurals()) {
            DecimalQuantity copy = fq.createCopy();
            micros.rounder.apply(copy);
            this.setNumberProperties(fq.signum(), copy.getStandardPlural(this.rules));
        } else {
            this.setNumberProperties(fq.signum(), null);
        }
        micros.modMiddle = this;
        return micros;
    }

    @Override
    public int apply(NumberStringBuilder output, int leftIndex, int rightIndex) {
        int prefixLen = this.insertPrefix(output, leftIndex);
        int suffixLen = this.insertSuffix(output, rightIndex + prefixLen);
        int overwriteLen = 0;
        if (!this.patternInfo.hasBody()) {
            overwriteLen = output.splice(leftIndex + prefixLen, rightIndex + prefixLen, "", 0, 0, null);
        }
        CurrencySpacingEnabledModifier.applyCurrencySpacing(output, leftIndex, prefixLen, rightIndex + prefixLen + overwriteLen, suffixLen, this.symbols);
        return prefixLen + overwriteLen + suffixLen;
    }

    @Override
    public int getPrefixLength() {
        this.prepareAffix(true);
        int result = AffixUtils.unescapedCount(this.currentAffix, true, this);
        return result;
    }

    @Override
    public int getCodePointCount() {
        this.prepareAffix(true);
        int result = AffixUtils.unescapedCount(this.currentAffix, false, this);
        this.prepareAffix(false);
        return result += AffixUtils.unescapedCount(this.currentAffix, false, this);
    }

    @Override
    public boolean isStrong() {
        return this.isStrong;
    }

    private int insertPrefix(NumberStringBuilder sb, int position) {
        this.prepareAffix(true);
        int length = AffixUtils.unescape(this.currentAffix, sb, position, this);
        return length;
    }

    private int insertSuffix(NumberStringBuilder sb, int position) {
        this.prepareAffix(false);
        int length = AffixUtils.unescape(this.currentAffix, sb, position, this);
        return length;
    }

    private void prepareAffix(boolean isPrefix) {
        if (this.currentAffix == null) {
            this.currentAffix = new StringBuilder();
        }
        PatternStringUtils.patternInfoToStringBuilder(this.patternInfo, isPrefix, this.signum, this.signDisplay, this.plural, this.perMilleReplacesPercent, this.currentAffix);
    }

    @Override
    public CharSequence getSymbol(int type) {
        switch (type) {
            case -1: {
                return this.symbols.getMinusSignString();
            }
            case -2: {
                return this.symbols.getPlusSignString();
            }
            case -3: {
                return this.symbols.getPercentString();
            }
            case -4: {
                return this.symbols.getPerMillString();
            }
            case -5: {
                if (this.unitWidth == NumberFormatter.UnitWidth.ISO_CODE) {
                    return this.currency.getCurrencyCode();
                }
                if (this.unitWidth == NumberFormatter.UnitWidth.HIDDEN) {
                    return "";
                }
                int selector = this.unitWidth == NumberFormatter.UnitWidth.NARROW ? 3 : 0;
                return this.currency.getName(this.symbols.getULocale(), selector, null);
            }
            case -6: {
                return this.currency.getCurrencyCode();
            }
            case -7: {
                assert (this.plural != null);
                return this.currency.getName(this.symbols.getULocale(), 2, this.plural.getKeyword(), null);
            }
            case -8: {
                return "\ufffd";
            }
            case -9: {
                return this.currency.getName(this.symbols.getULocale(), 3, null);
            }
        }
        throw new AssertionError();
    }

    public static class ImmutablePatternModifier
    implements MicroPropsGenerator {
        final ParameterizedModifier pm;
        final PluralRules rules;
        final MicroPropsGenerator parent;

        ImmutablePatternModifier(ParameterizedModifier pm, PluralRules rules, MicroPropsGenerator parent) {
            this.pm = pm;
            this.rules = rules;
            this.parent = parent;
        }

        @Override
        public MicroProps processQuantity(DecimalQuantity quantity) {
            MicroProps micros = this.parent.processQuantity(quantity);
            this.applyToMicros(micros, quantity);
            return micros;
        }

        public void applyToMicros(MicroProps micros, DecimalQuantity quantity) {
            if (this.rules == null) {
                micros.modMiddle = this.pm.getModifier(quantity.signum());
            } else {
                DecimalQuantity copy = quantity.createCopy();
                copy.roundToInfinity();
                StandardPlural plural = copy.getStandardPlural(this.rules);
                micros.modMiddle = this.pm.getModifier(quantity.signum(), plural);
            }
        }
    }
}

