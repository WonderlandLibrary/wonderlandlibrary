/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.number.AffixPatternProvider;
import com.ibm.icu.impl.number.AffixUtils;
import com.ibm.icu.impl.number.DecimalFormatProperties;

public class PropertiesAffixPatternProvider
implements AffixPatternProvider {
    private final String posPrefix;
    private final String posSuffix;
    private final String negPrefix;
    private final String negSuffix;

    public PropertiesAffixPatternProvider(DecimalFormatProperties properties) {
        String ppo = AffixUtils.escape(properties.getPositivePrefix());
        String pso = AffixUtils.escape(properties.getPositiveSuffix());
        String npo = AffixUtils.escape(properties.getNegativePrefix());
        String nso = AffixUtils.escape(properties.getNegativeSuffix());
        String ppp = properties.getPositivePrefixPattern();
        String psp = properties.getPositiveSuffixPattern();
        String npp = properties.getNegativePrefixPattern();
        String nsp = properties.getNegativeSuffixPattern();
        this.posPrefix = ppo != null ? ppo : (ppp != null ? ppp : "");
        this.posSuffix = pso != null ? pso : (psp != null ? psp : "");
        if (npo != null) {
            this.negPrefix = npo;
        } else if (npp != null) {
            this.negPrefix = npp;
        } else {
            String string = this.negPrefix = ppp == null ? "-" : "-" + ppp;
        }
        this.negSuffix = nso != null ? nso : (nsp != null ? nsp : (psp == null ? "" : psp));
    }

    @Override
    public char charAt(int flags, int i2) {
        return this.getString(flags).charAt(i2);
    }

    @Override
    public int length(int flags) {
        return this.getString(flags).length();
    }

    @Override
    public String getString(int flags) {
        boolean negative;
        boolean prefix = (flags & 0x100) != 0;
        boolean bl = negative = (flags & 0x200) != 0;
        if (prefix && negative) {
            return this.negPrefix;
        }
        if (prefix) {
            return this.posPrefix;
        }
        if (negative) {
            return this.negSuffix;
        }
        return this.posSuffix;
    }

    @Override
    public boolean positiveHasPlusSign() {
        return AffixUtils.containsType(this.posPrefix, -2) || AffixUtils.containsType(this.posSuffix, -2);
    }

    @Override
    public boolean hasNegativeSubpattern() {
        return true;
    }

    @Override
    public boolean negativeHasMinusSign() {
        return AffixUtils.containsType(this.negPrefix, -1) || AffixUtils.containsType(this.negSuffix, -1);
    }

    @Override
    public boolean hasCurrencySign() {
        return AffixUtils.hasCurrencySymbols(this.posPrefix) || AffixUtils.hasCurrencySymbols(this.posSuffix) || AffixUtils.hasCurrencySymbols(this.negPrefix) || AffixUtils.hasCurrencySymbols(this.negSuffix);
    }

    @Override
    public boolean containsSymbolType(int type) {
        return AffixUtils.containsType(this.posPrefix, type) || AffixUtils.containsType(this.posSuffix, type) || AffixUtils.containsType(this.negPrefix, type) || AffixUtils.containsType(this.negSuffix, type);
    }

    @Override
    public boolean hasBody() {
        return true;
    }

    public String toString() {
        return super.toString() + " {" + this.posPrefix + "#" + this.posSuffix + ";" + this.negPrefix + "#" + this.negSuffix + "}";
    }
}

