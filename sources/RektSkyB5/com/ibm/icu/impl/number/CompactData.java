/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.impl.number.MultiplierProducer;
import com.ibm.icu.text.CompactDecimalFormat;
import com.ibm.icu.util.ICUException;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class CompactData
implements MultiplierProducer {
    private static final String USE_FALLBACK = "<USE FALLBACK>";
    private final String[] patterns = new String[16 * StandardPlural.COUNT];
    private final byte[] multipliers = new byte[16];
    private byte largestMagnitude = 0;
    private boolean isEmpty = true;
    private static final int COMPACT_MAX_DIGITS = 15;

    public void populate(ULocale locale, String nsName, CompactDecimalFormat.CompactStyle compactStyle, CompactType compactType) {
        assert (this.isEmpty);
        CompactDataSink sink = new CompactDataSink(this);
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", locale);
        boolean nsIsLatn = nsName.equals("latn");
        boolean compactIsShort = compactStyle == CompactDecimalFormat.CompactStyle.SHORT;
        StringBuilder resourceKey = new StringBuilder();
        CompactData.getResourceBundleKey(nsName, compactStyle, compactType, resourceKey);
        rb.getAllItemsWithFallbackNoFail(resourceKey.toString(), sink);
        if (this.isEmpty && !nsIsLatn) {
            CompactData.getResourceBundleKey("latn", compactStyle, compactType, resourceKey);
            rb.getAllItemsWithFallbackNoFail(resourceKey.toString(), sink);
        }
        if (this.isEmpty && !compactIsShort) {
            CompactData.getResourceBundleKey(nsName, CompactDecimalFormat.CompactStyle.SHORT, compactType, resourceKey);
            rb.getAllItemsWithFallbackNoFail(resourceKey.toString(), sink);
        }
        if (this.isEmpty && !nsIsLatn && !compactIsShort) {
            CompactData.getResourceBundleKey("latn", CompactDecimalFormat.CompactStyle.SHORT, compactType, resourceKey);
            rb.getAllItemsWithFallbackNoFail(resourceKey.toString(), sink);
        }
        if (this.isEmpty) {
            throw new ICUException("Could not load compact decimal data for locale " + locale);
        }
    }

    private static void getResourceBundleKey(String nsName, CompactDecimalFormat.CompactStyle compactStyle, CompactType compactType, StringBuilder sb) {
        sb.setLength(0);
        sb.append("NumberElements/");
        sb.append(nsName);
        sb.append(compactStyle == CompactDecimalFormat.CompactStyle.SHORT ? "/patternsShort" : "/patternsLong");
        sb.append(compactType == CompactType.DECIMAL ? "/decimalFormat" : "/currencyFormat");
    }

    public void populate(Map<String, Map<String, String>> powersToPluralsToPatterns) {
        assert (this.isEmpty);
        for (Map.Entry<String, Map<String, String>> magnitudeEntry : powersToPluralsToPatterns.entrySet()) {
            byte magnitude = (byte)(magnitudeEntry.getKey().length() - 1);
            for (Map.Entry<String, String> pluralEntry : magnitudeEntry.getValue().entrySet()) {
                String patternString;
                StandardPlural plural = StandardPlural.fromString(pluralEntry.getKey().toString());
                this.patterns[CompactData.getIndex((int)magnitude, (StandardPlural)plural)] = patternString = pluralEntry.getValue().toString();
                int numZeros = CompactData.countZeros(patternString);
                if (numZeros <= 0) continue;
                this.multipliers[magnitude] = (byte)(numZeros - magnitude - 1);
                if (magnitude > this.largestMagnitude) {
                    this.largestMagnitude = magnitude;
                }
                this.isEmpty = false;
            }
        }
    }

    @Override
    public int getMultiplier(int magnitude) {
        if (magnitude < 0) {
            return 0;
        }
        if (magnitude > this.largestMagnitude) {
            magnitude = this.largestMagnitude;
        }
        return this.multipliers[magnitude];
    }

    public String getPattern(int magnitude, StandardPlural plural) {
        String patternString;
        if (magnitude < 0) {
            return null;
        }
        if (magnitude > this.largestMagnitude) {
            magnitude = this.largestMagnitude;
        }
        if ((patternString = this.patterns[CompactData.getIndex(magnitude, plural)]) == null && plural != StandardPlural.OTHER) {
            patternString = this.patterns[CompactData.getIndex(magnitude, StandardPlural.OTHER)];
        }
        if (patternString == USE_FALLBACK) {
            patternString = null;
        }
        return patternString;
    }

    public void getUniquePatterns(Set<String> output) {
        assert (output.isEmpty());
        output.addAll(Arrays.asList(this.patterns));
        output.remove(USE_FALLBACK);
        output.remove(null);
    }

    private static final int getIndex(int magnitude, StandardPlural plural) {
        return magnitude * StandardPlural.COUNT + plural.ordinal();
    }

    private static final int countZeros(String patternString) {
        int numZeros = 0;
        for (int i2 = 0; i2 < patternString.length(); ++i2) {
            if (patternString.charAt(i2) == '0') {
                ++numZeros;
                continue;
            }
            if (numZeros > 0) break;
        }
        return numZeros;
    }

    private static final class CompactDataSink
    extends UResource.Sink {
        CompactData data;

        public CompactDataSink(CompactData data) {
            this.data = data;
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean isRoot) {
            UResource.Table powersOfTenTable = value.getTable();
            int i3 = 0;
            while (powersOfTenTable.getKeyAndValue(i3, key, value)) {
                byte magnitude = (byte)(key.length() - 1);
                byte multiplier = this.data.multipliers[magnitude];
                assert (magnitude < 15);
                UResource.Table pluralVariantsTable = value.getTable();
                int i4 = 0;
                while (pluralVariantsTable.getKeyAndValue(i4, key, value)) {
                    StandardPlural plural = StandardPlural.fromString(key.toString());
                    if (this.data.patterns[CompactData.getIndex(magnitude, plural)] == null) {
                        int numZeros;
                        String patternString = value.toString();
                        if (patternString.equals("0")) {
                            patternString = CompactData.USE_FALLBACK;
                        }
                        ((CompactData)this.data).patterns[CompactData.getIndex((int)magnitude, (StandardPlural)plural)] = patternString;
                        if (multiplier == 0 && (numZeros = CompactData.countZeros(patternString)) > 0) {
                            multiplier = (byte)(numZeros - magnitude - 1);
                        }
                    }
                    ++i4;
                }
                if (this.data.multipliers[magnitude] == 0) {
                    ((CompactData)this.data).multipliers[magnitude] = multiplier;
                    if (magnitude > this.data.largestMagnitude) {
                        this.data.largestMagnitude = magnitude;
                    }
                    this.data.isEmpty = false;
                } else assert (this.data.multipliers[magnitude] == multiplier);
                ++i3;
            }
        }
    }

    public static enum CompactType {
        DECIMAL,
        CURRENCY;

    }
}

