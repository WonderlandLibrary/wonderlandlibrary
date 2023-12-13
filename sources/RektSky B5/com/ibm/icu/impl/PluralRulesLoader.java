/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.text.PluralRanges;
import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;

public class PluralRulesLoader
extends PluralRules.Factory {
    private final Map<String, PluralRules> rulesIdToRules = new HashMap<String, PluralRules>();
    private Map<String, String> localeIdToCardinalRulesId;
    private Map<String, String> localeIdToOrdinalRulesId;
    private Map<String, ULocale> rulesIdToEquivalentULocale;
    private static Map<String, PluralRanges> localeIdToPluralRanges;
    public static final PluralRulesLoader loader;
    private static final PluralRanges UNKNOWN_RANGE;

    private PluralRulesLoader() {
    }

    @Override
    public ULocale[] getAvailableULocales() {
        Set<String> keys = this.getLocaleIdToRulesIdMap(PluralRules.PluralType.CARDINAL).keySet();
        ULocale[] locales = new ULocale[keys.size()];
        int n2 = 0;
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            locales[n2++] = ULocale.createCanonical(iter.next());
        }
        return locales;
    }

    @Override
    public ULocale getFunctionalEquivalent(ULocale locale, boolean[] isAvailable) {
        String rulesId;
        if (isAvailable != null && isAvailable.length > 0) {
            String localeId = ULocale.canonicalize(locale.getBaseName());
            Map<String, String> idMap = this.getLocaleIdToRulesIdMap(PluralRules.PluralType.CARDINAL);
            isAvailable[0] = idMap.containsKey(localeId);
        }
        if ((rulesId = this.getRulesIdForLocale(locale, PluralRules.PluralType.CARDINAL)) == null || rulesId.trim().length() == 0) {
            return ULocale.ROOT;
        }
        ULocale result = this.getRulesIdToEquivalentULocaleMap().get(rulesId);
        if (result == null) {
            return ULocale.ROOT;
        }
        return result;
    }

    private Map<String, String> getLocaleIdToRulesIdMap(PluralRules.PluralType type) {
        this.checkBuildRulesIdMaps();
        return type == PluralRules.PluralType.CARDINAL ? this.localeIdToCardinalRulesId : this.localeIdToOrdinalRulesId;
    }

    private Map<String, ULocale> getRulesIdToEquivalentULocaleMap() {
        this.checkBuildRulesIdMaps();
        return this.rulesIdToEquivalentULocale;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void checkBuildRulesIdMaps() {
        boolean haveMap;
        PluralRulesLoader pluralRulesLoader = this;
        synchronized (pluralRulesLoader) {
            haveMap = this.localeIdToCardinalRulesId != null;
        }
        if (!haveMap) {
            Map<String, String> tempLocaleIdToOrdinalRulesId;
            Map<String, ULocale> tempRulesIdToEquivalentULocale;
            Map<String, String> tempLocaleIdToCardinalRulesId;
            try {
                String value;
                String id;
                UResourceBundle b2;
                int i2;
                UResourceBundle pluralb = this.getPluralBundle();
                UResourceBundle localeb = pluralb.get("locales");
                tempLocaleIdToCardinalRulesId = new TreeMap();
                tempRulesIdToEquivalentULocale = new HashMap();
                for (i2 = 0; i2 < localeb.getSize(); ++i2) {
                    b2 = localeb.get(i2);
                    id = b2.getKey();
                    value = b2.getString().intern();
                    tempLocaleIdToCardinalRulesId.put(id, value);
                    if (tempRulesIdToEquivalentULocale.containsKey(value)) continue;
                    tempRulesIdToEquivalentULocale.put(value, new ULocale(id));
                }
                localeb = pluralb.get("locales_ordinals");
                tempLocaleIdToOrdinalRulesId = new TreeMap();
                for (i2 = 0; i2 < localeb.getSize(); ++i2) {
                    b2 = localeb.get(i2);
                    id = b2.getKey();
                    value = b2.getString().intern();
                    tempLocaleIdToOrdinalRulesId.put(id, value);
                }
            }
            catch (MissingResourceException e2) {
                tempLocaleIdToCardinalRulesId = Collections.emptyMap();
                tempLocaleIdToOrdinalRulesId = Collections.emptyMap();
                tempRulesIdToEquivalentULocale = Collections.emptyMap();
            }
            PluralRulesLoader pluralRulesLoader2 = this;
            synchronized (pluralRulesLoader2) {
                if (this.localeIdToCardinalRulesId == null) {
                    this.localeIdToCardinalRulesId = tempLocaleIdToCardinalRulesId;
                    this.localeIdToOrdinalRulesId = tempLocaleIdToOrdinalRulesId;
                    this.rulesIdToEquivalentULocale = tempRulesIdToEquivalentULocale;
                }
            }
        }
    }

    public String getRulesIdForLocale(ULocale locale, PluralRules.PluralType type) {
        int ix;
        Map<String, String> idMap = this.getLocaleIdToRulesIdMap(type);
        String localeId = ULocale.canonicalize(locale.getBaseName());
        String rulesId = null;
        while (null == (rulesId = idMap.get(localeId)) && (ix = localeId.lastIndexOf("_")) != -1) {
            localeId = localeId.substring(0, ix);
        }
        return rulesId;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public PluralRules getRulesForRulesId(String rulesId) {
        boolean hasRules;
        PluralRules rules = null;
        Map<String, PluralRules> map = this.rulesIdToRules;
        synchronized (map) {
            hasRules = this.rulesIdToRules.containsKey(rulesId);
            if (hasRules) {
                rules = this.rulesIdToRules.get(rulesId);
            }
        }
        if (!hasRules) {
            try {
                UResourceBundle pluralb = this.getPluralBundle();
                UResourceBundle rulesb = pluralb.get("rules");
                UResourceBundle setb = rulesb.get(rulesId);
                StringBuilder sb = new StringBuilder();
                for (int i2 = 0; i2 < setb.getSize(); ++i2) {
                    UResourceBundle b2 = setb.get(i2);
                    if (i2 > 0) {
                        sb.append("; ");
                    }
                    sb.append(b2.getKey());
                    sb.append(": ");
                    sb.append(b2.getString());
                }
                rules = PluralRules.parseDescription(sb.toString());
            }
            catch (ParseException parseException) {
            }
            catch (MissingResourceException missingResourceException) {
                // empty catch block
            }
            map = this.rulesIdToRules;
            synchronized (map) {
                if (this.rulesIdToRules.containsKey(rulesId)) {
                    rules = this.rulesIdToRules.get(rulesId);
                } else {
                    this.rulesIdToRules.put(rulesId, rules);
                }
            }
        }
        return rules;
    }

    public UResourceBundle getPluralBundle() throws MissingResourceException {
        return ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", "plurals", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
    }

    @Override
    public PluralRules forLocale(ULocale locale, PluralRules.PluralType type) {
        String rulesId = this.getRulesIdForLocale(locale, type);
        if (rulesId == null || rulesId.trim().length() == 0) {
            return PluralRules.DEFAULT;
        }
        PluralRules rules = this.getRulesForRulesId(rulesId);
        if (rules == null) {
            rules = PluralRules.DEFAULT;
        }
        return rules;
    }

    @Override
    public boolean hasOverride(ULocale locale) {
        return false;
    }

    public PluralRanges getPluralRanges(ULocale locale) {
        PluralRanges result;
        String localeId = ULocale.canonicalize(locale.getBaseName());
        while (null == (result = localeIdToPluralRanges.get(localeId))) {
            int ix = localeId.lastIndexOf("_");
            if (ix == -1) {
                result = UNKNOWN_RANGE;
                break;
            }
            localeId = localeId.substring(0, ix);
        }
        return result;
    }

    public boolean isPluralRangesAvailable(ULocale locale) {
        return this.getPluralRanges(locale) == UNKNOWN_RANGE;
    }

    static {
        loader = new PluralRulesLoader();
        UNKNOWN_RANGE = new PluralRanges().freeze();
        String[][] pluralRangeData = new String[][]{{"locales", "id ja km ko lo ms my th vi zh"}, {"other", "other", "other"}, {"locales", "am bn fr gu hi hy kn mr pa zu"}, {"one", "one", "one"}, {"one", "other", "other"}, {"other", "other", "other"}, {"locales", "fa"}, {"one", "one", "other"}, {"one", "other", "other"}, {"other", "other", "other"}, {"locales", "ka"}, {"one", "other", "one"}, {"other", "one", "other"}, {"other", "other", "other"}, {"locales", "az de el gl hu it kk ky ml mn ne nl pt sq sw ta te tr ug uz"}, {"one", "other", "other"}, {"other", "one", "one"}, {"other", "other", "other"}, {"locales", "af bg ca en es et eu fi nb sv ur"}, {"one", "other", "other"}, {"other", "one", "other"}, {"other", "other", "other"}, {"locales", "da fil is"}, {"one", "one", "one"}, {"one", "other", "other"}, {"other", "one", "one"}, {"other", "other", "other"}, {"locales", "si"}, {"one", "one", "one"}, {"one", "other", "other"}, {"other", "one", "other"}, {"other", "other", "other"}, {"locales", "mk"}, {"one", "one", "other"}, {"one", "other", "other"}, {"other", "one", "other"}, {"other", "other", "other"}, {"locales", "lv"}, {"zero", "zero", "other"}, {"zero", "one", "one"}, {"zero", "other", "other"}, {"one", "zero", "other"}, {"one", "one", "one"}, {"one", "other", "other"}, {"other", "zero", "other"}, {"other", "one", "one"}, {"other", "other", "other"}, {"locales", "ro"}, {"one", "few", "few"}, {"one", "other", "other"}, {"few", "one", "few"}, {"few", "few", "few"}, {"few", "other", "other"}, {"other", "few", "few"}, {"other", "other", "other"}, {"locales", "hr sr bs"}, {"one", "one", "one"}, {"one", "few", "few"}, {"one", "other", "other"}, {"few", "one", "one"}, {"few", "few", "few"}, {"few", "other", "other"}, {"other", "one", "one"}, {"other", "few", "few"}, {"other", "other", "other"}, {"locales", "sl"}, {"one", "one", "few"}, {"one", "two", "two"}, {"one", "few", "few"}, {"one", "other", "other"}, {"two", "one", "few"}, {"two", "two", "two"}, {"two", "few", "few"}, {"two", "other", "other"}, {"few", "one", "few"}, {"few", "two", "two"}, {"few", "few", "few"}, {"few", "other", "other"}, {"other", "one", "few"}, {"other", "two", "two"}, {"other", "few", "few"}, {"other", "other", "other"}, {"locales", "he"}, {"one", "two", "other"}, {"one", "many", "many"}, {"one", "other", "other"}, {"two", "many", "other"}, {"two", "other", "other"}, {"many", "many", "many"}, {"many", "other", "many"}, {"other", "one", "other"}, {"other", "two", "other"}, {"other", "many", "many"}, {"other", "other", "other"}, {"locales", "cs pl sk"}, {"one", "few", "few"}, {"one", "many", "many"}, {"one", "other", "other"}, {"few", "few", "few"}, {"few", "many", "many"}, {"few", "other", "other"}, {"many", "one", "one"}, {"many", "few", "few"}, {"many", "many", "many"}, {"many", "other", "other"}, {"other", "one", "one"}, {"other", "few", "few"}, {"other", "many", "many"}, {"other", "other", "other"}, {"locales", "lt ru uk"}, {"one", "one", "one"}, {"one", "few", "few"}, {"one", "many", "many"}, {"one", "other", "other"}, {"few", "one", "one"}, {"few", "few", "few"}, {"few", "many", "many"}, {"few", "other", "other"}, {"many", "one", "one"}, {"many", "few", "few"}, {"many", "many", "many"}, {"many", "other", "other"}, {"other", "one", "one"}, {"other", "few", "few"}, {"other", "many", "many"}, {"other", "other", "other"}, {"locales", "cy"}, {"zero", "one", "one"}, {"zero", "two", "two"}, {"zero", "few", "few"}, {"zero", "many", "many"}, {"zero", "other", "other"}, {"one", "two", "two"}, {"one", "few", "few"}, {"one", "many", "many"}, {"one", "other", "other"}, {"two", "few", "few"}, {"two", "many", "many"}, {"two", "other", "other"}, {"few", "many", "many"}, {"few", "other", "other"}, {"many", "other", "other"}, {"other", "one", "one"}, {"other", "two", "two"}, {"other", "few", "few"}, {"other", "many", "many"}, {"other", "other", "other"}, {"locales", "ar"}, {"zero", "one", "zero"}, {"zero", "two", "zero"}, {"zero", "few", "few"}, {"zero", "many", "many"}, {"zero", "other", "other"}, {"one", "two", "other"}, {"one", "few", "few"}, {"one", "many", "many"}, {"one", "other", "other"}, {"two", "few", "few"}, {"two", "many", "many"}, {"two", "other", "other"}, {"few", "few", "few"}, {"few", "many", "many"}, {"few", "other", "other"}, {"many", "few", "few"}, {"many", "many", "many"}, {"many", "other", "other"}, {"other", "one", "other"}, {"other", "two", "other"}, {"other", "few", "few"}, {"other", "many", "many"}, {"other", "other", "other"}};
        PluralRanges pr = null;
        String[] locales = null;
        HashMap<Object, PluralRanges> tempLocaleIdToPluralRanges = new HashMap<Object, PluralRanges>();
        for (String[] stringArray : pluralRangeData) {
            if (stringArray[0].equals("locales")) {
                if (pr != null) {
                    pr.freeze();
                    for (String locale : locales) {
                        tempLocaleIdToPluralRanges.put(locale, pr);
                    }
                }
                locales = stringArray[1].split(" ");
                pr = new PluralRanges();
                continue;
            }
            pr.add(StandardPlural.fromString(stringArray[0]), StandardPlural.fromString(stringArray[1]), StandardPlural.fromString(stringArray[2]));
        }
        for (String[] stringArray : locales) {
            tempLocaleIdToPluralRanges.put(stringArray, pr);
        }
        localeIdToPluralRanges = Collections.unmodifiableMap(tempLocaleIdToPluralRanges);
    }
}

