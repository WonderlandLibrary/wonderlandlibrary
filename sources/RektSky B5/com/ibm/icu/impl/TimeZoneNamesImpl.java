/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.Grego;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SoftCache;
import com.ibm.icu.impl.TextTrieMap;
import com.ibm.icu.impl.UResource;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.ZoneMeta;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class TimeZoneNamesImpl
extends TimeZoneNames {
    private static final long serialVersionUID = -2179814848495897472L;
    private static final String ZONE_STRINGS_BUNDLE = "zoneStrings";
    private static final String MZ_PREFIX = "meta:";
    private static volatile Set<String> METAZONE_IDS;
    private static final TZ2MZsCache TZ_TO_MZS_CACHE;
    private static final MZ2TZsCache MZ_TO_TZS_CACHE;
    private transient ICUResourceBundle _zoneStrings;
    private transient ConcurrentHashMap<String, ZNames> _mzNamesMap;
    private transient ConcurrentHashMap<String, ZNames> _tzNamesMap;
    private transient boolean _namesFullyLoaded;
    private transient TextTrieMap<NameInfo> _namesTrie;
    private transient boolean _namesTrieFullyLoaded;
    private static final Pattern LOC_EXCLUSION_PATTERN;

    public TimeZoneNamesImpl(ULocale locale) {
        this.initialize(locale);
    }

    @Override
    public Set<String> getAvailableMetaZoneIDs() {
        return TimeZoneNamesImpl._getAvailableMetaZoneIDs();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static Set<String> _getAvailableMetaZoneIDs() {
        if (METAZONE_IDS != null) return METAZONE_IDS;
        Class<TimeZoneNamesImpl> clazz = TimeZoneNamesImpl.class;
        synchronized (TimeZoneNamesImpl.class) {
            if (METAZONE_IDS != null) return METAZONE_IDS;
            UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "metaZones");
            UResourceBundle mapTimezones = bundle.get("mapTimezones");
            Set<String> keys = mapTimezones.keySet();
            METAZONE_IDS = Collections.unmodifiableSet(keys);
            // ** MonitorExit[var0] (shouldn't be in output)
            return METAZONE_IDS;
        }
    }

    @Override
    public Set<String> getAvailableMetaZoneIDs(String tzID) {
        return TimeZoneNamesImpl._getAvailableMetaZoneIDs(tzID);
    }

    static Set<String> _getAvailableMetaZoneIDs(String tzID) {
        if (tzID == null || tzID.length() == 0) {
            return Collections.emptySet();
        }
        List maps = (List)TZ_TO_MZS_CACHE.getInstance(tzID, tzID);
        if (maps.isEmpty()) {
            return Collections.emptySet();
        }
        HashSet<String> mzIDs = new HashSet<String>(maps.size());
        for (MZMapEntry map : maps) {
            mzIDs.add(map.mzID());
        }
        return Collections.unmodifiableSet(mzIDs);
    }

    @Override
    public String getMetaZoneID(String tzID, long date) {
        return TimeZoneNamesImpl._getMetaZoneID(tzID, date);
    }

    static String _getMetaZoneID(String tzID, long date) {
        if (tzID == null || tzID.length() == 0) {
            return null;
        }
        String mzID = null;
        List maps = (List)TZ_TO_MZS_CACHE.getInstance(tzID, tzID);
        for (MZMapEntry map : maps) {
            if (date < map.from() || date >= map.to()) continue;
            mzID = map.mzID();
            break;
        }
        return mzID;
    }

    @Override
    public String getReferenceZoneID(String mzID, String region) {
        return TimeZoneNamesImpl._getReferenceZoneID(mzID, region);
    }

    static String _getReferenceZoneID(String mzID, String region) {
        if (mzID == null || mzID.length() == 0) {
            return null;
        }
        String refID = null;
        Map regionTzMap = (Map)MZ_TO_TZS_CACHE.getInstance(mzID, mzID);
        if (!regionTzMap.isEmpty() && (refID = (String)regionTzMap.get(region)) == null) {
            refID = (String)regionTzMap.get("001");
        }
        return refID;
    }

    @Override
    public String getMetaZoneDisplayName(String mzID, TimeZoneNames.NameType type) {
        if (mzID == null || mzID.length() == 0) {
            return null;
        }
        return this.loadMetaZoneNames(mzID).getName(type);
    }

    @Override
    public String getTimeZoneDisplayName(String tzID, TimeZoneNames.NameType type) {
        if (tzID == null || tzID.length() == 0) {
            return null;
        }
        return this.loadTimeZoneNames(tzID).getName(type);
    }

    @Override
    public String getExemplarLocationName(String tzID) {
        if (tzID == null || tzID.length() == 0) {
            return null;
        }
        String locName = this.loadTimeZoneNames(tzID).getName(TimeZoneNames.NameType.EXEMPLAR_LOCATION);
        return locName;
    }

    @Override
    public synchronized Collection<TimeZoneNames.MatchInfo> find(CharSequence text, int start, EnumSet<TimeZoneNames.NameType> nameTypes) {
        if (text == null || text.length() == 0 || start < 0 || start >= text.length()) {
            throw new IllegalArgumentException("bad input text or range");
        }
        NameSearchHandler handler = new NameSearchHandler(nameTypes);
        Collection<TimeZoneNames.MatchInfo> matches = this.doFind(handler, text, start);
        if (matches != null) {
            return matches;
        }
        this.addAllNamesIntoTrie();
        matches = this.doFind(handler, text, start);
        if (matches != null) {
            return matches;
        }
        this.internalLoadAllDisplayNames();
        Set<String> tzIDs = TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.CANONICAL, null, null);
        for (String tzID : tzIDs) {
            if (this._tzNamesMap.containsKey(tzID)) continue;
            ZNames.createTimeZoneAndPutInCache(this._tzNamesMap, null, tzID);
        }
        this.addAllNamesIntoTrie();
        this._namesTrieFullyLoaded = true;
        return this.doFind(handler, text, start);
    }

    private Collection<TimeZoneNames.MatchInfo> doFind(NameSearchHandler handler, CharSequence text, int start) {
        handler.resetResults();
        this._namesTrie.find(text, start, handler);
        if (handler.getMaxMatchLen() == text.length() - start || this._namesTrieFullyLoaded) {
            return handler.getMatches();
        }
        return null;
    }

    @Override
    public synchronized void loadAllDisplayNames() {
        this.internalLoadAllDisplayNames();
    }

    @Override
    public void getDisplayNames(String tzID, TimeZoneNames.NameType[] types, long date, String[] dest, int destOffset) {
        if (tzID == null || tzID.length() == 0) {
            return;
        }
        ZNames tzNames = this.loadTimeZoneNames(tzID);
        ZNames mzNames = null;
        for (int i2 = 0; i2 < types.length; ++i2) {
            TimeZoneNames.NameType type = types[i2];
            String name = tzNames.getName(type);
            if (name == null) {
                if (mzNames == null) {
                    String mzID = this.getMetaZoneID(tzID, date);
                    mzNames = mzID == null || mzID.length() == 0 ? ZNames.EMPTY_ZNAMES : this.loadMetaZoneNames(mzID);
                }
                name = mzNames.getName(type);
            }
            dest[destOffset + i2] = name;
        }
    }

    private void internalLoadAllDisplayNames() {
        if (!this._namesFullyLoaded) {
            this._namesFullyLoaded = true;
            new ZoneStringsLoader().load();
        }
    }

    private void addAllNamesIntoTrie() {
        for (Map.Entry<String, ZNames> entry : this._tzNamesMap.entrySet()) {
            entry.getValue().addAsTimeZoneIntoTrie(entry.getKey(), this._namesTrie);
        }
        for (Map.Entry<String, ZNames> entry : this._mzNamesMap.entrySet()) {
            entry.getValue().addAsMetaZoneIntoTrie(entry.getKey(), this._namesTrie);
        }
    }

    private void initialize(ULocale locale) {
        ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/zone", locale);
        this._zoneStrings = (ICUResourceBundle)bundle.get(ZONE_STRINGS_BUNDLE);
        this._tzNamesMap = new ConcurrentHashMap();
        this._mzNamesMap = new ConcurrentHashMap();
        this._namesFullyLoaded = false;
        this._namesTrie = new TextTrieMap(true);
        this._namesTrieFullyLoaded = false;
        TimeZone tz = TimeZone.getDefault();
        String tzCanonicalID = ZoneMeta.getCanonicalCLDRID(tz);
        if (tzCanonicalID != null) {
            this.loadStrings(tzCanonicalID);
        }
    }

    private synchronized void loadStrings(String tzCanonicalID) {
        if (tzCanonicalID == null || tzCanonicalID.length() == 0) {
            return;
        }
        this.loadTimeZoneNames(tzCanonicalID);
        Set<String> mzIDs = this.getAvailableMetaZoneIDs(tzCanonicalID);
        for (String mzID : mzIDs) {
            this.loadMetaZoneNames(mzID);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        ULocale locale = this._zoneStrings.getULocale();
        out.writeObject(locale);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ULocale locale = (ULocale)in.readObject();
        this.initialize(locale);
    }

    private synchronized ZNames loadMetaZoneNames(String mzID) {
        ZNames mznames = this._mzNamesMap.get(mzID);
        if (mznames == null) {
            ZNamesLoader loader = new ZNamesLoader();
            loader.loadMetaZone(this._zoneStrings, mzID);
            mznames = ZNames.createMetaZoneAndPutInCache(this._mzNamesMap, loader.getNames(), mzID);
        }
        return mznames;
    }

    private synchronized ZNames loadTimeZoneNames(String tzID) {
        ZNames tznames = this._tzNamesMap.get(tzID);
        if (tznames == null) {
            ZNamesLoader loader = new ZNamesLoader();
            loader.loadTimeZone(this._zoneStrings, tzID);
            tznames = ZNames.createTimeZoneAndPutInCache(this._tzNamesMap, loader.getNames(), tzID);
        }
        return tznames;
    }

    public static String getDefaultExemplarLocationName(String tzID) {
        if (tzID == null || tzID.length() == 0 || LOC_EXCLUSION_PATTERN.matcher(tzID).matches()) {
            return null;
        }
        String location = null;
        int sep = tzID.lastIndexOf(47);
        if (sep > 0 && sep + 1 < tzID.length()) {
            location = tzID.substring(sep + 1).replace('_', ' ');
        }
        return location;
    }

    static {
        TZ_TO_MZS_CACHE = new TZ2MZsCache();
        MZ_TO_TZS_CACHE = new MZ2TZsCache();
        LOC_EXCLUSION_PATTERN = Pattern.compile("Etc/.*|SystemV/.*|.*/Riyadh8[7-9]");
    }

    private static class MZ2TZsCache
    extends SoftCache<String, Map<String, String>, String> {
        private MZ2TZsCache() {
        }

        @Override
        protected Map<String, String> createInstance(String key, String data) {
            Map<String, String> map = null;
            UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "metaZones");
            UResourceBundle mapTimezones = bundle.get("mapTimezones");
            try {
                UResourceBundle regionMap = mapTimezones.get(key);
                Set<String> regions = regionMap.keySet();
                map = new HashMap<String, String>(regions.size());
                for (String region : regions) {
                    String tzID = regionMap.getString(region).intern();
                    map.put(region.intern(), tzID);
                }
            }
            catch (MissingResourceException e2) {
                map = Collections.emptyMap();
            }
            return map;
        }
    }

    private static class TZ2MZsCache
    extends SoftCache<String, List<MZMapEntry>, String> {
        private TZ2MZsCache() {
        }

        @Override
        protected List<MZMapEntry> createInstance(String key, String data) {
            List<MZMapEntry> mzMaps = null;
            UResourceBundle bundle = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "metaZones");
            UResourceBundle metazoneInfoBundle = bundle.get("metazoneInfo");
            String tzkey = data.replace('/', ':');
            try {
                UResourceBundle zoneBundle = metazoneInfoBundle.get(tzkey);
                mzMaps = new ArrayList<MZMapEntry>(zoneBundle.getSize());
                for (int idx = 0; idx < zoneBundle.getSize(); ++idx) {
                    UResourceBundle mz = zoneBundle.get(idx);
                    String mzid = mz.getString(0);
                    String fromStr = "1970-01-01 00:00";
                    String toStr = "9999-12-31 23:59";
                    if (mz.getSize() == 3) {
                        fromStr = mz.getString(1);
                        toStr = mz.getString(2);
                    }
                    long from = TZ2MZsCache.parseDate(fromStr);
                    long to = TZ2MZsCache.parseDate(toStr);
                    mzMaps.add(new MZMapEntry(mzid, from, to));
                }
            }
            catch (MissingResourceException mre) {
                mzMaps = Collections.emptyList();
            }
            return mzMaps;
        }

        private static long parseDate(String text) {
            int n2;
            int idx;
            int year = 0;
            int month = 0;
            int day = 0;
            int hour = 0;
            int min = 0;
            for (idx = 0; idx <= 3; ++idx) {
                n2 = text.charAt(idx) - 48;
                if (n2 < 0 || n2 >= 10) {
                    throw new IllegalArgumentException("Bad year");
                }
                year = 10 * year + n2;
            }
            for (idx = 5; idx <= 6; ++idx) {
                n2 = text.charAt(idx) - 48;
                if (n2 < 0 || n2 >= 10) {
                    throw new IllegalArgumentException("Bad month");
                }
                month = 10 * month + n2;
            }
            for (idx = 8; idx <= 9; ++idx) {
                n2 = text.charAt(idx) - 48;
                if (n2 < 0 || n2 >= 10) {
                    throw new IllegalArgumentException("Bad day");
                }
                day = 10 * day + n2;
            }
            for (idx = 11; idx <= 12; ++idx) {
                n2 = text.charAt(idx) - 48;
                if (n2 < 0 || n2 >= 10) {
                    throw new IllegalArgumentException("Bad hour");
                }
                hour = 10 * hour + n2;
            }
            for (idx = 14; idx <= 15; ++idx) {
                n2 = text.charAt(idx) - 48;
                if (n2 < 0 || n2 >= 10) {
                    throw new IllegalArgumentException("Bad minute");
                }
                min = 10 * min + n2;
            }
            long date = Grego.fieldsToDay(year, month - 1, day) * 86400000L + (long)hour * 3600000L + (long)min * 60000L;
            return date;
        }
    }

    private static class MZMapEntry {
        private String _mzID;
        private long _from;
        private long _to;

        MZMapEntry(String mzID, long from, long to) {
            this._mzID = mzID;
            this._from = from;
            this._to = to;
        }

        String mzID() {
            return this._mzID;
        }

        long from() {
            return this._from;
        }

        long to() {
            return this._to;
        }
    }

    private static class ZNames {
        public static final int NUM_NAME_TYPES = 7;
        static final ZNames EMPTY_ZNAMES = new ZNames(null);
        private static final int EX_LOC_INDEX = NameTypeIndex.EXEMPLAR_LOCATION.ordinal();
        private String[] _names;
        private boolean didAddIntoTrie;

        private static int getNameTypeIndex(TimeZoneNames.NameType type) {
            switch (type) {
                case EXEMPLAR_LOCATION: {
                    return NameTypeIndex.EXEMPLAR_LOCATION.ordinal();
                }
                case LONG_GENERIC: {
                    return NameTypeIndex.LONG_GENERIC.ordinal();
                }
                case LONG_STANDARD: {
                    return NameTypeIndex.LONG_STANDARD.ordinal();
                }
                case LONG_DAYLIGHT: {
                    return NameTypeIndex.LONG_DAYLIGHT.ordinal();
                }
                case SHORT_GENERIC: {
                    return NameTypeIndex.SHORT_GENERIC.ordinal();
                }
                case SHORT_STANDARD: {
                    return NameTypeIndex.SHORT_STANDARD.ordinal();
                }
                case SHORT_DAYLIGHT: {
                    return NameTypeIndex.SHORT_DAYLIGHT.ordinal();
                }
            }
            throw new AssertionError((Object)("No NameTypeIndex match for " + (Object)((Object)type)));
        }

        private static TimeZoneNames.NameType getNameType(int index) {
            switch (NameTypeIndex.values[index]) {
                case EXEMPLAR_LOCATION: {
                    return TimeZoneNames.NameType.EXEMPLAR_LOCATION;
                }
                case LONG_GENERIC: {
                    return TimeZoneNames.NameType.LONG_GENERIC;
                }
                case LONG_STANDARD: {
                    return TimeZoneNames.NameType.LONG_STANDARD;
                }
                case LONG_DAYLIGHT: {
                    return TimeZoneNames.NameType.LONG_DAYLIGHT;
                }
                case SHORT_GENERIC: {
                    return TimeZoneNames.NameType.SHORT_GENERIC;
                }
                case SHORT_STANDARD: {
                    return TimeZoneNames.NameType.SHORT_STANDARD;
                }
                case SHORT_DAYLIGHT: {
                    return TimeZoneNames.NameType.SHORT_DAYLIGHT;
                }
            }
            throw new AssertionError((Object)("No NameType match for " + index));
        }

        protected ZNames(String[] names) {
            this._names = names;
            this.didAddIntoTrie = names == null;
        }

        public static ZNames createMetaZoneAndPutInCache(Map<String, ZNames> cache, String[] names, String mzID) {
            String key = mzID.intern();
            ZNames value = names == null ? EMPTY_ZNAMES : new ZNames(names);
            cache.put(key, value);
            return value;
        }

        public static ZNames createTimeZoneAndPutInCache(Map<String, ZNames> cache, String[] names, String tzID) {
            String[] stringArray = names = names == null ? new String[EX_LOC_INDEX + 1] : names;
            if (names[EX_LOC_INDEX] == null) {
                names[ZNames.EX_LOC_INDEX] = TimeZoneNamesImpl.getDefaultExemplarLocationName(tzID);
            }
            String key = tzID.intern();
            ZNames value = new ZNames(names);
            cache.put(key, value);
            return value;
        }

        public String getName(TimeZoneNames.NameType type) {
            int index = ZNames.getNameTypeIndex(type);
            if (this._names != null && index < this._names.length) {
                return this._names[index];
            }
            return null;
        }

        public void addAsMetaZoneIntoTrie(String mzID, TextTrieMap<NameInfo> trie) {
            this.addNamesIntoTrie(mzID, null, trie);
        }

        public void addAsTimeZoneIntoTrie(String tzID, TextTrieMap<NameInfo> trie) {
            this.addNamesIntoTrie(null, tzID, trie);
        }

        private void addNamesIntoTrie(String mzID, String tzID, TextTrieMap<NameInfo> trie) {
            if (this._names == null || this.didAddIntoTrie) {
                return;
            }
            this.didAddIntoTrie = true;
            for (int i2 = 0; i2 < this._names.length; ++i2) {
                String name = this._names[i2];
                if (name == null) continue;
                NameInfo info = new NameInfo();
                info.mzID = mzID;
                info.tzID = tzID;
                info.type = ZNames.getNameType(i2);
                trie.put(name, info);
            }
        }

        private static enum NameTypeIndex {
            EXEMPLAR_LOCATION,
            LONG_GENERIC,
            LONG_STANDARD,
            LONG_DAYLIGHT,
            SHORT_GENERIC,
            SHORT_STANDARD,
            SHORT_DAYLIGHT;

            static final NameTypeIndex[] values;

            static {
                values = NameTypeIndex.values();
            }
        }
    }

    private static final class ZNamesLoader
    extends UResource.Sink {
        private String[] names;
        private static ZNamesLoader DUMMY_LOADER = new ZNamesLoader();

        private ZNamesLoader() {
        }

        void loadMetaZone(ICUResourceBundle zoneStrings, String mzID) {
            String key = TimeZoneNamesImpl.MZ_PREFIX + mzID;
            this.loadNames(zoneStrings, key);
        }

        void loadTimeZone(ICUResourceBundle zoneStrings, String tzID) {
            String key = tzID.replace('/', ':');
            this.loadNames(zoneStrings, key);
        }

        void loadNames(ICUResourceBundle zoneStrings, String key) {
            assert (zoneStrings != null);
            assert (key != null);
            assert (key.length() > 0);
            this.names = null;
            try {
                zoneStrings.getAllItemsWithFallback(key, this);
            }
            catch (MissingResourceException missingResourceException) {
                // empty catch block
            }
        }

        private static ZNames.NameTypeIndex nameTypeIndexFromKey(UResource.Key key) {
            if (key.length() != 2) {
                return null;
            }
            char c0 = key.charAt(0);
            char c1 = key.charAt(1);
            if (c0 == 'l') {
                return c1 == 'g' ? ZNames.NameTypeIndex.LONG_GENERIC : (c1 == 's' ? ZNames.NameTypeIndex.LONG_STANDARD : (c1 == 'd' ? ZNames.NameTypeIndex.LONG_DAYLIGHT : null));
            }
            if (c0 == 's') {
                return c1 == 'g' ? ZNames.NameTypeIndex.SHORT_GENERIC : (c1 == 's' ? ZNames.NameTypeIndex.SHORT_STANDARD : (c1 == 'd' ? ZNames.NameTypeIndex.SHORT_DAYLIGHT : null));
            }
            if (c0 == 'e' && c1 == 'c') {
                return ZNames.NameTypeIndex.EXEMPLAR_LOCATION;
            }
            return null;
        }

        private void setNameIfEmpty(UResource.Key key, UResource.Value value) {
            ZNames.NameTypeIndex index;
            if (this.names == null) {
                this.names = new String[7];
            }
            if ((index = ZNamesLoader.nameTypeIndexFromKey(key)) == null) {
                return;
            }
            assert (index.ordinal() < 7);
            if (this.names[index.ordinal()] == null) {
                this.names[index.ordinal()] = value.getString();
            }
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table namesTable = value.getTable();
            int i2 = 0;
            while (namesTable.getKeyAndValue(i2, key, value)) {
                assert (value.getType() == 0);
                this.setNameIfEmpty(key, value);
                ++i2;
            }
        }

        private String[] getNames() {
            if (Utility.sameObjects(this.names, null)) {
                return null;
            }
            int length = 0;
            for (int i2 = 0; i2 < 7; ++i2) {
                String name = this.names[i2];
                if (name == null) continue;
                if (name.equals("\u2205\u2205\u2205")) {
                    this.names[i2] = null;
                    continue;
                }
                length = i2 + 1;
            }
            Object result = length == 7 ? this.names : (length == 0 ? null : Arrays.copyOfRange(this.names, 0, length));
            return result;
        }
    }

    private static class NameSearchHandler
    implements TextTrieMap.ResultHandler<NameInfo> {
        private EnumSet<TimeZoneNames.NameType> _nameTypes;
        private Collection<TimeZoneNames.MatchInfo> _matches;
        private int _maxMatchLen;

        NameSearchHandler(EnumSet<TimeZoneNames.NameType> nameTypes) {
            this._nameTypes = nameTypes;
        }

        @Override
        public boolean handlePrefixMatch(int matchLength, Iterator<NameInfo> values) {
            while (values.hasNext()) {
                TimeZoneNames.MatchInfo minfo;
                NameInfo ninfo = values.next();
                if (this._nameTypes != null && !this._nameTypes.contains((Object)ninfo.type)) continue;
                if (ninfo.tzID != null) {
                    minfo = new TimeZoneNames.MatchInfo(ninfo.type, ninfo.tzID, null, matchLength);
                } else {
                    assert (ninfo.mzID != null);
                    minfo = new TimeZoneNames.MatchInfo(ninfo.type, null, ninfo.mzID, matchLength);
                }
                if (this._matches == null) {
                    this._matches = new LinkedList<TimeZoneNames.MatchInfo>();
                }
                this._matches.add(minfo);
                if (matchLength <= this._maxMatchLen) continue;
                this._maxMatchLen = matchLength;
            }
            return true;
        }

        public Collection<TimeZoneNames.MatchInfo> getMatches() {
            if (this._matches == null) {
                return Collections.emptyList();
            }
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

    private static class NameInfo {
        String tzID;
        String mzID;
        TimeZoneNames.NameType type;

        private NameInfo() {
        }
    }

    private final class ZoneStringsLoader
    extends UResource.Sink {
        private static final int INITIAL_NUM_ZONES = 300;
        private HashMap<UResource.Key, ZNamesLoader> keyToLoader = new HashMap(300);
        private StringBuilder sb = new StringBuilder(32);

        private ZoneStringsLoader() {
        }

        void load() {
            TimeZoneNamesImpl.this._zoneStrings.getAllItemsWithFallback("", this);
            for (Map.Entry<UResource.Key, ZNamesLoader> entry : this.keyToLoader.entrySet()) {
                ZNamesLoader loader = entry.getValue();
                if (loader == ZNamesLoader.DUMMY_LOADER) continue;
                UResource.Key key = entry.getKey();
                if (this.isMetaZone(key)) {
                    String mzID = this.mzIDFromKey(key);
                    ZNames.createMetaZoneAndPutInCache(TimeZoneNamesImpl.this._mzNamesMap, loader.getNames(), mzID);
                    continue;
                }
                String tzID = this.tzIDFromKey(key);
                ZNames.createTimeZoneAndPutInCache(TimeZoneNamesImpl.this._tzNamesMap, loader.getNames(), tzID);
            }
        }

        @Override
        public void put(UResource.Key key, UResource.Value value, boolean noFallback) {
            UResource.Table timeZonesTable = value.getTable();
            int j2 = 0;
            while (timeZonesTable.getKeyAndValue(j2, key, value)) {
                assert (!value.isNoInheritanceMarker());
                if (value.getType() == 2) {
                    this.consumeNamesTable(key, value, noFallback);
                }
                ++j2;
            }
        }

        private void consumeNamesTable(UResource.Key key, UResource.Value value, boolean noFallback) {
            ZNamesLoader loader = this.keyToLoader.get(key);
            if (loader == null) {
                if (this.isMetaZone(key)) {
                    String mzID = this.mzIDFromKey(key);
                    loader = TimeZoneNamesImpl.this._mzNamesMap.containsKey(mzID) ? ZNamesLoader.DUMMY_LOADER : new ZNamesLoader();
                } else {
                    String tzID = this.tzIDFromKey(key);
                    loader = TimeZoneNamesImpl.this._tzNamesMap.containsKey(tzID) ? ZNamesLoader.DUMMY_LOADER : new ZNamesLoader();
                }
                UResource.Key newKey = this.createKey(key);
                this.keyToLoader.put(newKey, loader);
            }
            if (loader != ZNamesLoader.DUMMY_LOADER) {
                loader.put(key, value, noFallback);
            }
        }

        UResource.Key createKey(UResource.Key key) {
            return key.clone();
        }

        boolean isMetaZone(UResource.Key key) {
            return key.startsWith(TimeZoneNamesImpl.MZ_PREFIX);
        }

        private String mzIDFromKey(UResource.Key key) {
            this.sb.setLength(0);
            for (int i2 = TimeZoneNamesImpl.MZ_PREFIX.length(); i2 < key.length(); ++i2) {
                this.sb.append(key.charAt(i2));
            }
            return this.sb.toString();
        }

        private String tzIDFromKey(UResource.Key key) {
            this.sb.setLength(0);
            for (int i2 = 0; i2 < key.length(); ++i2) {
                char c2 = key.charAt(i2);
                if (c2 == ':') {
                    c2 = '/';
                }
                this.sb.append(c2);
            }
            return this.sb.toString();
        }
    }
}

