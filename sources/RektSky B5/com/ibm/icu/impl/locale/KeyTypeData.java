/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.locale;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.locale.AsciiUtil;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.regex.Pattern;

public class KeyTypeData {
    static Set<String> DEPRECATED_KEYS = Collections.emptySet();
    static Map<String, ValueType> VALUE_TYPES = Collections.emptyMap();
    static Map<String, Set<String>> DEPRECATED_KEY_TYPES = Collections.emptyMap();
    private static final Object[][] KEY_DATA = new Object[0][];
    private static final Map<String, KeyData> KEYMAP = new HashMap<String, KeyData>();
    private static Map<String, Set<String>> BCP47_KEYS;

    public static String toBcpKey(String key) {
        KeyData keyData = KEYMAP.get(key = AsciiUtil.toLowerString(key));
        if (keyData != null) {
            return keyData.bcpId;
        }
        return null;
    }

    public static String toLegacyKey(String key) {
        KeyData keyData = KEYMAP.get(key = AsciiUtil.toLowerString(key));
        if (keyData != null) {
            return keyData.legacyId;
        }
        return null;
    }

    public static String toBcpType(String key, String type, Output<Boolean> isKnownKey, Output<Boolean> isSpecialType) {
        if (isKnownKey != null) {
            isKnownKey.value = false;
        }
        if (isSpecialType != null) {
            isSpecialType.value = false;
        }
        key = AsciiUtil.toLowerString(key);
        type = AsciiUtil.toLowerString(type);
        KeyData keyData = KEYMAP.get(key);
        if (keyData != null) {
            Type t2;
            if (isKnownKey != null) {
                isKnownKey.value = Boolean.TRUE;
            }
            if ((t2 = keyData.typeMap.get(type)) != null) {
                return t2.bcpId;
            }
            if (keyData.specialTypes != null) {
                for (SpecialType st : keyData.specialTypes) {
                    if (!st.handler.isWellFormed(type)) continue;
                    if (isSpecialType != null) {
                        isSpecialType.value = true;
                    }
                    return st.handler.canonicalize(type);
                }
            }
        }
        return null;
    }

    public static String toLegacyType(String key, String type, Output<Boolean> isKnownKey, Output<Boolean> isSpecialType) {
        if (isKnownKey != null) {
            isKnownKey.value = false;
        }
        if (isSpecialType != null) {
            isSpecialType.value = false;
        }
        key = AsciiUtil.toLowerString(key);
        type = AsciiUtil.toLowerString(type);
        KeyData keyData = KEYMAP.get(key);
        if (keyData != null) {
            Type t2;
            if (isKnownKey != null) {
                isKnownKey.value = Boolean.TRUE;
            }
            if ((t2 = keyData.typeMap.get(type)) != null) {
                return t2.legacyId;
            }
            if (keyData.specialTypes != null) {
                for (SpecialType st : keyData.specialTypes) {
                    if (!st.handler.isWellFormed(type)) continue;
                    if (isSpecialType != null) {
                        isSpecialType.value = true;
                    }
                    return st.handler.canonicalize(type);
                }
            }
        }
        return null;
    }

    private static void initFromResourceBundle() {
        UResourceBundle keyTypeDataRes = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "keyTypeData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        KeyTypeData.getKeyInfo(keyTypeDataRes.get("keyInfo"));
        KeyTypeData.getTypeInfo(keyTypeDataRes.get("typeInfo"));
        UResourceBundle keyMapRes = keyTypeDataRes.get("keyMap");
        UResourceBundle typeMapRes = keyTypeDataRes.get("typeMap");
        UResourceBundle typeAliasRes = null;
        UResourceBundle bcpTypeAliasRes = null;
        try {
            typeAliasRes = keyTypeDataRes.get("typeAlias");
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
        try {
            bcpTypeAliasRes = keyTypeDataRes.get("bcpTypeAlias");
        }
        catch (MissingResourceException missingResourceException) {
            // empty catch block
        }
        UResourceBundleIterator keyMapItr = keyMapRes.getIterator();
        LinkedHashMap _Bcp47Keys = new LinkedHashMap();
        while (keyMapItr.hasNext()) {
            UResourceBundle typeMapResByKey;
            EnumSet<SpecialType> specialTypeSet;
            HashMap<String, Type> typeDataMap;
            HashMap<String, HashSet<String>> bcpTypeAliasMap;
            HashMap<String, HashSet<String>> typeAliasMap;
            boolean isTZ;
            LinkedHashSet<String> _bcp47Types;
            boolean hasSameKey;
            String bcpKeyId;
            String legacyKeyId;
            block31: {
                UResourceBundle keyMapEntry = keyMapItr.next();
                legacyKeyId = keyMapEntry.getKey();
                bcpKeyId = keyMapEntry.getString();
                hasSameKey = false;
                if (bcpKeyId.length() == 0) {
                    bcpKeyId = legacyKeyId;
                    hasSameKey = true;
                }
                _bcp47Types = new LinkedHashSet<String>();
                _Bcp47Keys.put(bcpKeyId, Collections.unmodifiableSet(_bcp47Types));
                isTZ = legacyKeyId.equals("timezone");
                typeAliasMap = null;
                if (typeAliasRes != null) {
                    UResourceBundle typeAliasResByKey = null;
                    try {
                        typeAliasResByKey = typeAliasRes.get(legacyKeyId);
                    }
                    catch (MissingResourceException missingResourceException) {
                        // empty catch block
                    }
                    if (typeAliasResByKey != null) {
                        typeAliasMap = new HashMap<String, HashSet<String>>();
                        UResourceBundleIterator typeAliasResItr = typeAliasResByKey.getIterator();
                        while (typeAliasResItr.hasNext()) {
                            HashSet<String> aliasSet;
                            UResourceBundle typeAliasDataEntry = typeAliasResItr.next();
                            String from = typeAliasDataEntry.getKey();
                            String to = typeAliasDataEntry.getString();
                            if (isTZ) {
                                from = from.replace(':', '/');
                            }
                            if ((aliasSet = (HashSet<String>)typeAliasMap.get(to)) == null) {
                                aliasSet = new HashSet<String>();
                                typeAliasMap.put(to, aliasSet);
                            }
                            aliasSet.add(from);
                        }
                    }
                }
                bcpTypeAliasMap = null;
                if (bcpTypeAliasRes != null) {
                    UResourceBundle bcpTypeAliasResByKey = null;
                    try {
                        bcpTypeAliasResByKey = bcpTypeAliasRes.get(bcpKeyId);
                    }
                    catch (MissingResourceException typeAliasDataEntry) {
                        // empty catch block
                    }
                    if (bcpTypeAliasResByKey != null) {
                        bcpTypeAliasMap = new HashMap<String, HashSet<String>>();
                        UResourceBundleIterator bcpTypeAliasResItr = bcpTypeAliasResByKey.getIterator();
                        while (bcpTypeAliasResItr.hasNext()) {
                            UResourceBundle bcpTypeAliasDataEntry = bcpTypeAliasResItr.next();
                            String from = bcpTypeAliasDataEntry.getKey();
                            String to = bcpTypeAliasDataEntry.getString();
                            HashSet<String> aliasSet = (HashSet<String>)bcpTypeAliasMap.get(to);
                            if (aliasSet == null) {
                                aliasSet = new HashSet<String>();
                                bcpTypeAliasMap.put(to, aliasSet);
                            }
                            aliasSet.add(from);
                        }
                    }
                }
                typeDataMap = new HashMap<String, Type>();
                specialTypeSet = null;
                typeMapResByKey = null;
                try {
                    typeMapResByKey = typeMapRes.get(legacyKeyId);
                }
                catch (MissingResourceException e2) {
                    if ($assertionsDisabled) break block31;
                    throw new AssertionError();
                }
            }
            if (typeMapResByKey != null) {
                UResourceBundleIterator typeMapResByKeyItr = typeMapResByKey.getIterator();
                while (typeMapResByKeyItr.hasNext()) {
                    Set bcpTypeAliasSet;
                    Set typeAliasSet;
                    boolean isSpecialType;
                    UResourceBundle typeMapEntry = typeMapResByKeyItr.next();
                    String legacyTypeId = typeMapEntry.getKey();
                    String bcpTypeId = typeMapEntry.getString();
                    char first = legacyTypeId.charAt(0);
                    boolean bl = isSpecialType = '9' < first && first < 'a' && bcpTypeId.length() == 0;
                    if (isSpecialType) {
                        if (specialTypeSet == null) {
                            specialTypeSet = EnumSet.noneOf(SpecialType.class);
                        }
                        specialTypeSet.add(SpecialType.valueOf(legacyTypeId));
                        _bcp47Types.add(legacyTypeId);
                        continue;
                    }
                    if (isTZ) {
                        legacyTypeId = legacyTypeId.replace(':', '/');
                    }
                    boolean hasSameType = false;
                    if (bcpTypeId.length() == 0) {
                        bcpTypeId = legacyTypeId;
                        hasSameType = true;
                    }
                    _bcp47Types.add(bcpTypeId);
                    Type t2 = new Type(legacyTypeId, bcpTypeId);
                    typeDataMap.put(AsciiUtil.toLowerString(legacyTypeId), t2);
                    if (!hasSameType) {
                        typeDataMap.put(AsciiUtil.toLowerString(bcpTypeId), t2);
                    }
                    if (typeAliasMap != null && (typeAliasSet = (Set)typeAliasMap.get(legacyTypeId)) != null) {
                        for (String alias : typeAliasSet) {
                            typeDataMap.put(AsciiUtil.toLowerString(alias), t2);
                        }
                    }
                    if (bcpTypeAliasMap == null || (bcpTypeAliasSet = (Set)bcpTypeAliasMap.get(bcpTypeId)) == null) continue;
                    for (String alias : bcpTypeAliasSet) {
                        typeDataMap.put(AsciiUtil.toLowerString(alias), t2);
                    }
                }
            }
            KeyData keyData = new KeyData(legacyKeyId, bcpKeyId, typeDataMap, specialTypeSet);
            KEYMAP.put(AsciiUtil.toLowerString(legacyKeyId), keyData);
            if (hasSameKey) continue;
            KEYMAP.put(AsciiUtil.toLowerString(bcpKeyId), keyData);
        }
        BCP47_KEYS = Collections.unmodifiableMap(_Bcp47Keys);
    }

    private static void getKeyInfo(UResourceBundle keyInfoRes) {
        LinkedHashSet<String> _deprecatedKeys = new LinkedHashSet<String>();
        LinkedHashMap<String, ValueType> _valueTypes = new LinkedHashMap<String, ValueType>();
        UResourceBundleIterator keyInfoIt = keyInfoRes.getIterator();
        while (keyInfoIt.hasNext()) {
            UResourceBundle keyInfoEntry = keyInfoIt.next();
            String key = keyInfoEntry.getKey();
            KeyInfoType keyInfo = KeyInfoType.valueOf(key);
            UResourceBundleIterator keyInfoIt2 = keyInfoEntry.getIterator();
            while (keyInfoIt2.hasNext()) {
                UResourceBundle keyInfoEntry2 = keyInfoIt2.next();
                String key2 = keyInfoEntry2.getKey();
                String value2 = keyInfoEntry2.getString();
                switch (keyInfo) {
                    case deprecated: {
                        _deprecatedKeys.add(key2);
                        break;
                    }
                    case valueType: {
                        _valueTypes.put(key2, ValueType.valueOf(value2));
                    }
                }
            }
        }
        DEPRECATED_KEYS = Collections.unmodifiableSet(_deprecatedKeys);
        VALUE_TYPES = Collections.unmodifiableMap(_valueTypes);
    }

    private static void getTypeInfo(UResourceBundle typeInfoRes) {
        LinkedHashMap _deprecatedKeyTypes = new LinkedHashMap();
        UResourceBundleIterator keyInfoIt = typeInfoRes.getIterator();
        while (keyInfoIt.hasNext()) {
            UResourceBundle keyInfoEntry = keyInfoIt.next();
            String key = keyInfoEntry.getKey();
            TypeInfoType typeInfo = TypeInfoType.valueOf(key);
            UResourceBundleIterator keyInfoIt2 = keyInfoEntry.getIterator();
            while (keyInfoIt2.hasNext()) {
                UResourceBundle keyInfoEntry2 = keyInfoIt2.next();
                String key2 = keyInfoEntry2.getKey();
                LinkedHashSet<String> _deprecatedTypes = new LinkedHashSet<String>();
                UResourceBundleIterator keyInfoIt3 = keyInfoEntry2.getIterator();
                while (keyInfoIt3.hasNext()) {
                    UResourceBundle keyInfoEntry3 = keyInfoIt3.next();
                    String key3 = keyInfoEntry3.getKey();
                    switch (typeInfo) {
                        case deprecated: {
                            _deprecatedTypes.add(key3);
                        }
                    }
                }
                _deprecatedKeyTypes.put(key2, Collections.unmodifiableSet(_deprecatedTypes));
            }
        }
        DEPRECATED_KEY_TYPES = Collections.unmodifiableMap(_deprecatedKeyTypes);
    }

    /*
     * WARNING - void declaration
     */
    private static void initFromTables() {
        for (Object[] keyDataEntry : KEY_DATA) {
            void var14_17;
            String legacyKeyId = (String)keyDataEntry[0];
            String bcpKeyId = (String)keyDataEntry[1];
            String[][] typeData = (String[][])keyDataEntry[2];
            String[][] typeAliasData = (String[][])keyDataEntry[3];
            String[][] bcpTypeAliasData = (String[][])keyDataEntry[4];
            boolean hasSameKey = false;
            if (bcpKeyId == null) {
                bcpKeyId = legacyKeyId;
                hasSameKey = true;
            }
            HashMap<String, HashSet<String>> typeAliasMap = null;
            if (typeAliasData != null) {
                typeAliasMap = new HashMap<String, HashSet<String>>();
                for (String[] stringArray : typeAliasData) {
                    String from = stringArray[0];
                    String to = stringArray[1];
                    HashSet<String> aliasSet = (HashSet<String>)typeAliasMap.get(to);
                    if (aliasSet == null) {
                        aliasSet = new HashSet<String>();
                        typeAliasMap.put(to, aliasSet);
                    }
                    aliasSet.add(from);
                }
            }
            HashMap<String, HashSet<String>> bcpTypeAliasMap = null;
            if (bcpTypeAliasData != null) {
                void var14_21;
                bcpTypeAliasMap = new HashMap<String, HashSet<String>>();
                String[][] stringArray = bcpTypeAliasData;
                int n2 = stringArray.length;
                boolean bl = false;
                while (var14_21 < n2) {
                    String[] bcpTypeAliasDataEntry = stringArray[var14_21];
                    String from = bcpTypeAliasDataEntry[0];
                    String to = bcpTypeAliasDataEntry[1];
                    HashSet<String> aliasSet = (HashSet<String>)bcpTypeAliasMap.get(to);
                    if (aliasSet == null) {
                        aliasSet = new HashSet<String>();
                        bcpTypeAliasMap.put(to, aliasSet);
                    }
                    aliasSet.add(from);
                    ++var14_21;
                }
            }
            assert (typeData != null);
            HashMap<String, Type> typeDataMap = new HashMap<String, Type>();
            HashSet<SpecialType> specialTypeSet = null;
            for (String[] typeDataEntry : typeData) {
                Set bcpTypeAliasSet;
                Set typeAliasSet;
                String legacyTypeId = typeDataEntry[0];
                String bcpTypeId = typeDataEntry[1];
                boolean isSpecialType = false;
                for (Object st : SpecialType.values()) {
                    if (!legacyTypeId.equals(st.toString())) continue;
                    isSpecialType = true;
                    if (specialTypeSet == null) {
                        specialTypeSet = new HashSet<SpecialType>();
                    }
                    specialTypeSet.add((SpecialType)((Object)st));
                    break;
                }
                if (isSpecialType) continue;
                boolean hasSameType = false;
                if (bcpTypeId == null) {
                    bcpTypeId = legacyTypeId;
                    hasSameType = true;
                }
                Type t2 = new Type(legacyTypeId, bcpTypeId);
                typeDataMap.put(AsciiUtil.toLowerString(legacyTypeId), t2);
                if (!hasSameType) {
                    typeDataMap.put(AsciiUtil.toLowerString(bcpTypeId), t2);
                }
                if ((typeAliasSet = (Set)typeAliasMap.get(legacyTypeId)) != null) {
                    Object st;
                    st = typeAliasSet.iterator();
                    while (st.hasNext()) {
                        String alias = (String)st.next();
                        typeDataMap.put(AsciiUtil.toLowerString(alias), t2);
                    }
                }
                if ((bcpTypeAliasSet = (Set)bcpTypeAliasMap.get(bcpTypeId)) == null) continue;
                for (String alias : bcpTypeAliasSet) {
                    typeDataMap.put(AsciiUtil.toLowerString(alias), t2);
                }
            }
            Object var14_23 = null;
            if (specialTypeSet != null) {
                EnumSet enumSet = EnumSet.copyOf(specialTypeSet);
            }
            KeyData keyData = new KeyData(legacyKeyId, bcpKeyId, typeDataMap, (EnumSet<SpecialType>)var14_17);
            KEYMAP.put(AsciiUtil.toLowerString(legacyKeyId), keyData);
            if (hasSameKey) continue;
            KEYMAP.put(AsciiUtil.toLowerString(bcpKeyId), keyData);
        }
    }

    public static Set<String> getBcp47Keys() {
        return BCP47_KEYS.keySet();
    }

    public static Set<String> getBcp47KeyTypes(String key) {
        return BCP47_KEYS.get(key);
    }

    public static boolean isDeprecated(String key) {
        return DEPRECATED_KEYS.contains(key);
    }

    public static boolean isDeprecated(String key, String type) {
        Set<String> deprecatedTypes = DEPRECATED_KEY_TYPES.get(key);
        if (deprecatedTypes == null) {
            return false;
        }
        return deprecatedTypes.contains(type);
    }

    public static ValueType getValueType(String key) {
        ValueType type = VALUE_TYPES.get(key);
        return type == null ? ValueType.single : type;
    }

    static {
        KeyTypeData.initFromResourceBundle();
    }

    private static enum TypeInfoType {
        deprecated;

    }

    private static enum KeyInfoType {
        deprecated,
        valueType;

    }

    private static class Type {
        String legacyId;
        String bcpId;

        Type(String legacyId, String bcpId) {
            this.legacyId = legacyId;
            this.bcpId = bcpId;
        }
    }

    private static class KeyData {
        String legacyId;
        String bcpId;
        Map<String, Type> typeMap;
        EnumSet<SpecialType> specialTypes;

        KeyData(String legacyId, String bcpId, Map<String, Type> typeMap, EnumSet<SpecialType> specialTypes) {
            this.legacyId = legacyId;
            this.bcpId = bcpId;
            this.typeMap = typeMap;
            this.specialTypes = specialTypes;
        }
    }

    private static enum SpecialType {
        CODEPOINTS(new CodepointsTypeHandler()),
        REORDER_CODE(new ReorderCodeTypeHandler()),
        RG_KEY_VALUE(new RgKeyValueTypeHandler()),
        SUBDIVISION_CODE(new SubdivisionKeyValueTypeHandler()),
        PRIVATE_USE(new PrivateUseKeyValueTypeHandler());

        SpecialTypeHandler handler;

        private SpecialType(SpecialTypeHandler handler) {
            this.handler = handler;
        }
    }

    private static class PrivateUseKeyValueTypeHandler
    extends SpecialTypeHandler {
        private static final Pattern pat = Pattern.compile("[a-zA-Z0-9]{3,8}(-[a-zA-Z0-9]{3,8})*");

        private PrivateUseKeyValueTypeHandler() {
        }

        @Override
        boolean isWellFormed(String value) {
            return pat.matcher(value).matches();
        }
    }

    private static class SubdivisionKeyValueTypeHandler
    extends SpecialTypeHandler {
        private static final Pattern pat = Pattern.compile("([a-zA-Z]{2}|[0-9]{3})");

        private SubdivisionKeyValueTypeHandler() {
        }

        @Override
        boolean isWellFormed(String value) {
            return pat.matcher(value).matches();
        }
    }

    private static class RgKeyValueTypeHandler
    extends SpecialTypeHandler {
        private static final Pattern pat = Pattern.compile("([a-zA-Z]{2}|[0-9]{3})[zZ]{4}");

        private RgKeyValueTypeHandler() {
        }

        @Override
        boolean isWellFormed(String value) {
            return pat.matcher(value).matches();
        }
    }

    private static class ReorderCodeTypeHandler
    extends SpecialTypeHandler {
        private static final Pattern pat = Pattern.compile("[a-zA-Z]{3,8}(-[a-zA-Z]{3,8})*");

        private ReorderCodeTypeHandler() {
        }

        @Override
        boolean isWellFormed(String value) {
            return pat.matcher(value).matches();
        }
    }

    private static class CodepointsTypeHandler
    extends SpecialTypeHandler {
        private static final Pattern pat = Pattern.compile("[0-9a-fA-F]{4,6}(-[0-9a-fA-F]{4,6})*");

        private CodepointsTypeHandler() {
        }

        @Override
        boolean isWellFormed(String value) {
            return pat.matcher(value).matches();
        }
    }

    private static abstract class SpecialTypeHandler {
        private SpecialTypeHandler() {
        }

        abstract boolean isWellFormed(String var1);

        String canonicalize(String value) {
            return AsciiUtil.toLowerString(value);
        }
    }

    public static enum ValueType {
        single,
        multiple,
        incremental,
        any;

    }
}

