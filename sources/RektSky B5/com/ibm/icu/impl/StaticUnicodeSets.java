/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.impl.number.parse.ParsingUtils;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.EnumMap;
import java.util.Map;

public class StaticUnicodeSets {
    private static final Map<Key, UnicodeSet> unicodeSets = new EnumMap<Key, UnicodeSet>(Key.class);

    public static UnicodeSet get(Key key) {
        UnicodeSet candidate = unicodeSets.get((Object)key);
        if (candidate == null) {
            return UnicodeSet.EMPTY;
        }
        return candidate;
    }

    public static Key chooseFrom(String str, Key key1) {
        return ParsingUtils.safeContains(StaticUnicodeSets.get(key1), str) ? key1 : null;
    }

    public static Key chooseFrom(String str, Key key1, Key key2) {
        return ParsingUtils.safeContains(StaticUnicodeSets.get(key1), str) ? key1 : StaticUnicodeSets.chooseFrom(str, key2);
    }

    public static Key chooseCurrency(String str) {
        if (StaticUnicodeSets.get(Key.DOLLAR_SIGN).contains(str)) {
            return Key.DOLLAR_SIGN;
        }
        if (StaticUnicodeSets.get(Key.POUND_SIGN).contains(str)) {
            return Key.POUND_SIGN;
        }
        if (StaticUnicodeSets.get(Key.RUPEE_SIGN).contains(str)) {
            return Key.RUPEE_SIGN;
        }
        if (StaticUnicodeSets.get(Key.YEN_SIGN).contains(str)) {
            return Key.YEN_SIGN;
        }
        return null;
    }

    private static UnicodeSet computeUnion(Key k1, Key k2) {
        return new UnicodeSet().addAll(StaticUnicodeSets.get(k1)).addAll(StaticUnicodeSets.get(k2)).freeze();
    }

    private static UnicodeSet computeUnion(Key k1, Key k2, Key k3) {
        return new UnicodeSet().addAll(StaticUnicodeSets.get(k1)).addAll(StaticUnicodeSets.get(k2)).addAll(StaticUnicodeSets.get(k3)).freeze();
    }

    private static void saveSet(Key key, String unicodeSetPattern) {
        assert (unicodeSets.get((Object)key) == null);
        unicodeSets.put(key, new UnicodeSet(unicodeSetPattern).freeze());
    }

    static {
        unicodeSets.put(Key.DEFAULT_IGNORABLES, new UnicodeSet("[[:Zs:][\\u0009][:Bidi_Control:][:Variation_Selector:]]").freeze());
        unicodeSets.put(Key.STRICT_IGNORABLES, new UnicodeSet("[[:Bidi_Control:]]").freeze());
        ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", ULocale.ROOT);
        rb.getAllItemsWithFallback("parse", new ParseDataSink());
        assert (unicodeSets.containsKey((Object)Key.COMMA));
        assert (unicodeSets.containsKey((Object)Key.STRICT_COMMA));
        assert (unicodeSets.containsKey((Object)Key.PERIOD));
        assert (unicodeSets.containsKey((Object)Key.STRICT_PERIOD));
        unicodeSets.put(Key.OTHER_GROUPING_SEPARATORS, new UnicodeSet("['\u066c\u2018\u2019\uff07\\u0020\\u00A0\\u2000-\\u200A\\u202F\\u205F\\u3000]").freeze());
        unicodeSets.put(Key.ALL_SEPARATORS, StaticUnicodeSets.computeUnion(Key.COMMA, Key.PERIOD, Key.OTHER_GROUPING_SEPARATORS));
        unicodeSets.put(Key.STRICT_ALL_SEPARATORS, StaticUnicodeSets.computeUnion(Key.STRICT_COMMA, Key.STRICT_PERIOD, Key.OTHER_GROUPING_SEPARATORS));
        assert (unicodeSets.containsKey((Object)Key.MINUS_SIGN));
        assert (unicodeSets.containsKey((Object)Key.PLUS_SIGN));
        unicodeSets.put(Key.PERCENT_SIGN, new UnicodeSet("[%\u066a]").freeze());
        unicodeSets.put(Key.PERMILLE_SIGN, new UnicodeSet("[\u2030\u0609]").freeze());
        unicodeSets.put(Key.INFINITY, new UnicodeSet("[\u221e]").freeze());
        assert (unicodeSets.containsKey((Object)Key.DOLLAR_SIGN));
        assert (unicodeSets.containsKey((Object)Key.POUND_SIGN));
        assert (unicodeSets.containsKey((Object)Key.RUPEE_SIGN));
        unicodeSets.put(Key.YEN_SIGN, new UnicodeSet("[\u00a5\\uffe5]").freeze());
        unicodeSets.put(Key.DIGITS, new UnicodeSet("[:digit:]").freeze());
        unicodeSets.put(Key.DIGITS_OR_ALL_SEPARATORS, StaticUnicodeSets.computeUnion(Key.DIGITS, Key.ALL_SEPARATORS));
        unicodeSets.put(Key.DIGITS_OR_STRICT_ALL_SEPARATORS, StaticUnicodeSets.computeUnion(Key.DIGITS, Key.STRICT_ALL_SEPARATORS));
    }

    static class ParseDataSink
    extends UResource.Sink {
        ParseDataSink() {
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table contextsTable = value.getTable();
            int i2 = 0;
            while (contextsTable.getKeyAndValue(i2, key, value)) {
                if (!key.contentEquals("date")) {
                    assert (key.contentEquals("general") || key.contentEquals("number"));
                    UResource.Table strictnessTable = value.getTable();
                    int j2 = 0;
                    while (strictnessTable.getKeyAndValue(j2, key, value)) {
                        boolean isLenient = key.contentEquals("lenient");
                        UResource.Array array = value.getArray();
                        for (int k2 = 0; k2 < array.getSize(); ++k2) {
                            array.getValue(k2, value);
                            String str = value.toString();
                            if (str.indexOf(46) != -1) {
                                StaticUnicodeSets.saveSet(isLenient ? Key.PERIOD : Key.STRICT_PERIOD, str);
                                continue;
                            }
                            if (str.indexOf(44) != -1) {
                                StaticUnicodeSets.saveSet(isLenient ? Key.COMMA : Key.STRICT_COMMA, str);
                                continue;
                            }
                            if (str.indexOf(43) != -1) {
                                StaticUnicodeSets.saveSet(Key.PLUS_SIGN, str);
                                continue;
                            }
                            if (str.indexOf(8210) != -1) {
                                StaticUnicodeSets.saveSet(Key.MINUS_SIGN, str);
                                continue;
                            }
                            if (str.indexOf(36) != -1) {
                                StaticUnicodeSets.saveSet(Key.DOLLAR_SIGN, str);
                                continue;
                            }
                            if (str.indexOf(163) != -1) {
                                StaticUnicodeSets.saveSet(Key.POUND_SIGN, str);
                                continue;
                            }
                            if (str.indexOf(8360) == -1) continue;
                            StaticUnicodeSets.saveSet(Key.RUPEE_SIGN, str);
                        }
                        ++j2;
                    }
                }
                ++i2;
            }
        }
    }

    public static enum Key {
        DEFAULT_IGNORABLES,
        STRICT_IGNORABLES,
        COMMA,
        PERIOD,
        STRICT_COMMA,
        STRICT_PERIOD,
        OTHER_GROUPING_SEPARATORS,
        ALL_SEPARATORS,
        STRICT_ALL_SEPARATORS,
        MINUS_SIGN,
        PLUS_SIGN,
        PERCENT_SIGN,
        PERMILLE_SIGN,
        INFINITY,
        DOLLAR_SIGN,
        POUND_SIGN,
        RUPEE_SIGN,
        YEN_SIGN,
        DIGITS,
        DIGITS_OR_ALL_SEPARATORS,
        DIGITS_OR_STRICT_ALL_SEPARATORS;

    }
}

