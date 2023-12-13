/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.TextTrieMap;
import com.ibm.icu.impl.number.parse.NumberParseMatcher;
import com.ibm.icu.impl.number.parse.ParsedNumber;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.util.Currency;
import java.util.Iterator;

public class CombinedCurrencyMatcher
implements NumberParseMatcher {
    private final String isoCode;
    private final String currency1;
    private final String currency2;
    private final String[] localLongNames;
    private final String afterPrefixInsert;
    private final String beforeSuffixInsert;
    private final TextTrieMap<Currency.CurrencyStringInfo> longNameTrie;
    private final TextTrieMap<Currency.CurrencyStringInfo> symbolTrie;

    public static CombinedCurrencyMatcher getInstance(Currency currency, DecimalFormatSymbols dfs, int parseFlags) {
        return new CombinedCurrencyMatcher(currency, dfs, parseFlags);
    }

    private CombinedCurrencyMatcher(Currency currency, DecimalFormatSymbols dfs, int parseFlags) {
        this.isoCode = currency.getSubtype();
        this.currency1 = currency.getSymbol(dfs.getULocale());
        this.currency2 = currency.getCurrencyCode();
        this.afterPrefixInsert = dfs.getPatternForCurrencySpacing(2, false);
        this.beforeSuffixInsert = dfs.getPatternForCurrencySpacing(2, true);
        if (0 == (parseFlags & 0x2000)) {
            this.longNameTrie = Currency.getParsingTrie(dfs.getULocale(), 1);
            this.symbolTrie = Currency.getParsingTrie(dfs.getULocale(), 0);
            this.localLongNames = null;
        } else {
            this.longNameTrie = null;
            this.symbolTrie = null;
            this.localLongNames = new String[StandardPlural.COUNT];
            for (int i2 = 0; i2 < StandardPlural.COUNT; ++i2) {
                String pluralKeyword = StandardPlural.VALUES.get(i2).getKeyword();
                this.localLongNames[i2] = currency.getName(dfs.getLocale(), 2, pluralKeyword, null);
            }
        }
    }

    @Override
    public boolean match(StringSegment segment, ParsedNumber result) {
        int overlap;
        if (result.currencyCode != null) {
            return false;
        }
        int initialOffset = segment.getOffset();
        boolean maybeMore = false;
        if (result.seenNumber() && !this.beforeSuffixInsert.isEmpty()) {
            overlap = segment.getCommonPrefixLength(this.beforeSuffixInsert);
            if (overlap == this.beforeSuffixInsert.length()) {
                segment.adjustOffset(overlap);
            }
            maybeMore = maybeMore || overlap == segment.length();
        }
        boolean bl = maybeMore = maybeMore || this.matchCurrency(segment, result);
        if (result.currencyCode == null) {
            segment.setOffset(initialOffset);
            return maybeMore;
        }
        if (!result.seenNumber() && !this.afterPrefixInsert.isEmpty()) {
            overlap = segment.getCommonPrefixLength(this.afterPrefixInsert);
            if (overlap == this.afterPrefixInsert.length()) {
                segment.adjustOffset(overlap);
            }
            maybeMore = maybeMore || overlap == segment.length();
        }
        return maybeMore;
    }

    private boolean matchCurrency(StringSegment segment, ParsedNumber result) {
        boolean maybeMore = false;
        int overlap1 = !this.currency1.isEmpty() ? segment.getCaseSensitivePrefixLength(this.currency1) : -1;
        boolean bl = maybeMore = maybeMore || overlap1 == segment.length();
        if (overlap1 == this.currency1.length()) {
            result.currencyCode = this.isoCode;
            segment.adjustOffset(overlap1);
            result.setCharsConsumed(segment);
            return maybeMore;
        }
        int overlap2 = !this.currency2.isEmpty() ? segment.getCaseSensitivePrefixLength(this.currency2) : -1;
        boolean bl2 = maybeMore = maybeMore || overlap2 == segment.length();
        if (overlap2 == this.currency2.length()) {
            result.currencyCode = this.isoCode;
            segment.adjustOffset(overlap2);
            result.setCharsConsumed(segment);
            return maybeMore;
        }
        if (this.longNameTrie != null) {
            TextTrieMap.Output trieOutput = new TextTrieMap.Output();
            Iterator<Currency.CurrencyStringInfo> values = this.longNameTrie.get(segment, 0, trieOutput);
            boolean bl3 = maybeMore = maybeMore || trieOutput.partialMatch;
            if (values == null) {
                values = this.symbolTrie.get(segment, 0, trieOutput);
                boolean bl4 = maybeMore = maybeMore || trieOutput.partialMatch;
            }
            if (values != null) {
                result.currencyCode = values.next().getISOCode();
                segment.adjustOffset(trieOutput.matchLength);
                result.setCharsConsumed(segment);
                return maybeMore;
            }
        } else {
            int longestFullMatch = 0;
            for (int i2 = 0; i2 < StandardPlural.COUNT; ++i2) {
                String name = this.localLongNames[i2];
                if (name.isEmpty()) continue;
                int overlap = segment.getCommonPrefixLength(name);
                if (overlap == name.length() && name.length() > longestFullMatch) {
                    longestFullMatch = name.length();
                }
                maybeMore = maybeMore || overlap > 0;
            }
            if (longestFullMatch > 0) {
                result.currencyCode = this.isoCode;
                segment.adjustOffset(longestFullMatch);
                result.setCharsConsumed(segment);
                return maybeMore;
            }
        }
        return maybeMore;
    }

    @Override
    public boolean smokeTest(StringSegment segment) {
        return true;
    }

    @Override
    public void postProcess(ParsedNumber result) {
    }

    public String toString() {
        return "<CombinedCurrencyMatcher " + this.isoCode + ">";
    }
}

