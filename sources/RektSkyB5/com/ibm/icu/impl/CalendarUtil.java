/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.TreeMap;

public final class CalendarUtil {
    private static final String CALKEY = "calendar";
    private static final String DEFCAL = "gregorian";

    public static String getCalendarType(ULocale loc) {
        String calType = loc.getKeywordValue(CALKEY);
        if (calType != null) {
            return calType.toLowerCase(Locale.ROOT);
        }
        ULocale canonical = ULocale.createCanonical(loc.toString());
        calType = canonical.getKeywordValue(CALKEY);
        if (calType != null) {
            return calType;
        }
        String region = ULocale.getRegionForSupplementalData(canonical, true);
        return CalendarPreferences.INSTANCE.getCalendarTypeForRegion(region);
    }

    private static final class CalendarPreferences
    extends UResource.Sink {
        private static final CalendarPreferences INSTANCE = new CalendarPreferences();
        Map<String, String> prefs = new TreeMap<String, String>();

        CalendarPreferences() {
            try {
                ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "supplementalData");
                rb.getAllItemsWithFallback("calendarPreferenceData", this);
            }
            catch (MissingResourceException missingResourceException) {
                // empty catch block
            }
        }

        String getCalendarTypeForRegion(String region) {
            String type = this.prefs.get(region);
            return type == null ? CalendarUtil.DEFCAL : type;
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table calendarPreferenceData = value.getTable();
            int i2 = 0;
            while (calendarPreferenceData.getKeyAndValue(i2, key, value)) {
                String type;
                UResource.Array types = value.getArray();
                if (types.getValue(0, value) && !(type = value.getString()).equals(CalendarUtil.DEFCAL)) {
                    this.prefs.put(key.toString(), type);
                }
                ++i2;
            }
        }
    }
}

