/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.TextTrieMap;
import com.ibm.icu.impl.ZoneMeta;
import com.ibm.icu.text.LocaleDisplayNames;
import com.ibm.icu.text.TimeZoneFormat;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.BasicTimeZone;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.TimeZoneTransition;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TimeZoneGenericNames
implements Serializable,
Freezable<TimeZoneGenericNames> {
    private static final long serialVersionUID = 2729910342063468417L;
    private ULocale _locale;
    private TimeZoneNames _tznames;
    private transient boolean _frozen;
    private transient String _region;
    private transient WeakReference<LocaleDisplayNames> _localeDisplayNamesRef;
    private transient MessageFormat[] _patternFormatters;
    private transient ConcurrentHashMap<String, String> _genericLocationNamesMap;
    private transient ConcurrentHashMap<String, String> _genericPartialLocationNamesMap;
    private transient TextTrieMap<NameInfo> _gnamesTrie;
    private transient boolean _gnamesTrieFullyLoaded;
    private static Cache GENERIC_NAMES_CACHE = new Cache();
    private static final long DST_CHECK_RANGE = 15897600000L;
    private static final TimeZoneNames.NameType[] GENERIC_NON_LOCATION_TYPES = new TimeZoneNames.NameType[]{TimeZoneNames.NameType.LONG_GENERIC, TimeZoneNames.NameType.SHORT_GENERIC};

    public TimeZoneGenericNames(ULocale locale, TimeZoneNames tznames) {
        this._locale = locale;
        this._tznames = tznames;
        this.init();
    }

    private void init() {
        if (this._tznames == null) {
            this._tznames = TimeZoneNames.getInstance(this._locale);
        }
        this._genericLocationNamesMap = new ConcurrentHashMap();
        this._genericPartialLocationNamesMap = new ConcurrentHashMap();
        this._gnamesTrie = new TextTrieMap(true);
        this._gnamesTrieFullyLoaded = false;
        TimeZone tz = TimeZone.getDefault();
        String tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
        if (tzCanonicalID != null) {
            this.loadStrings(tzCanonicalID);
        }
    }

    private TimeZoneGenericNames(ULocale locale) {
        this(locale, (TimeZoneNames)null);
    }

    public static TimeZoneGenericNames getInstance(ULocale locale) {
        String key = locale.getBaseName();
        return (TimeZoneGenericNames)GENERIC_NAMES_CACHE.getInstance(key, locale);
    }

    public String getDisplayName(TimeZone tz, GenericNameType type, long date) {
        String name = null;
        String tzCanonicalID = null;
        switch (type) {
            case LOCATION: {
                tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
                if (tzCanonicalID == null) break;
                name = this.getGenericLocationName(tzCanonicalID);
                break;
            }
            case LONG: 
            case SHORT: {
                name = this.formatGenericNonLocationName(tz, type, date);
                if (name != null || (tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz)) == null) break;
                name = this.getGenericLocationName(tzCanonicalID);
            }
        }
        return name;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getGenericLocationName(String canonicalTzID) {
        if (canonicalTzID == null || canonicalTzID.length() == 0) {
            return null;
        }
        String name = this._genericLocationNamesMap.get(canonicalTzID);
        if (name != null) {
            if (name.length() == 0) {
                return null;
            }
            return name;
        }
        Output<Boolean> isPrimary = new Output<Boolean>();
        String countryCode = ZoneMeta.getCanonicalCountry(canonicalTzID, isPrimary);
        if (countryCode != null) {
            if (((Boolean)isPrimary.value).booleanValue()) {
                String country = this.getLocaleDisplayNames().regionDisplayName(countryCode);
                name = this.formatPattern(Pattern.REGION_FORMAT, country);
            } else {
                String city = this._tznames.getExemplarLocationName(canonicalTzID);
                name = this.formatPattern(Pattern.REGION_FORMAT, city);
            }
        }
        if (name == null) {
            this._genericLocationNamesMap.putIfAbsent(canonicalTzID.intern(), "");
        } else {
            TimeZoneGenericNames timeZoneGenericNames = this;
            synchronized (timeZoneGenericNames) {
                canonicalTzID = canonicalTzID.intern();
                String tmp = this._genericLocationNamesMap.putIfAbsent(canonicalTzID, name.intern());
                if (tmp == null) {
                    NameInfo info = new NameInfo();
                    info.tzID = canonicalTzID;
                    info.type = GenericNameType.LOCATION;
                    this._gnamesTrie.put(name, info);
                } else {
                    name = tmp;
                }
            }
        }
        return name;
    }

    public TimeZoneGenericNames setFormatPattern(Pattern patType, String patStr) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
        if (!this._genericLocationNamesMap.isEmpty()) {
            this._genericLocationNamesMap = new ConcurrentHashMap();
        }
        if (!this._genericPartialLocationNamesMap.isEmpty()) {
            this._genericPartialLocationNamesMap = new ConcurrentHashMap();
        }
        this._gnamesTrie = null;
        this._gnamesTrieFullyLoaded = false;
        if (this._patternFormatters == null) {
            this._patternFormatters = new MessageFormat[Pattern.values().length];
        }
        this._patternFormatters[patType.ordinal()] = new MessageFormat(patStr);
        return this;
    }

    private String formatGenericNonLocationName(TimeZone tz, GenericNameType type, long date) {
        assert (type == GenericNameType.LONG || type == GenericNameType.SHORT);
        String tzID = ZoneMeta.getCanonicalCLDRID(tz);
        if (tzID == null) {
            return null;
        }
        TimeZoneNames.NameType nameType = type == GenericNameType.LONG ? TimeZoneNames.NameType.LONG_GENERIC : TimeZoneNames.NameType.SHORT_GENERIC;
        String name = this._tznames.getTimeZoneDisplayName(tzID, nameType);
        if (name != null) {
            return name;
        }
        String mzID = this._tznames.getMetaZoneID(tzID, date);
        if (mzID != null) {
            String mzName;
            TimeZoneNames.NameType stdNameType;
            String stdName;
            boolean useStandard = false;
            int[] offsets = new int[]{0, 0};
            tz.getOffset(date, false, offsets);
            if (offsets[1] == 0) {
                useStandard = true;
                if (tz instanceof BasicTimeZone) {
                    BasicTimeZone btz = (BasicTimeZone)tz;
                    TimeZoneTransition before = btz.getPreviousTransition(date, true);
                    if (before != null && date - before.getTime() < 15897600000L && before.getFrom().getDSTSavings() != 0) {
                        useStandard = false;
                    } else {
                        TimeZoneTransition after = btz.getNextTransition(date, false);
                        if (after != null && after.getTime() - date < 15897600000L && after.getTo().getDSTSavings() != 0) {
                            useStandard = false;
                        }
                    }
                } else {
                    int[] tmpOffsets = new int[2];
                    tz.getOffset(date - 15897600000L, false, tmpOffsets);
                    if (tmpOffsets[1] != 0) {
                        useStandard = false;
                    } else {
                        tz.getOffset(date + 15897600000L, false, tmpOffsets);
                        if (tmpOffsets[1] != 0) {
                            useStandard = false;
                        }
                    }
                }
            }
            if (useStandard && (stdName = this._tznames.getDisplayName(tzID, stdNameType = nameType == TimeZoneNames.NameType.LONG_GENERIC ? TimeZoneNames.NameType.LONG_STANDARD : TimeZoneNames.NameType.SHORT_STANDARD, date)) != null) {
                name = stdName;
                String mzGenericName = this._tznames.getMetaZoneDisplayName(mzID, nameType);
                if (stdName.equalsIgnoreCase(mzGenericName)) {
                    name = null;
                }
            }
            if (name == null && (mzName = this._tznames.getMetaZoneDisplayName(mzID, nameType)) != null) {
                String goldenID = this._tznames.getReferenceZoneID(mzID, this.getTargetRegion());
                if (goldenID != null && !goldenID.equals(tzID)) {
                    TimeZone goldenZone = TimeZone.getFrozenTimeZone(goldenID);
                    int[] offsets1 = new int[]{0, 0};
                    goldenZone.getOffset(date + (long)offsets[0] + (long)offsets[1], true, offsets1);
                    name = offsets[0] != offsets1[0] || offsets[1] != offsets1[1] ? this.getPartialLocationName(tzID, mzID, nameType == TimeZoneNames.NameType.LONG_GENERIC, mzName) : mzName;
                } else {
                    name = mzName;
                }
            }
        }
        return name;
    }

    private synchronized String formatPattern(Pattern pat, String ... args) {
        int idx;
        if (this._patternFormatters == null) {
            this._patternFormatters = new MessageFormat[Pattern.values().length];
        }
        if (this._patternFormatters[idx = pat.ordinal()] == null) {
            String patText;
            try {
                ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/zone", this._locale);
                patText = bundle.getStringWithFallback("zoneStrings/" + pat.key());
            }
            catch (MissingResourceException e) {
                patText = pat.defaultValue();
            }
            this._patternFormatters[idx] = new MessageFormat(patText);
        }
        return this._patternFormatters[idx].format(args);
    }

    private synchronized LocaleDisplayNames getLocaleDisplayNames() {
        LocaleDisplayNames locNames = null;
        if (this._localeDisplayNamesRef != null) {
            locNames = (LocaleDisplayNames)this._localeDisplayNamesRef.get();
        }
        if (locNames == null) {
            locNames = LocaleDisplayNames.getInstance(this._locale);
            this._localeDisplayNamesRef = new WeakReference<LocaleDisplayNames>(locNames);
        }
        return locNames;
    }

    private synchronized void loadStrings(String tzCanonicalID) {
        if (tzCanonicalID == null || tzCanonicalID.length() == 0) {
            return;
        }
        this.getGenericLocationName(tzCanonicalID);
        Set<String> mzIDs = this._tznames.getAvailableMetaZoneIDs(tzCanonicalID);
        for (String mzID : mzIDs) {
            String goldenID = this._tznames.getReferenceZoneID(mzID, this.getTargetRegion());
            if (tzCanonicalID.equals(goldenID)) continue;
            for (TimeZoneNames.NameType genNonLocType : GENERIC_NON_LOCATION_TYPES) {
                String mzGenName = this._tznames.getMetaZoneDisplayName(mzID, genNonLocType);
                if (mzGenName == null) continue;
                this.getPartialLocationName(tzCanonicalID, mzID, genNonLocType == TimeZoneNames.NameType.LONG_GENERIC, mzGenName);
            }
        }
    }

    private synchronized String getTargetRegion() {
        if (this._region == null) {
            this._region = this._locale.getCountry();
            if (this._region.length() == 0) {
                ULocale tmp = ULocale.addLikelySubtags(this._locale);
                this._region = tmp.getCountry();
                if (this._region.length() == 0) {
                    this._region = "001";
                }
            }
        }
        return this._region;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private String getPartialLocationName(String tzID, String mzID, boolean isLong, String mzDisplayName) {
        String letter = isLong ? "L" : "S";
        String key = tzID + "&" + mzID + "#" + letter;
        String name = this._genericPartialLocationNamesMap.get(key);
        if (name != null) {
            return name;
        }
        String location = null;
        String countryCode = ZoneMeta.getCanonicalCountry(tzID);
        if (countryCode != null) {
            String regionalGolden = this._tznames.getReferenceZoneID(mzID, countryCode);
            location = tzID.equals(regionalGolden) ? this.getLocaleDisplayNames().regionDisplayName(countryCode) : this._tznames.getExemplarLocationName(tzID);
        } else {
            location = this._tznames.getExemplarLocationName(tzID);
            if (location == null) {
                location = tzID;
            }
        }
        name = this.formatPattern(Pattern.FALLBACK_FORMAT, location, mzDisplayName);
        TimeZoneGenericNames timeZoneGenericNames = this;
        synchronized (timeZoneGenericNames) {
            String tmp = this._genericPartialLocationNamesMap.putIfAbsent(key.intern(), name.intern());
            if (tmp == null) {
                NameInfo info = new NameInfo();
                info.tzID = tzID.intern();
                info.type = isLong ? GenericNameType.LONG : GenericNameType.SHORT;
                this._gnamesTrie.put(name, info);
            } else {
                name = tmp;
            }
        }
        return name;
    }

    public GenericMatchInfo findBestMatch(String text, int start, EnumSet<GenericNameType> genericTypes) {
        Collection<GenericMatchInfo> localMatches;
        if (text == null || text.length() == 0 || start < 0 || start >= text.length()) {
            throw new IllegalArgumentException("bad input text or range");
        }
        GenericMatchInfo bestMatch = null;
        Collection<TimeZoneNames.MatchInfo> tznamesMatches = this.findTimeZoneNames(text, start, genericTypes);
        if (tznamesMatches != null) {
            TimeZoneNames.MatchInfo longestMatch = null;
            for (TimeZoneNames.MatchInfo matchInfo : tznamesMatches) {
                if (longestMatch != null && matchInfo.matchLength() <= longestMatch.matchLength()) continue;
                longestMatch = matchInfo;
            }
            if (longestMatch != null && (bestMatch = this.createGenericMatchInfo(longestMatch)).matchLength() == text.length() - start && bestMatch.timeType != TimeZoneFormat.TimeType.STANDARD) {
                return bestMatch;
            }
        }
        if ((localMatches = this.findLocal(text, start, genericTypes)) != null) {
            for (GenericMatchInfo genericMatchInfo : localMatches) {
                if (bestMatch != null && genericMatchInfo.matchLength() < bestMatch.matchLength()) continue;
                bestMatch = genericMatchInfo;
            }
        }
        return bestMatch;
    }

    public Collection<GenericMatchInfo> find(String text, int start, EnumSet<GenericNameType> genericTypes) {
        if (text == null || text.length() == 0 || start < 0 || start >= text.length()) {
            throw new IllegalArgumentException("bad input text or range");
        }
        Collection<GenericMatchInfo> results = this.findLocal(text, start, genericTypes);
        Collection<TimeZoneNames.MatchInfo> tznamesMatches = this.findTimeZoneNames(text, start, genericTypes);
        if (tznamesMatches != null) {
            for (TimeZoneNames.MatchInfo match : tznamesMatches) {
                if (results == null) {
                    results = new LinkedList<GenericMatchInfo>();
                }
                results.add(this.createGenericMatchInfo(match));
            }
        }
        return results;
    }

    private GenericMatchInfo createGenericMatchInfo(TimeZoneNames.MatchInfo matchInfo) {
        GenericNameType nameType = null;
        TimeZoneFormat.TimeType timeType = TimeZoneFormat.TimeType.UNKNOWN;
        switch (matchInfo.nameType()) {
            case LONG_STANDARD: {
                nameType = GenericNameType.LONG;
                timeType = TimeZoneFormat.TimeType.STANDARD;
                break;
            }
            case LONG_GENERIC: {
                nameType = GenericNameType.LONG;
                break;
            }
            case SHORT_STANDARD: {
                nameType = GenericNameType.SHORT;
                timeType = TimeZoneFormat.TimeType.STANDARD;
                break;
            }
            case SHORT_GENERIC: {
                nameType = GenericNameType.SHORT;
            }
        }
        assert (nameType != null);
        String tzID = matchInfo.tzID();
        if (tzID == null) {
            String mzID = matchInfo.mzID();
            assert (mzID != null);
            tzID = this._tznames.getReferenceZoneID(mzID, this.getTargetRegion());
        }
        assert (tzID != null);
        GenericMatchInfo gmatch = new GenericMatchInfo();
        gmatch.nameType = nameType;
        gmatch.tzID = tzID;
        gmatch.matchLength = matchInfo.matchLength();
        gmatch.timeType = timeType;
        return gmatch;
    }

    private Collection<TimeZoneNames.MatchInfo> findTimeZoneNames(String text, int start, EnumSet<GenericNameType> types) {
        Collection<TimeZoneNames.MatchInfo> tznamesMatches = null;
        EnumSet<TimeZoneNames.NameType> nameTypes = EnumSet.noneOf(TimeZoneNames.NameType.class);
        if (types.contains((Object)GenericNameType.LONG)) {
            nameTypes.add(TimeZoneNames.NameType.LONG_GENERIC);
            nameTypes.add(TimeZoneNames.NameType.LONG_STANDARD);
        }
        if (types.contains((Object)GenericNameType.SHORT)) {
            nameTypes.add(TimeZoneNames.NameType.SHORT_GENERIC);
            nameTypes.add(TimeZoneNames.NameType.SHORT_STANDARD);
        }
        if (!nameTypes.isEmpty()) {
            tznamesMatches = this._tznames.find(text, start, nameTypes);
        }
        return tznamesMatches;
    }

    private synchronized Collection<GenericMatchInfo> findLocal(String text, int start, EnumSet<GenericNameType> types) {
        GenericNameSearchHandler handler = new GenericNameSearchHandler(types);
        this._gnamesTrie.find(text, start, (TextTrieMap.ResultHandler<NameInfo>)handler);
        if (handler.getMaxMatchLen() == text.length() - start || this._gnamesTrieFullyLoaded) {
            return handler.getMatches();
        }
        Set<String> tzIDs = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null);
        for (String tzID : tzIDs) {
            this.loadStrings(tzID);
        }
        this._gnamesTrieFullyLoaded = true;
        handler.resetResults();
        this._gnamesTrie.find(text, start, (TextTrieMap.ResultHandler<NameInfo>)handler);
        return handler.getMatches();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.init();
    }

    @Override
    public boolean isFrozen() {
        return this._frozen;
    }

    @Override
    public TimeZoneGenericNames freeze() {
        this._frozen = true;
        return this;
    }

    @Override
    public TimeZoneGenericNames cloneAsThawed() {
        TimeZoneGenericNames copy = null;
        try {
            copy = (TimeZoneGenericNames)super.clone();
            copy._frozen = false;
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        return copy;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class Cache
    extends SoftCache<String, TimeZoneGenericNames, ULocale> {
        private Cache() {
        }

        @Override
        protected TimeZoneGenericNames createInstance(String key, ULocale data) {
            return new TimeZoneGenericNames(data).freeze();
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class GenericNameSearchHandler
    implements TextTrieMap.ResultHandler<NameInfo> {
        private EnumSet<GenericNameType> _types;
        private Collection<GenericMatchInfo> _matches;
        private int _maxMatchLen;

        GenericNameSearchHandler(EnumSet<GenericNameType> types) {
            this._types = types;
        }

        @Override
        public boolean handlePrefixMatch(int matchLength, Iterator<NameInfo> values) {
            while (values.hasNext()) {
                NameInfo info = values.next();
                if (this._types != null && !this._types.contains((Object)info.type)) continue;
                GenericMatchInfo matchInfo = new GenericMatchInfo();
                matchInfo.tzID = info.tzID;
                matchInfo.nameType = info.type;
                matchInfo.matchLength = matchLength;
                if (this._matches == null) {
                    this._matches = new LinkedList<GenericMatchInfo>();
                }
                this._matches.add(matchInfo);
                if (matchLength <= this._maxMatchLen) continue;
                this._maxMatchLen = matchLength;
            }
            return true;
        }

        public Collection<GenericMatchInfo> getMatches() {
            return this._matches;
        }

        public int getMaxMatchLen() {
            return this._maxMatchLen;
        }

        public void resetResults() {
            this._matches = null;
            this._maxMatchLen = 0;
        }
    }

    public static class GenericMatchInfo {
        GenericNameType nameType;
        String tzID;
        int matchLength;
        TimeZoneFormat.TimeType timeType = TimeZoneFormat.TimeType.UNKNOWN;

        public GenericNameType nameType() {
            return this.nameType;
        }

        public String tzID() {
            return this.tzID;
        }

        public TimeZoneFormat.TimeType timeType() {
            return this.timeType;
        }

        public int matchLength() {
            return this.matchLength;
        }
    }

    private static class NameInfo {
        String tzID;
        GenericNameType type;

        private NameInfo() {
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum Pattern {
        REGION_FORMAT("regionFormat", "({0})"),
        FALLBACK_FORMAT("fallbackFormat", "{1} ({0})");

        String _key;
        String _defaultVal;

        private Pattern(String key, String defaultVal) {
            this._key = key;
            this._defaultVal = defaultVal;
        }

        String key() {
            return this._key;
        }

        String defaultValue() {
            return this._defaultVal;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum GenericNameType {
        LOCATION("LONG", "SHORT"),
        LONG(new String[0]),
        SHORT(new String[0]);

        String[] _fallbackTypeOf;

        private GenericNameType(String ... fallbackTypeOf) {
            this._fallbackTypeOf = fallbackTypeOf;
        }

        public boolean isFallbackTypeOf(GenericNameType type) {
            String typeStr = type.toString();
            for (String t : this._fallbackTypeOf) {
                if (!t.equals(typeStr)) continue;
                return true;
            }
            return false;
        }
    }
}

