/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.locale;

import com.ibm.icu.impl.ValidIdentifiers;
import com.ibm.icu.impl.locale.AsciiUtil;
import com.ibm.icu.impl.locale.KeyTypeData;
import com.ibm.icu.util.IllformedLocaleException;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.ULocale;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class LocaleValidityChecker {
    private final Set<ValidIdentifiers.Datasubtype> datasubtypes;
    private final boolean allowsDeprecated;
    static Pattern SEPARATOR = Pattern.compile("[-_]");
    private static final Pattern VALID_X = Pattern.compile("[a-zA-Z0-9]{2,8}(-[a-zA-Z0-9]{2,8})*");
    static final Set<String> REORDERING_INCLUDE = new HashSet<String>(Arrays.asList("space", "punct", "symbol", "currency", "digit", "others", "zzzz"));
    static final Set<String> REORDERING_EXCLUDE = new HashSet<String>(Arrays.asList("zinh", "zyyy"));
    static final Set<ValidIdentifiers.Datasubtype> REGULAR_ONLY = EnumSet.of(ValidIdentifiers.Datasubtype.regular);

    public LocaleValidityChecker(Set<ValidIdentifiers.Datasubtype> datasubtypes) {
        this.datasubtypes = EnumSet.copyOf(datasubtypes);
        this.allowsDeprecated = datasubtypes.contains((Object)ValidIdentifiers.Datasubtype.deprecated);
    }

    public LocaleValidityChecker(ValidIdentifiers.Datasubtype ... datasubtypes) {
        this.datasubtypes = EnumSet.copyOf(Arrays.asList(datasubtypes));
        this.allowsDeprecated = this.datasubtypes.contains((Object)ValidIdentifiers.Datasubtype.deprecated);
    }

    public Set<ValidIdentifiers.Datasubtype> getDatasubtypes() {
        return EnumSet.copyOf(this.datasubtypes);
    }

    public boolean isValid(ULocale locale, Where where) {
        where.set(null, null);
        String language = locale.getLanguage();
        String script = locale.getScript();
        String region = locale.getCountry();
        String variantString = locale.getVariant();
        Set<Character> extensionKeys = locale.getExtensionKeys();
        if (!this.isValid(ValidIdentifiers.Datatype.language, language, where)) {
            if (language.equals("x")) {
                where.set(null, null);
                return true;
            }
            return false;
        }
        if (!this.isValid(ValidIdentifiers.Datatype.script, script, where)) {
            return false;
        }
        if (!this.isValid(ValidIdentifiers.Datatype.region, region, where)) {
            return false;
        }
        if (!variantString.isEmpty()) {
            for (String variant : SEPARATOR.split(variantString)) {
                if (this.isValid(ValidIdentifiers.Datatype.variant, variant, where)) continue;
                return false;
            }
        }
        for (Character c2 : extensionKeys) {
            try {
                ValidIdentifiers.Datatype datatype = ValidIdentifiers.Datatype.valueOf(c2 + "");
                switch (datatype) {
                    case x: {
                        return true;
                    }
                    case t: 
                    case u: {
                        if (this.isValidU(locale, datatype, locale.getExtension(c2.charValue()), where)) break;
                        return false;
                    }
                }
            }
            catch (Exception e2) {
                return where.set(ValidIdentifiers.Datatype.illegal, c2 + "");
            }
        }
        return true;
    }

    private boolean isValidU(ULocale locale, ValidIdentifiers.Datatype datatype, String extensionString, Where where) {
        String key = "";
        int typeCount = 0;
        Enum valueType = null;
        Enum specialCase = null;
        StringBuilder prefix = new StringBuilder();
        HashSet<String> seen = new HashSet<String>();
        StringBuilder tBuffer = datatype == ValidIdentifiers.Datatype.t ? new StringBuilder() : null;
        block14: for (String subtag : SEPARATOR.split(extensionString)) {
            if (subtag.length() == 2 && (tBuffer == null || subtag.charAt(1) <= '9')) {
                if (tBuffer != null) {
                    if (tBuffer.length() != 0 && !this.isValidLocale(tBuffer.toString(), where)) {
                        return false;
                    }
                    tBuffer = null;
                }
                if ((key = KeyTypeData.toBcpKey(subtag)) == null) {
                    return where.set(datatype, subtag);
                }
                if (!this.allowsDeprecated && KeyTypeData.isDeprecated(key)) {
                    return where.set(datatype, key);
                }
                valueType = KeyTypeData.getValueType(key);
                specialCase = SpecialCase.get(key);
                typeCount = 0;
                continue;
            }
            if (tBuffer != null) {
                if (tBuffer.length() != 0) {
                    tBuffer.append('-');
                }
                tBuffer.append(subtag);
                continue;
            }
            ++typeCount;
            switch (1.$SwitchMap$com$ibm$icu$impl$locale$KeyTypeData$ValueType[valueType.ordinal()]) {
                case 1: {
                    if (typeCount <= 1) break;
                    return where.set(datatype, key + "-" + subtag);
                }
                case 2: {
                    if (typeCount == 1) {
                        prefix.setLength(0);
                        prefix.append(subtag);
                        break;
                    }
                    prefix.append('-').append(subtag);
                    subtag = prefix.toString();
                    break;
                }
                case 3: {
                    if (typeCount != 1) break;
                    seen.clear();
                    break;
                }
            }
            switch (1.$SwitchMap$com$ibm$icu$impl$locale$LocaleValidityChecker$SpecialCase[specialCase.ordinal()]) {
                case 1: {
                    continue block14;
                }
                case 2: {
                    try {
                        if (Integer.parseInt(subtag, 16) <= 0x10FFFF) continue block14;
                        return where.set(datatype, key + "-" + subtag);
                    }
                    catch (NumberFormatException e2) {
                        return where.set(datatype, key + "-" + subtag);
                    }
                }
                case 3: {
                    boolean newlyAdded = seen.add(subtag.equals("zzzz") ? "others" : subtag);
                    if (newlyAdded && this.isScriptReorder(subtag)) continue block14;
                    return where.set(datatype, key + "-" + subtag);
                }
                case 4: {
                    if (this.isSubdivision(locale, subtag)) continue block14;
                    return where.set(datatype, key + "-" + subtag);
                }
                case 5: {
                    if (subtag.length() < 6 || !subtag.endsWith("zzzz")) {
                        return where.set(datatype, subtag);
                    }
                    if (this.isValid(ValidIdentifiers.Datatype.region, subtag.substring(0, subtag.length() - 4), where)) continue block14;
                    return false;
                }
                default: {
                    Output<Boolean> isKnownKey = new Output<Boolean>();
                    Output<Boolean> isSpecialType = new Output<Boolean>();
                    String type = KeyTypeData.toBcpType(key, subtag, isKnownKey, isSpecialType);
                    if (type == null) {
                        return where.set(datatype, key + "-" + subtag);
                    }
                    if (this.allowsDeprecated || !KeyTypeData.isDeprecated(key, subtag)) continue block14;
                    return where.set(datatype, key + "-" + subtag);
                }
            }
        }
        return tBuffer == null || tBuffer.length() == 0 || this.isValidLocale(tBuffer.toString(), where);
    }

    private boolean isSubdivision(ULocale locale, String subtag) {
        String subdivision;
        if (subtag.length() < 3) {
            return false;
        }
        String region = subtag.substring(0, subtag.charAt(0) <= '9' ? 3 : 2);
        if (ValidIdentifiers.isValid(ValidIdentifiers.Datatype.subdivision, this.datasubtypes, region, subdivision = subtag.substring(region.length())) == null) {
            return false;
        }
        String localeRegion = locale.getCountry();
        if (localeRegion.isEmpty()) {
            ULocale max = ULocale.addLikelySubtags(locale);
            localeRegion = max.getCountry();
        }
        return region.equalsIgnoreCase(localeRegion);
    }

    private boolean isScriptReorder(String subtag) {
        if (REORDERING_INCLUDE.contains(subtag = AsciiUtil.toLowerString(subtag))) {
            return true;
        }
        if (REORDERING_EXCLUDE.contains(subtag)) {
            return false;
        }
        return ValidIdentifiers.isValid(ValidIdentifiers.Datatype.script, REGULAR_ONLY, subtag) != null;
    }

    private boolean isValidLocale(String extensionString, Where where) {
        try {
            ULocale locale = new ULocale.Builder().setLanguageTag(extensionString).build();
            return this.isValid(locale, where);
        }
        catch (IllformedLocaleException e2) {
            int startIndex = e2.getErrorIndex();
            String[] list = SEPARATOR.split(extensionString.substring(startIndex));
            return where.set(ValidIdentifiers.Datatype.t, list[0]);
        }
        catch (Exception e3) {
            return where.set(ValidIdentifiers.Datatype.t, e3.getMessage());
        }
    }

    private boolean isValid(ValidIdentifiers.Datatype datatype, String code, Where where) {
        if (code.isEmpty()) {
            return true;
        }
        if (datatype == ValidIdentifiers.Datatype.variant && "posix".equalsIgnoreCase(code)) {
            return true;
        }
        return ValidIdentifiers.isValid(datatype, this.datasubtypes, code) != null ? true : (where == null ? false : where.set(datatype, code));
    }

    static enum SpecialCase {
        normal,
        anything,
        reorder,
        codepoints,
        subdivision,
        rgKey;


        static SpecialCase get(String key) {
            if (key.equals("kr")) {
                return reorder;
            }
            if (key.equals("vt")) {
                return codepoints;
            }
            if (key.equals("sd")) {
                return subdivision;
            }
            if (key.equals("rg")) {
                return rgKey;
            }
            if (key.equals("x0")) {
                return anything;
            }
            return normal;
        }
    }

    public static class Where {
        public ValidIdentifiers.Datatype fieldFailure;
        public String codeFailure;

        public boolean set(ValidIdentifiers.Datatype datatype, String code) {
            this.fieldFailure = datatype;
            this.codeFailure = code;
            return false;
        }

        public String toString() {
            return this.fieldFailure == null ? "OK" : "{" + (Object)((Object)this.fieldFailure) + ", " + this.codeFailure + "}";
        }
    }
}

