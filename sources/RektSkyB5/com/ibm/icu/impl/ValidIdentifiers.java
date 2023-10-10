/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.StringRange;
import com.ibm.icu.impl.locale.AsciiUtil;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ValidIdentifiers {
    public static Map<Datatype, Map<Datasubtype, ValiditySet>> getData() {
        return ValidityData.data;
    }

    public static Datasubtype isValid(Datatype datatype, Set<Datasubtype> datasubtypes, String code) {
        Map<Datasubtype, ValiditySet> subtable = ValidityData.data.get((Object)datatype);
        if (subtable != null) {
            for (Datasubtype datasubtype : datasubtypes) {
                ValiditySet validitySet = subtable.get((Object)datasubtype);
                if (validitySet == null || !validitySet.contains(AsciiUtil.toLowerString(code))) continue;
                return datasubtype;
            }
        }
        return null;
    }

    public static Datasubtype isValid(Datatype datatype, Set<Datasubtype> datasubtypes, String code, String value) {
        Map<Datasubtype, ValiditySet> subtable = ValidityData.data.get((Object)datatype);
        if (subtable != null) {
            code = AsciiUtil.toLowerString(code);
            value = AsciiUtil.toLowerString(value);
            for (Datasubtype datasubtype : datasubtypes) {
                ValiditySet validitySet = subtable.get((Object)datasubtype);
                if (validitySet == null || !validitySet.contains(code, value)) continue;
                return datasubtype;
            }
        }
        return null;
    }

    private static class ValidityData {
        static final Map<Datatype, Map<Datasubtype, ValiditySet>> data;

        private ValidityData() {
        }

        private static void addRange(String string, Set<String> subvalues) {
            int pos = (string = AsciiUtil.toLowerString(string)).indexOf(126);
            if (pos < 0) {
                subvalues.add(string);
            } else {
                StringRange.expand(string.substring(0, pos), string.substring(pos + 1), false, subvalues);
            }
        }

        static {
            EnumMap _data = new EnumMap(Datatype.class);
            UResourceBundle suppData = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
            UResourceBundle validityInfo = suppData.get("idValidity");
            UResourceBundleIterator datatypeIterator = validityInfo.getIterator();
            while (datatypeIterator.hasNext()) {
                UResourceBundle datatype = datatypeIterator.next();
                String rawKey = datatype.getKey();
                Datatype key = Datatype.valueOf(rawKey);
                EnumMap<Datasubtype, ValiditySet> values = new EnumMap<Datasubtype, ValiditySet>(Datasubtype.class);
                UResourceBundleIterator datasubtypeIterator = datatype.getIterator();
                while (datasubtypeIterator.hasNext()) {
                    UResourceBundle datasubtype = datasubtypeIterator.next();
                    String rawsubkey = datasubtype.getKey();
                    Datasubtype subkey = Datasubtype.valueOf(rawsubkey);
                    HashSet<String> subvalues = new HashSet<String>();
                    if (datasubtype.getType() == 0) {
                        ValidityData.addRange(datasubtype.getString(), subvalues);
                    } else {
                        for (String string : datasubtype.getStringArray()) {
                            ValidityData.addRange(string, subvalues);
                        }
                    }
                    values.put(subkey, new ValiditySet(subvalues, key == Datatype.subdivision));
                }
                _data.put(key, Collections.unmodifiableMap(values));
            }
            data = Collections.unmodifiableMap(_data);
        }
    }

    public static class ValiditySet {
        public final Set<String> regularData;
        public final Map<String, Set<String>> subdivisionData;

        public ValiditySet(Set<String> plainData, boolean makeMap) {
            if (makeMap) {
                HashMap<String, HashSet<String>> _subdivisionData = new HashMap<String, HashSet<String>>();
                for (String s2 : plainData) {
                    int pos = s2.indexOf(45);
                    int pos2 = pos + 1;
                    if (pos < 0) {
                        pos = s2.charAt(0) < 'A' ? 3 : 2;
                        pos2 = pos;
                    }
                    String key = s2.substring(0, pos);
                    String subdivision = s2.substring(pos2);
                    HashSet<String> oldSet = (HashSet<String>)_subdivisionData.get(key);
                    if (oldSet == null) {
                        oldSet = new HashSet<String>();
                        _subdivisionData.put(key, oldSet);
                    }
                    oldSet.add(subdivision);
                }
                this.regularData = null;
                HashMap _subdivisionData2 = new HashMap();
                for (Map.Entry e2 : _subdivisionData.entrySet()) {
                    Set value = (Set)e2.getValue();
                    Set set = value.size() == 1 ? Collections.singleton(value.iterator().next()) : Collections.unmodifiableSet(value);
                    _subdivisionData2.put(e2.getKey(), set);
                }
                this.subdivisionData = Collections.unmodifiableMap(_subdivisionData2);
            } else {
                this.regularData = Collections.unmodifiableSet(plainData);
                this.subdivisionData = null;
            }
        }

        public boolean contains(String code) {
            if (this.regularData != null) {
                return this.regularData.contains(code);
            }
            int pos = code.indexOf(45);
            String key = code.substring(0, pos);
            String value = code.substring(pos + 1);
            return this.contains(key, value);
        }

        public boolean contains(String key, String value) {
            Set<String> oldSet = this.subdivisionData.get(key);
            return oldSet != null && oldSet.contains(value);
        }

        public String toString() {
            if (this.regularData != null) {
                return this.regularData.toString();
            }
            return this.subdivisionData.toString();
        }
    }

    public static enum Datasubtype {
        deprecated,
        private_use,
        regular,
        special,
        unknown,
        macroregion;

    }

    public static enum Datatype {
        currency,
        language,
        region,
        script,
        subdivision,
        unit,
        variant,
        u,
        t,
        x,
        illegal;

    }
}

