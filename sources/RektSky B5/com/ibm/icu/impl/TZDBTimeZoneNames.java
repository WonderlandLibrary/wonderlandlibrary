/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.TextTrieMap;
import com.ibm.icu.impl.TimeZoneNamesImpl;
import com.ibm.icu.text.TimeZoneNames;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TZDBTimeZoneNames
extends TimeZoneNames {
    private static final long serialVersionUID = 1L;
    private static final ConcurrentHashMap<String, TZDBNames> TZDB_NAMES_MAP = new ConcurrentHashMap();
    private static volatile TextTrieMap<TZDBNameInfo> TZDB_NAMES_TRIE = null;
    private static final ICUResourceBundle ZONESTRINGS;
    private ULocale _locale;
    private volatile transient String _region;

    public TZDBTimeZoneNames(ULocale loc) {
        this._locale = loc;
    }

    @Override
    public Set<String> getAvailableMetaZoneIDs() {
        return TimeZoneNamesImpl._getAvailableMetaZoneIDs();
    }

    @Override
    public Set<String> getAvailableMetaZoneIDs(String tzID) {
        return TimeZoneNamesImpl._getAvailableMetaZoneIDs(tzID);
    }

    @Override
    public String getMetaZoneID(String tzID, long date) {
        return TimeZoneNamesImpl._getMetaZoneID(tzID, date);
    }

    @Override
    public String getReferenceZoneID(String mzID, String region) {
        return TimeZoneNamesImpl._getReferenceZoneID(mzID, region);
    }

    @Override
    public String getMetaZoneDisplayName(String mzID, TimeZoneNames.NameType type) {
        if (mzID == null || mzID.length() == 0 || type != TimeZoneNames.NameType.SHORT_STANDARD && type != TimeZoneNames.NameType.SHORT_DAYLIGHT) {
            return null;
        }
        return TZDBTimeZoneNames.getMetaZoneNames(mzID).getName(type);
    }

    @Override
    public String getTimeZoneDisplayName(String tzID, TimeZoneNames.NameType type) {
        return null;
    }

    @Override
    public Collection<TimeZoneNames.MatchInfo> find(CharSequence text, int start, EnumSet<TimeZoneNames.NameType> nameTypes) {
        if (text == null || text.length() == 0 || start < 0 || start >= text.length()) {
            throw new IllegalArgumentException("bad input text or range");
        }
        TZDBTimeZoneNames.prepareFind();
        TZDBNameSearchHandler handler = new TZDBNameSearchHandler(nameTypes, this.getTargetRegion());
        TZDB_NAMES_TRIE.find(text, start, handler);
        return handler.getMatches();
    }

    private static TZDBNames getMetaZoneNames(String mzID) {
        TZDBNames names = TZDB_NAMES_MAP.get(mzID);
        if (names == null) {
            names = TZDBNames.getInstance(ZONESTRINGS, "meta:" + mzID);
            TZDBNames tmpNames = TZDB_NAMES_MAP.putIfAbsent(mzID = mzID.intern(), names);
            names = tmpNames == null ? names : tmpNames;
        }
        return names;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void prepareFind() {
        if (TZDB_NAMES_TRIE != null) return;
        Class<TZDBTimeZoneNames> clazz = TZDBTimeZoneNames.class;
        synchronized (TZDBTimeZoneNames.class) {
            if (TZDB_NAMES_TRIE != null) return;
            TextTrieMap<TZDBNameInfo> trie = new TextTrieMap<TZDBNameInfo>(true);
            Set<String> mzIDs = TimeZoneNamesImpl._getAvailableMetaZoneIDs();
            for (String mzID : mzIDs) {
                boolean ambiguousType;
                TZDBNames names = TZDBTimeZoneNames.getMetaZoneNames(mzID);
                String std = names.getName(TimeZoneNames.NameType.SHORT_STANDARD);
                String dst = names.getName(TimeZoneNames.NameType.SHORT_DAYLIGHT);
                if (std == null && dst == null) continue;
                String[] parseRegions = names.getParseRegions();
                mzID = mzID.intern();
                boolean bl = ambiguousType = std != null && dst != null && std.equals(dst);
                if (std != null) {
                    TZDBNameInfo stdInf = new TZDBNameInfo(mzID, TimeZoneNames.NameType.SHORT_STANDARD, ambiguousType, parseRegions);
                    trie.put(std, stdInf);
                }
                if (dst == null) continue;
                TZDBNameInfo dstInf = new TZDBNameInfo(mzID, TimeZoneNames.NameType.SHORT_DAYLIGHT, ambiguousType, parseRegions);
                trie.put(dst, dstInf);
            }
            TZDB_NAMES_TRIE = trie;
            // ** MonitorExit[var0] (shouldn't be in output)
            return;
        }
    }

    private String getTargetRegion() {
        if (this._region == null) {
            ULocale tmp;
            String region = this._locale.getCountry();
            if (region.length() == 0 && (region = (tmp = ULocale.addLikelySubtags(this._locale)).getCountry()).length() == 0) {
                region = "001";
            }
            this._region = region;
        }
        return this._region;
    }

    static {
        UResourceBundle bundle = ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/zone", "tzdbNames");
        ZONESTRINGS = (ICUResourceBundle)bundle.get("zoneStrings");
    }

    private static class TZDBNameSearchHandler
    implements TextTrieMap.ResultHandler<TZDBNameInfo> {
        private EnumSet<TimeZoneNames.NameType> _nameTypes;
        private Collection<TimeZoneNames.MatchInfo> _matches;
        private String _region;

        TZDBNameSearchHandler(EnumSet<TimeZoneNames.NameType> nameTypes, String region) {
            this._nameTypes = nameTypes;
            assert (region != null);
            this._region = region;
        }

        @Override
        public boolean handlePrefixMatch(int matchLength, Iterator<TZDBNameInfo> values) {
            TZDBNameInfo match = null;
            TZDBNameInfo defaultRegionMatch = null;
            while (values.hasNext()) {
                TZDBNameInfo ninfo = values.next();
                if (this._nameTypes != null && !this._nameTypes.contains((Object)ninfo.type)) continue;
                if (ninfo.parseRegions == null) {
                    if (defaultRegionMatch != null) continue;
                    match = defaultRegionMatch = ninfo;
                    continue;
                }
                boolean matchRegion = false;
                for (String region : ninfo.parseRegions) {
                    if (!this._region.equals(region)) continue;
                    match = ninfo;
                    matchRegion = true;
                    break;
                }
                if (matchRegion) break;
                if (match != null) continue;
                match = ninfo;
            }
            if (match != null) {
                TimeZoneNames.NameType ntype = match.type;
                if (match.ambiguousType && (ntype == TimeZoneNames.NameType.SHORT_STANDARD || ntype == TimeZoneNames.NameType.SHORT_DAYLIGHT) && this._nameTypes.contains((Object)TimeZoneNames.NameType.SHORT_STANDARD) && this._nameTypes.contains((Object)TimeZoneNames.NameType.SHORT_DAYLIGHT)) {
                    ntype = TimeZoneNames.NameType.SHORT_GENERIC;
                }
                TimeZoneNames.MatchInfo minfo = new TimeZoneNames.MatchInfo(ntype, null, match.mzID, matchLength);
                if (this._matches == null) {
                    this._matches = new LinkedList<TimeZoneNames.MatchInfo>();
                }
                this._matches.add(minfo);
            }
            return true;
        }

        public Collection<TimeZoneNames.MatchInfo> getMatches() {
            if (this._matches == null) {
                return Collections.emptyList();
            }
            return this._matches;
        }
    }

    private static class TZDBNameInfo {
        final String mzID;
        final TimeZoneNames.NameType type;
        final boolean ambiguousType;
        final String[] parseRegions;

        TZDBNameInfo(String mzID, TimeZoneNames.NameType type, boolean ambiguousType, String[] parseRegions) {
            this.mzID = mzID;
            this.type = type;
            this.ambiguousType = ambiguousType;
            this.parseRegions = parseRegions;
        }
    }

    private static class TZDBNames {
        public static final TZDBNames EMPTY_TZDBNAMES = new TZDBNames(null, null);
        private String[] _names;
        private String[] _parseRegions;
        private static final String[] KEYS = new String[]{"ss", "sd"};

        private TZDBNames(String[] names, String[] parseRegions) {
            this._names = names;
            this._parseRegions = parseRegions;
        }

        static TZDBNames getInstance(ICUResourceBundle zoneStrings, String key) {
            if (zoneStrings == null || key == null || key.length() == 0) {
                return EMPTY_TZDBNAMES;
            }
            ICUResourceBundle table = null;
            try {
                table = (ICUResourceBundle)zoneStrings.get(key);
            }
            catch (MissingResourceException e2) {
                return EMPTY_TZDBNAMES;
            }
            boolean isEmpty = true;
            String[] names = new String[KEYS.length];
            for (int i2 = 0; i2 < names.length; ++i2) {
                try {
                    names[i2] = table.getString(KEYS[i2]);
                    isEmpty = false;
                    continue;
                }
                catch (MissingResourceException e3) {
                    names[i2] = null;
                }
            }
            if (isEmpty) {
                return EMPTY_TZDBNAMES;
            }
            String[] parseRegions = null;
            try {
                ICUResourceBundle regionsRes = (ICUResourceBundle)table.get("parseRegions");
                if (regionsRes.getType() == 0) {
                    parseRegions = new String[]{regionsRes.getString()};
                } else if (regionsRes.getType() == 8) {
                    parseRegions = regionsRes.getStringArray();
                }
            }
            catch (MissingResourceException missingResourceException) {
                // empty catch block
            }
            return new TZDBNames(names, parseRegions);
        }

        String getName(TimeZoneNames.NameType type) {
            if (this._names == null) {
                return null;
            }
            String name = null;
            switch (type) {
                case SHORT_STANDARD: {
                    name = this._names[0];
                    break;
                }
                case SHORT_DAYLIGHT: {
                    name = this._names[1];
                    break;
                }
            }
            return name;
        }

        String[] getParseRegions() {
            return this._parseRegions;
        }
    }
}

