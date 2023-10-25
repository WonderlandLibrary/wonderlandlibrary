/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberingSystem;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class CompactDecimalDataCache {
    private static final String SHORT_STYLE = "short";
    private static final String LONG_STYLE = "long";
    private static final String NUMBER_ELEMENTS = "NumberElements";
    private static final String PATTERN_LONG_PATH = "patternsLong/decimalFormat";
    private static final String PATTERNS_SHORT_PATH = "patternsShort/decimalFormat";
    static final String OTHER = "other";
    static final int MAX_DIGITS = 15;
    private static final String LATIN_NUMBERING_SYSTEM = "latn";
    private final ICUCache<ULocale, DataBundle> cache = new SimpleCache<ULocale, DataBundle>();

    CompactDecimalDataCache() {
    }

    DataBundle get(ULocale locale) {
        DataBundle result = this.cache.get(locale);
        if (result == null) {
            result = CompactDecimalDataCache.load(locale);
            this.cache.put(locale, result);
        }
        return result;
    }

    private static DataBundle load(ULocale ulocale) {
        ICUResourceBundle bundle;
        NumberingSystem ns = NumberingSystem.getInstance(ulocale);
        ICUResourceBundle r = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", ulocale);
        r = r.getWithFallback(NUMBER_ELEMENTS);
        String numberingSystemName = ns.getName();
        ICUResourceBundle shortDataBundle = null;
        ICUResourceBundle longDataBundle = null;
        if (!LATIN_NUMBERING_SYSTEM.equals(numberingSystemName)) {
            bundle = CompactDecimalDataCache.findWithFallback(r, numberingSystemName, UResFlags.NOT_ROOT);
            shortDataBundle = CompactDecimalDataCache.findWithFallback(bundle, PATTERNS_SHORT_PATH, UResFlags.NOT_ROOT);
            longDataBundle = CompactDecimalDataCache.findWithFallback(bundle, PATTERN_LONG_PATH, UResFlags.NOT_ROOT);
        }
        if (shortDataBundle == null) {
            bundle = CompactDecimalDataCache.getWithFallback(r, LATIN_NUMBERING_SYSTEM, UResFlags.ANY);
            shortDataBundle = CompactDecimalDataCache.getWithFallback(bundle, PATTERNS_SHORT_PATH, UResFlags.ANY);
            if (longDataBundle == null && (longDataBundle = CompactDecimalDataCache.findWithFallback(bundle, PATTERN_LONG_PATH, UResFlags.ANY)) != null && CompactDecimalDataCache.isRoot(longDataBundle) && !CompactDecimalDataCache.isRoot(shortDataBundle)) {
                longDataBundle = null;
            }
        }
        Data shortData = CompactDecimalDataCache.loadStyle(shortDataBundle, ulocale, SHORT_STYLE);
        Data longData = longDataBundle == null ? shortData : CompactDecimalDataCache.loadStyle(longDataBundle, ulocale, LONG_STYLE);
        return new DataBundle(shortData, longData);
    }

    private static ICUResourceBundle findWithFallback(ICUResourceBundle r, String path, UResFlags flags) {
        if (r == null) {
            return null;
        }
        ICUResourceBundle result = r.findWithFallback(path);
        if (result == null) {
            return null;
        }
        switch (flags) {
            case NOT_ROOT: {
                return CompactDecimalDataCache.isRoot(result) ? null : result;
            }
            case ANY: {
                return result;
            }
        }
        throw new IllegalArgumentException();
    }

    private static ICUResourceBundle getWithFallback(ICUResourceBundle r, String path, UResFlags flags) {
        ICUResourceBundle result = CompactDecimalDataCache.findWithFallback(r, path, flags);
        if (result == null) {
            throw new MissingResourceException("Cannot find " + path, ICUResourceBundle.class.getName(), path);
        }
        return result;
    }

    private static boolean isRoot(ICUResourceBundle r) {
        ULocale bundleLocale = r.getULocale();
        return bundleLocale.equals(ULocale.ROOT) || bundleLocale.toString().equals("root");
    }

    private static Data loadStyle(ICUResourceBundle r, ULocale locale, String style) {
        int size = r.getSize();
        Data result = new Data(new long[15], new HashMap<String, DecimalFormat.Unit[]>());
        for (int i = 0; i < size; ++i) {
            CompactDecimalDataCache.populateData(r.get(i), locale, style, result);
        }
        CompactDecimalDataCache.fillInMissing(result);
        return result;
    }

    private static void populateData(UResourceBundle divisorData, ULocale locale, String style, Data result) {
        long magnitude = Long.parseLong(divisorData.getKey());
        int thisIndex = (int)Math.log10(magnitude);
        if (thisIndex >= 15) {
            return;
        }
        int size = divisorData.getSize();
        int numZeros = 0;
        boolean otherVariantDefined = false;
        for (int i = 0; i < size; ++i) {
            int nz;
            UResourceBundle pluralVariantData = divisorData.get(i);
            String pluralVariant = pluralVariantData.getKey();
            String template = pluralVariantData.getString();
            if (pluralVariant.equals(OTHER)) {
                otherVariantDefined = true;
            }
            if ((nz = CompactDecimalDataCache.populatePrefixSuffix(pluralVariant, thisIndex, template, locale, style, result)) == numZeros) continue;
            if (numZeros != 0) {
                throw new IllegalArgumentException("Plural variant '" + pluralVariant + "' template '" + template + "' for 10^" + thisIndex + " has wrong number of zeros in " + CompactDecimalDataCache.localeAndStyle(locale, style));
            }
            numZeros = nz;
        }
        if (!otherVariantDefined) {
            throw new IllegalArgumentException("No 'other' plural variant defined for 10^" + thisIndex + "in " + CompactDecimalDataCache.localeAndStyle(locale, style));
        }
        long divisor = magnitude;
        for (int i = 1; i < numZeros; ++i) {
            divisor /= 10L;
        }
        result.divisors[thisIndex] = divisor;
    }

    private static int populatePrefixSuffix(String pluralVariant, int idx, String template, ULocale locale, String style, Data result) {
        int i;
        int firstIdx = template.indexOf("0");
        int lastIdx = template.lastIndexOf("0");
        if (firstIdx == -1) {
            throw new IllegalArgumentException("Expect at least one zero in template '" + template + "' for variant '" + pluralVariant + "' for 10^" + idx + " in " + CompactDecimalDataCache.localeAndStyle(locale, style));
        }
        String prefix = CompactDecimalDataCache.fixQuotes(template.substring(0, firstIdx));
        String suffix = CompactDecimalDataCache.fixQuotes(template.substring(lastIdx + 1));
        CompactDecimalDataCache.saveUnit(new DecimalFormat.Unit(prefix, suffix), pluralVariant, idx, result.units);
        if (prefix.trim().length() == 0 && suffix.trim().length() == 0) {
            return idx + 1;
        }
        for (i = firstIdx + 1; i <= lastIdx && template.charAt(i) == '0'; ++i) {
        }
        return i - firstIdx;
    }

    private static String fixQuotes(String prefixOrSuffix) {
        StringBuilder result = new StringBuilder();
        int len = prefixOrSuffix.length();
        QuoteState state = QuoteState.OUTSIDE;
        block4: for (int idx = 0; idx < len; ++idx) {
            char ch = prefixOrSuffix.charAt(idx);
            if (ch == '\'') {
                if (state == QuoteState.INSIDE_EMPTY) {
                    result.append('\'');
                }
            } else {
                result.append(ch);
            }
            switch (state) {
                case OUTSIDE: {
                    state = ch == '\'' ? QuoteState.INSIDE_EMPTY : QuoteState.OUTSIDE;
                    continue block4;
                }
                case INSIDE_EMPTY: 
                case INSIDE_FULL: {
                    state = ch == '\'' ? QuoteState.OUTSIDE : QuoteState.INSIDE_FULL;
                    continue block4;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
        }
        return result.toString();
    }

    private static String localeAndStyle(ULocale locale, String style) {
        return "locale '" + locale + "' style '" + style + "'";
    }

    private static void fillInMissing(Data result) {
        long lastDivisor = 1L;
        for (int i = 0; i < result.divisors.length; ++i) {
            if (result.units.get(OTHER)[i] == null) {
                result.divisors[i] = lastDivisor;
                CompactDecimalDataCache.copyFromPreviousIndex(i, result.units);
                continue;
            }
            lastDivisor = result.divisors[i];
            CompactDecimalDataCache.propagateOtherToMissing(i, result.units);
        }
    }

    private static void propagateOtherToMissing(int idx, Map<String, DecimalFormat.Unit[]> units) {
        DecimalFormat.Unit otherVariantValue = units.get(OTHER)[idx];
        for (DecimalFormat.Unit[] byBase : units.values()) {
            if (byBase[idx] != null) continue;
            byBase[idx] = otherVariantValue;
        }
    }

    private static void copyFromPreviousIndex(int idx, Map<String, DecimalFormat.Unit[]> units) {
        for (DecimalFormat.Unit[] byBase : units.values()) {
            if (idx == 0) {
                byBase[idx] = DecimalFormat.NULL_UNIT;
                continue;
            }
            byBase[idx] = byBase[idx - 1];
        }
    }

    private static void saveUnit(DecimalFormat.Unit unit, String pluralVariant, int idx, Map<String, DecimalFormat.Unit[]> units) {
        DecimalFormat.Unit[] byBase = units.get(pluralVariant);
        if (byBase == null) {
            byBase = new DecimalFormat.Unit[15];
            units.put(pluralVariant, byBase);
        }
        byBase[idx] = unit;
    }

    static DecimalFormat.Unit getUnit(Map<String, DecimalFormat.Unit[]> units, String variant, int base) {
        DecimalFormat.Unit[] byBase = units.get(variant);
        if (byBase == null) {
            byBase = units.get(OTHER);
        }
        return byBase[base];
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static enum UResFlags {
        ANY,
        NOT_ROOT;

    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static enum QuoteState {
        OUTSIDE,
        INSIDE_EMPTY,
        INSIDE_FULL;

    }

    static class DataBundle {
        Data shortData;
        Data longData;

        DataBundle(Data shortData, Data longData) {
            this.shortData = shortData;
            this.longData = longData;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    static class Data {
        long[] divisors;
        Map<String, DecimalFormat.Unit[]> units;

        Data(long[] divisors, Map<String, DecimalFormat.Unit[]> units) {
            this.divisors = divisors;
            this.units = units;
        }
    }
}

