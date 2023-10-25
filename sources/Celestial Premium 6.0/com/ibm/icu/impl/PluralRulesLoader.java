/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
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

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class PluralRulesLoader {
    private final Map<String, PluralRules> rulesIdToRules = new HashMap<String, PluralRules>();
    private Map<String, String> localeIdToCardinalRulesId;
    private Map<String, String> localeIdToOrdinalRulesId;
    private Map<String, ULocale> rulesIdToEquivalentULocale;
    public static final PluralRulesLoader loader = new PluralRulesLoader();

    private PluralRulesLoader() {
    }

    public ULocale[] getAvailableULocales() {
        Set<String> keys = this.getLocaleIdToRulesIdMap(PluralRules.PluralType.CARDINAL).keySet();
        ULocale[] locales = new ULocale[keys.size()];
        int n = 0;
        Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            locales[n++] = ULocale.createCanonical(iter.next());
        }
        return locales;
    }

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
                UResourceBundle b;
                int i;
                UResourceBundle pluralb = this.getPluralBundle();
                UResourceBundle localeb = pluralb.get("locales");
                tempLocaleIdToCardinalRulesId = new TreeMap();
                tempRulesIdToEquivalentULocale = new HashMap();
                for (i = 0; i < localeb.getSize(); ++i) {
                    b = localeb.get(i);
                    id = b.getKey();
                    value = b.getString().intern();
                    tempLocaleIdToCardinalRulesId.put(id, value);
                    if (tempRulesIdToEquivalentULocale.containsKey(value)) continue;
                    tempRulesIdToEquivalentULocale.put(value, new ULocale(id));
                }
                localeb = pluralb.get("locales_ordinals");
                tempLocaleIdToOrdinalRulesId = new TreeMap();
                for (i = 0; i < localeb.getSize(); ++i) {
                    b = localeb.get(i);
                    id = b.getKey();
                    value = b.getString().intern();
                    tempLocaleIdToOrdinalRulesId.put(id, value);
                }
            }
            catch (MissingResourceException e) {
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
                for (int i = 0; i < setb.getSize(); ++i) {
                    UResourceBundle b = setb.get(i);
                    if (i > 0) {
                        sb.append("; ");
                    }
                    sb.append(b.getKey());
                    sb.append(": ");
                    sb.append(b.getString());
                }
                rules = PluralRules.parseDescription(sb.toString());
            }
            catch (ParseException e) {
            }
            catch (MissingResourceException e) {
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
        return ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "plurals", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
    }

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
}

