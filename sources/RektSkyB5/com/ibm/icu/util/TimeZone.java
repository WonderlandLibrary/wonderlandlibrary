/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.Grego;
import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.JavaTimeZone;
import com.ibm.icu.impl.OlsonTimeZone;
import com.ibm.icu.impl.TimeZoneAdapter;
import com.ibm.icu.impl.ZoneMeta;
import com.ibm.icu.text.TimeZoneFormat;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import com.ibm.icu.util.VersionInfo;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.logging.Logger;

public abstract class TimeZone
implements Serializable,
Cloneable,
Freezable<TimeZone> {
    private static final Logger LOGGER = Logger.getLogger("com.ibm.icu.util.TimeZone");
    private static final long serialVersionUID = -744942128318337471L;
    public static final int TIMEZONE_ICU = 0;
    public static final int TIMEZONE_JDK = 1;
    public static final int SHORT = 0;
    public static final int LONG = 1;
    public static final int SHORT_GENERIC = 2;
    public static final int LONG_GENERIC = 3;
    public static final int SHORT_GMT = 4;
    public static final int LONG_GMT = 5;
    public static final int SHORT_COMMONLY_USED = 6;
    public static final int GENERIC_LOCATION = 7;
    public static final String UNKNOWN_ZONE_ID = "Etc/Unknown";
    static final String GMT_ZONE_ID = "Etc/GMT";
    public static final TimeZone UNKNOWN_ZONE = new ConstantZone(0, "Etc/Unknown").freeze();
    public static final TimeZone GMT_ZONE = new ConstantZone(0, "Etc/GMT").freeze();
    private String ID;
    private static volatile TimeZone defaultZone = null;
    private static int TZ_IMPL = 0;
    private static final String TZIMPL_CONFIG_KEY = "com.ibm.icu.util.TimeZone.DefaultTimeZoneType";
    private static final String TZIMPL_CONFIG_ICU = "ICU";
    private static final String TZIMPL_CONFIG_JDK = "JDK";

    public TimeZone() {
    }

    @Deprecated
    protected TimeZone(String ID2) {
        if (ID2 == null) {
            throw new NullPointerException();
        }
        this.ID = ID2;
    }

    public abstract int getOffset(int var1, int var2, int var3, int var4, int var5, int var6);

    public int getOffset(long date) {
        int[] result = new int[2];
        this.getOffset(date, false, result);
        return result[0] + result[1];
    }

    public void getOffset(long date, boolean local, int[] offsets) {
        offsets[0] = this.getRawOffset();
        if (!local) {
            date += (long)offsets[0];
        }
        int[] fields = new int[6];
        int pass = 0;
        while (true) {
            Grego.timeToFields(date, fields);
            offsets[1] = this.getOffset(1, fields[0], fields[1], fields[2], fields[3], fields[5]) - offsets[0];
            if (pass != 0 || !local || offsets[1] == 0) break;
            date -= (long)offsets[1];
            ++pass;
        }
    }

    public abstract void setRawOffset(int var1);

    public abstract int getRawOffset();

    public String getID() {
        return this.ID;
    }

    public void setID(String ID2) {
        if (ID2 == null) {
            throw new NullPointerException();
        }
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify a frozen TimeZone instance.");
        }
        this.ID = ID2;
    }

    public final String getDisplayName() {
        return this._getDisplayName(3, false, ULocale.getDefault(ULocale.Category.DISPLAY));
    }

    public final String getDisplayName(Locale locale) {
        return this._getDisplayName(3, false, ULocale.forLocale(locale));
    }

    public final String getDisplayName(ULocale locale) {
        return this._getDisplayName(3, false, locale);
    }

    public final String getDisplayName(boolean daylight, int style) {
        return this.getDisplayName(daylight, style, ULocale.getDefault(ULocale.Category.DISPLAY));
    }

    public String getDisplayName(boolean daylight, int style, Locale locale) {
        return this.getDisplayName(daylight, style, ULocale.forLocale(locale));
    }

    public String getDisplayName(boolean daylight, int style, ULocale locale) {
        if (style < 0 || style > 7) {
            throw new IllegalArgumentException("Illegal style: " + style);
        }
        return this._getDisplayName(style, daylight, locale);
    }

    private String _getDisplayName(int style, boolean daylight, ULocale locale) {
        if (locale == null) {
            throw new NullPointerException("locale is null");
        }
        String result = null;
        if (style == 7 || style == 3 || style == 2) {
            TimeZoneFormat tzfmt = TimeZoneFormat.getInstance(locale);
            long date = System.currentTimeMillis();
            Output<TimeZoneFormat.TimeType> timeType = new Output<TimeZoneFormat.TimeType>(TimeZoneFormat.TimeType.UNKNOWN);
            switch (style) {
                case 7: {
                    result = tzfmt.format(TimeZoneFormat.Style.GENERIC_LOCATION, this, date, timeType);
                    break;
                }
                case 3: {
                    result = tzfmt.format(TimeZoneFormat.Style.GENERIC_LONG, this, date, timeType);
                    break;
                }
                case 2: {
                    result = tzfmt.format(TimeZoneFormat.Style.GENERIC_SHORT, this, date, timeType);
                }
            }
            if (daylight && timeType.value == TimeZoneFormat.TimeType.STANDARD || !daylight && timeType.value == TimeZoneFormat.TimeType.DAYLIGHT) {
                int offset = daylight ? this.getRawOffset() + this.getDSTSavings() : this.getRawOffset();
                result = style == 2 ? tzfmt.formatOffsetShortLocalizedGMT(offset) : tzfmt.formatOffsetLocalizedGMT(offset);
            }
        } else if (style == 5 || style == 4) {
            TimeZoneFormat tzfmt = TimeZoneFormat.getInstance(locale);
            int offset = daylight && this.useDaylightTime() ? this.getRawOffset() + this.getDSTSavings() : this.getRawOffset();
            switch (style) {
                case 5: {
                    result = tzfmt.formatOffsetLocalizedGMT(offset);
                    break;
                }
                case 4: {
                    result = tzfmt.formatOffsetISO8601Basic(offset, false, false, false);
                }
            }
        } else {
            assert (style == 1 || style == 0 || style == 6);
            long date = System.currentTimeMillis();
            TimeZoneNames tznames = TimeZoneNames.getInstance(locale);
            TimeZoneNames.NameType nameType = null;
            switch (style) {
                case 1: {
                    nameType = daylight ? TimeZoneNames.NameType.LONG_DAYLIGHT : TimeZoneNames.NameType.LONG_STANDARD;
                    break;
                }
                case 0: 
                case 6: {
                    nameType = daylight ? TimeZoneNames.NameType.SHORT_DAYLIGHT : TimeZoneNames.NameType.SHORT_STANDARD;
                }
            }
            result = tznames.getDisplayName(ZoneMeta.getCanonicalCLDRID(this), nameType, date);
            if (result == null) {
                TimeZoneFormat tzfmt = TimeZoneFormat.getInstance(locale);
                int offset = daylight && this.useDaylightTime() ? this.getRawOffset() + this.getDSTSavings() : this.getRawOffset();
                String string = result = style == 1 ? tzfmt.formatOffsetLocalizedGMT(offset) : tzfmt.formatOffsetShortLocalizedGMT(offset);
            }
        }
        assert (result != null);
        return result;
    }

    public int getDSTSavings() {
        if (this.useDaylightTime()) {
            return 3600000;
        }
        return 0;
    }

    public abstract boolean useDaylightTime();

    public boolean observesDaylightTime() {
        return this.useDaylightTime() || this.inDaylightTime(new Date());
    }

    public abstract boolean inDaylightTime(Date var1);

    public static TimeZone getTimeZone(String ID2) {
        return TimeZone.getTimeZone(ID2, TZ_IMPL, false);
    }

    public static TimeZone getFrozenTimeZone(String ID2) {
        return TimeZone.getTimeZone(ID2, TZ_IMPL, true);
    }

    public static TimeZone getTimeZone(String ID2, int type) {
        return TimeZone.getTimeZone(ID2, type, false);
    }

    private static TimeZone getTimeZone(String id, int type, boolean frozen) {
        TimeZone result;
        if (type == 1) {
            result = JavaTimeZone.createTimeZone(id);
            if (result != null) {
                return frozen ? result.freeze() : result;
            }
            result = TimeZone.getFrozenICUTimeZone(id, false);
        } else {
            result = TimeZone.getFrozenICUTimeZone(id, true);
        }
        if (result == null) {
            LOGGER.fine("\"" + id + "\" is a bogus id so timezone is falling back to Etc/Unknown(GMT).");
            result = UNKNOWN_ZONE;
        }
        return frozen ? result : result.cloneAsThawed();
    }

    static BasicTimeZone getFrozenICUTimeZone(String id, boolean trySystem) {
        BasicTimeZone result = null;
        if (trySystem) {
            result = ZoneMeta.getSystemTimeZone(id);
        }
        if (result == null) {
            result = ZoneMeta.getCustomTimeZone(id);
        }
        return result;
    }

    public static synchronized void setDefaultTimeZoneType(int type) {
        if (type != 0 && type != 1) {
            throw new IllegalArgumentException("Invalid timezone type");
        }
        TZ_IMPL = type;
    }

    public static int getDefaultTimeZoneType() {
        return TZ_IMPL;
    }

    public static Set<String> getAvailableIDs(SystemTimeZoneType zoneType, String region, Integer rawOffset) {
        return ZoneMeta.getAvailableIDs(zoneType, region, rawOffset);
    }

    public static String[] getAvailableIDs(int rawOffset) {
        Set<String> ids = TimeZone.getAvailableIDs(SystemTimeZoneType.ANY, null, rawOffset);
        return ids.toArray(new String[0]);
    }

    public static String[] getAvailableIDs(String country) {
        Set<String> ids = TimeZone.getAvailableIDs(SystemTimeZoneType.ANY, country, null);
        return ids.toArray(new String[0]);
    }

    public static String[] getAvailableIDs() {
        Set<String> ids = TimeZone.getAvailableIDs(SystemTimeZoneType.ANY, null, null);
        return ids.toArray(new String[0]);
    }

    public static int countEquivalentIDs(String id) {
        return ZoneMeta.countEquivalentIDs(id);
    }

    public static String getEquivalentID(String id, int index) {
        return ZoneMeta.getEquivalentID(id, index);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static TimeZone getDefault() {
        TimeZone tmpDefaultZone = defaultZone;
        if (tmpDefaultZone != null) return tmpDefaultZone.cloneAsThawed();
        Class<java.util.TimeZone> clazz = java.util.TimeZone.class;
        synchronized (java.util.TimeZone.class) {
            Class<TimeZone> clazz2 = TimeZone.class;
            synchronized (TimeZone.class) {
                tmpDefaultZone = defaultZone;
                if (tmpDefaultZone != null) return tmpDefaultZone.cloneAsThawed();
                if (TZ_IMPL == 1) {
                    tmpDefaultZone = new JavaTimeZone();
                } else {
                    java.util.TimeZone temp = java.util.TimeZone.getDefault();
                    tmpDefaultZone = TimeZone.getFrozenTimeZone(temp.getID());
                }
                defaultZone = tmpDefaultZone;
                // ** MonitorExit[var2_2] (shouldn't be in output)
                return tmpDefaultZone.cloneAsThawed();
            }
        }
    }

    public static synchronized void setDefault(TimeZone tz) {
        TimeZone.setICUDefault(tz);
        if (tz != null) {
            String icuID;
            java.util.TimeZone jdkZone = null;
            if (tz instanceof JavaTimeZone) {
                jdkZone = ((JavaTimeZone)tz).unwrap();
            } else if (tz instanceof OlsonTimeZone && !(icuID = tz.getID()).equals((jdkZone = java.util.TimeZone.getTimeZone(icuID)).getID()) && !(icuID = TimeZone.getCanonicalID(icuID)).equals((jdkZone = java.util.TimeZone.getTimeZone(icuID)).getID())) {
                jdkZone = null;
            }
            if (jdkZone == null) {
                jdkZone = TimeZoneAdapter.wrap(tz);
            }
            java.util.TimeZone.setDefault(jdkZone);
        }
    }

    @Deprecated
    public static synchronized void setICUDefault(TimeZone tz) {
        defaultZone = tz == null ? null : (tz.isFrozen() ? tz : ((TimeZone)tz.clone()).freeze());
    }

    public boolean hasSameRules(TimeZone other) {
        return other != null && this.getRawOffset() == other.getRawOffset() && this.useDaylightTime() == other.useDaylightTime();
    }

    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        return this.ID.equals(((TimeZone)obj).ID);
    }

    public int hashCode() {
        return this.ID.hashCode();
    }

    public static String getTZDataVersion() {
        return VersionInfo.getTZDataVersion();
    }

    public static String getCanonicalID(String id) {
        return TimeZone.getCanonicalID(id, null);
    }

    public static String getCanonicalID(String id, boolean[] isSystemID) {
        String canonicalID = null;
        boolean systemTzid = false;
        if (id != null && id.length() != 0) {
            if (id.equals(UNKNOWN_ZONE_ID)) {
                canonicalID = UNKNOWN_ZONE_ID;
                systemTzid = false;
            } else {
                canonicalID = ZoneMeta.getCanonicalCLDRID(id);
                if (canonicalID != null) {
                    systemTzid = true;
                } else {
                    canonicalID = ZoneMeta.getCustomID(id);
                }
            }
        }
        if (isSystemID != null) {
            isSystemID[0] = systemTzid;
        }
        return canonicalID;
    }

    public static String getRegion(String id) {
        String region = null;
        if (!id.equals(UNKNOWN_ZONE_ID)) {
            region = ZoneMeta.getRegion(id);
        }
        if (region == null) {
            throw new IllegalArgumentException("Unknown system zone id: " + id);
        }
        return region;
    }

    public static String getWindowsID(String id) {
        boolean[] isSystemID = new boolean[]{false};
        id = TimeZone.getCanonicalID(id, isSystemID);
        if (!isSystemID[0]) {
            return null;
        }
        UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "windowsZones", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        UResourceBundle mapTimezones = top.get("mapTimezones");
        UResourceBundleIterator resitr = mapTimezones.getIterator();
        while (resitr.hasNext()) {
            UResourceBundle winzone = resitr.next();
            if (winzone.getType() != 2) continue;
            UResourceBundleIterator rgitr = winzone.getIterator();
            while (rgitr.hasNext()) {
                String[] tzids;
                UResourceBundle regionalData = rgitr.next();
                if (regionalData.getType() != 0) continue;
                for (String tzid : tzids = regionalData.getString().split(" ")) {
                    if (!tzid.equals(id)) continue;
                    return winzone.getKey();
                }
            }
        }
        return null;
    }

    public static String getIDForWindowsID(String winid, String region) {
        String id = null;
        UResourceBundle top = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "windowsZones", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        UResourceBundle mapTimezones = top.get("mapTimezones");
        try {
            UResourceBundle zones = mapTimezones.get(winid);
            if (region != null) {
                try {
                    int endIdx;
                    id = zones.getString(region);
                    if (id != null && (endIdx = id.indexOf(32)) > 0) {
                        id = id.substring(0, endIdx);
                    }
                }
                catch (MissingResourceException missingResourceException) {
                    // empty catch block
                }
            }
            if (id == null) {
                id = zones.getString("001");
            }
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
        return id;
    }

    @Override
    public boolean isFrozen() {
        return false;
    }

    @Override
    public TimeZone freeze() {
        throw new UnsupportedOperationException("Needs to be implemented by the subclass.");
    }

    @Override
    public TimeZone cloneAsThawed() {
        try {
            TimeZone other = (TimeZone)super.clone();
            return other;
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
    }

    static {
        String type = ICUConfig.get(TZIMPL_CONFIG_KEY, TZIMPL_CONFIG_ICU);
        if (type.equalsIgnoreCase(TZIMPL_CONFIG_JDK)) {
            TZ_IMPL = 1;
        }
    }

    private static final class ConstantZone
    extends TimeZone {
        private static final long serialVersionUID = 1L;
        private int rawOffset;
        private volatile transient boolean isFrozen = false;

        private ConstantZone(int rawOffset, String ID2) {
            super(ID2);
            this.rawOffset = rawOffset;
        }

        @Override
        public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
            return this.rawOffset;
        }

        @Override
        public void setRawOffset(int offsetMillis) {
            if (this.isFrozen()) {
                throw new UnsupportedOperationException("Attempt to modify a frozen TimeZone instance.");
            }
            this.rawOffset = offsetMillis;
        }

        @Override
        public int getRawOffset() {
            return this.rawOffset;
        }

        @Override
        public boolean useDaylightTime() {
            return false;
        }

        @Override
        public boolean inDaylightTime(Date date) {
            return false;
        }

        @Override
        public boolean isFrozen() {
            return this.isFrozen;
        }

        @Override
        public TimeZone freeze() {
            this.isFrozen = true;
            return this;
        }

        @Override
        public TimeZone cloneAsThawed() {
            ConstantZone tz = (ConstantZone)super.cloneAsThawed();
            tz.isFrozen = false;
            return tz;
        }
    }

    public static enum SystemTimeZoneType {
        ANY,
        CANONICAL,
        CANONICAL_LOCATION;

    }
}

