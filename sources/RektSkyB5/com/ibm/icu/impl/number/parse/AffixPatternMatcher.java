/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.number.AffixUtils;
import com.ibm.icu.impl.number.parse.AffixTokenMatcherFactory;
import com.ibm.icu.impl.number.parse.CodePointMatcher;
import com.ibm.icu.impl.number.parse.IgnorablesMatcher;
import com.ibm.icu.impl.number.parse.SeriesMatcher;

public class AffixPatternMatcher
extends SeriesMatcher
implements AffixUtils.TokenConsumer {
    private final String affixPattern;
    private AffixTokenMatcherFactory factory;
    private IgnorablesMatcher ignorables;
    private int lastTypeOrCp;

    private AffixPatternMatcher(String affixPattern) {
        this.affixPattern = affixPattern;
    }

    public static AffixPatternMatcher fromAffixPattern(String affixPattern, AffixTokenMatcherFactory factory, int parseFlags) {
        if (affixPattern.isEmpty()) {
            return null;
        }
        AffixPatternMatcher series = new AffixPatternMatcher(affixPattern);
        series.factory = factory;
        series.ignorables = 0 != (parseFlags & 0x200) ? null : factory.ignorables();
        series.lastTypeOrCp = 0;
        AffixUtils.iterateWithConsumer(affixPattern, series);
        series.factory = null;
        series.ignorables = null;
        series.lastTypeOrCp = 0;
        series.freeze();
        return series;
    }

    @Override
    public void consumeToken(int typeOrCp) {
        block10: {
            block9: {
                if (!(this.ignorables == null || this.length() <= 0 || this.lastTypeOrCp >= 0 && this.ignorables.getSet().contains(this.lastTypeOrCp))) {
                    this.addMatcher(this.ignorables);
                }
                if (typeOrCp >= 0) break block9;
                switch (typeOrCp) {
                    case -1: {
                        this.addMatcher(this.factory.minusSign());
                        break block10;
                    }
                    case -2: {
                        this.addMatcher(this.factory.plusSign());
                        break block10;
                    }
                    case -3: {
                        this.addMatcher(this.factory.percent());
                        break block10;
                    }
                    case -4: {
                        this.addMatcher(this.factory.permille());
                        break block10;
                    }
                    case -9: 
                    case -8: 
                    case -7: 
                    case -6: 
                    case -5: {
                        this.addMatcher(this.factory.currency());
                        break block10;
                    }
                    default: {
                        throw new AssertionError();
                    }
                }
            }
            if (this.ignorables == null || !this.ignorables.getSet().contains(typeOrCp)) {
                this.addMatcher(CodePointMatcher.getInstance(typeOrCp));
            }
        }
        this.lastTypeOrCp = typeOrCp;
    }

    public String getPattern() {
        return this.affixPattern;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AffixPatternMatcher)) {
            return false;
        }
        return this.affixPattern.equals(((AffixPatternMatcher)other).affixPattern);
    }

    public int hashCode() {
        return this.affixPattern.hashCode();
    }

    @Override
    public String toString() {
        return this.affixPattern;
    }
}

