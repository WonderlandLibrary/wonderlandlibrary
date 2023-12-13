/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.number.DecimalFormatProperties;
import com.ibm.icu.impl.number.DecimalQuantity;
import com.ibm.icu.impl.number.PatternStringParser;
import com.ibm.icu.number.NumberFormatter;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

public class Grouper {
    private static final Grouper GROUPER_NEVER = new Grouper(-1, -1, -2);
    private static final Grouper GROUPER_MIN2 = new Grouper(-2, -2, -3);
    private static final Grouper GROUPER_AUTO = new Grouper(-2, -2, -2);
    private static final Grouper GROUPER_ON_ALIGNED = new Grouper(-4, -4, 1);
    private static final Grouper GROUPER_WESTERN = new Grouper(3, 3, 1);
    private static final Grouper GROUPER_INDIC = new Grouper(3, 2, 1);
    private static final Grouper GROUPER_WESTERN_MIN2 = new Grouper(3, 3, 2);
    private static final Grouper GROUPER_INDIC_MIN2 = new Grouper(3, 2, 2);
    private final short grouping1;
    private final short grouping2;
    private final short minGrouping;

    public static Grouper forStrategy(NumberFormatter.GroupingStrategy grouping) {
        switch (grouping) {
            case OFF: {
                return GROUPER_NEVER;
            }
            case MIN2: {
                return GROUPER_MIN2;
            }
            case AUTO: {
                return GROUPER_AUTO;
            }
            case ON_ALIGNED: {
                return GROUPER_ON_ALIGNED;
            }
            case THOUSANDS: {
                return GROUPER_WESTERN;
            }
        }
        throw new AssertionError();
    }

    public static Grouper forProperties(DecimalFormatProperties properties) {
        if (!properties.getGroupingUsed()) {
            return GROUPER_NEVER;
        }
        short grouping1 = (short)properties.getGroupingSize();
        short grouping2 = (short)properties.getSecondaryGroupingSize();
        short minGrouping = (short)properties.getMinimumGroupingDigits();
        grouping1 = grouping1 > 0 ? grouping1 : (grouping2 > 0 ? grouping2 : grouping1);
        grouping2 = grouping2 > 0 ? grouping2 : grouping1;
        return Grouper.getInstance(grouping1, grouping2, minGrouping);
    }

    public static Grouper getInstance(short grouping1, short grouping2, short minGrouping) {
        if (grouping1 == -1) {
            return GROUPER_NEVER;
        }
        if (grouping1 == 3 && grouping2 == 3 && minGrouping == 1) {
            return GROUPER_WESTERN;
        }
        if (grouping1 == 3 && grouping2 == 2 && minGrouping == 1) {
            return GROUPER_INDIC;
        }
        if (grouping1 == 3 && grouping2 == 3 && minGrouping == 2) {
            return GROUPER_WESTERN_MIN2;
        }
        if (grouping1 == 3 && grouping2 == 2 && minGrouping == 2) {
            return GROUPER_INDIC_MIN2;
        }
        return new Grouper(grouping1, grouping2, minGrouping);
    }

    private static short getMinGroupingForLocale(ULocale locale) {
        ICUResourceBundle resource = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", locale);
        String result = resource.getStringWithFallback("NumberElements/minimumGroupingDigits");
        return Short.valueOf(result);
    }

    private Grouper(short grouping1, short grouping2, short minGrouping) {
        this.grouping1 = grouping1;
        this.grouping2 = grouping2;
        this.minGrouping = minGrouping;
    }

    public Grouper withLocaleData(ULocale locale, PatternStringParser.ParsedPatternInfo patternInfo) {
        if (this.grouping1 != -2 && this.grouping1 != -4) {
            return this;
        }
        short grouping1 = (short)(patternInfo.positive.groupingSizes & 0xFFFFL);
        short grouping2 = (short)(patternInfo.positive.groupingSizes >>> 16 & 0xFFFFL);
        short grouping3 = (short)(patternInfo.positive.groupingSizes >>> 32 & 0xFFFFL);
        if (grouping2 == -1) {
            short s2 = grouping1 = this.grouping1 == -4 ? (short)3 : -1;
        }
        if (grouping3 == -1) {
            grouping2 = grouping1;
        }
        short minGrouping = this.minGrouping == -2 ? Grouper.getMinGroupingForLocale(locale) : (this.minGrouping == -3 ? (short)Math.max(2, Grouper.getMinGroupingForLocale(locale)) : this.minGrouping);
        return Grouper.getInstance(grouping1, grouping2, minGrouping);
    }

    public boolean groupAtPosition(int position, DecimalQuantity value) {
        assert (this.grouping1 != -2 && this.grouping1 != -4);
        if (this.grouping1 == -1 || this.grouping1 == 0) {
            return false;
        }
        return (position -= this.grouping1) >= 0 && position % this.grouping2 == 0 && value.getUpperDisplayMagnitude() - this.grouping1 + 1 >= this.minGrouping;
    }

    public short getPrimary() {
        return this.grouping1;
    }

    public short getSecondary() {
        return this.grouping2;
    }
}

