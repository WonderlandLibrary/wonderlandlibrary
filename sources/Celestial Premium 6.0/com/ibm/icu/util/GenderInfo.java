/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.util;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class GenderInfo {
    private final ListGenderStyle style;
    private static GenderInfo neutral = new GenderInfo(ListGenderStyle.NEUTRAL);
    private static Cache genderInfoCache = new Cache();

    public static GenderInfo getInstance(ULocale uLocale) {
        return genderInfoCache.get(uLocale);
    }

    public static GenderInfo getInstance(Locale locale) {
        return GenderInfo.getInstance(ULocale.forLocale(locale));
    }

    public Gender getListGender(Gender ... genders) {
        return this.getListGender(Arrays.asList(genders));
    }

    public Gender getListGender(List<Gender> genders) {
        if (genders.size() == 0) {
            return Gender.OTHER;
        }
        if (genders.size() == 1) {
            return genders.get(0);
        }
        switch (this.style) {
            case NEUTRAL: {
                return Gender.OTHER;
            }
            case MIXED_NEUTRAL: {
                boolean hasFemale = false;
                boolean hasMale = false;
                for (Gender gender : genders) {
                    switch (gender) {
                        case FEMALE: {
                            if (hasMale) {
                                return Gender.OTHER;
                            }
                            hasFemale = true;
                            break;
                        }
                        case MALE: {
                            if (hasFemale) {
                                return Gender.OTHER;
                            }
                            hasMale = true;
                            break;
                        }
                        case OTHER: {
                            return Gender.OTHER;
                        }
                    }
                }
                return hasMale ? Gender.MALE : Gender.FEMALE;
            }
            case MALE_TAINTS: {
                for (Gender gender : genders) {
                    if (gender == Gender.FEMALE) continue;
                    return Gender.MALE;
                }
                return Gender.FEMALE;
            }
        }
        return Gender.OTHER;
    }

    public GenderInfo(ListGenderStyle genderStyle) {
        this.style = genderStyle;
    }

    private static class Cache {
        private final ICUCache<ULocale, GenderInfo> cache = new SimpleCache<ULocale, GenderInfo>();

        private Cache() {
        }

        public GenderInfo get(ULocale locale) {
            GenderInfo result = this.cache.get(locale);
            if (result == null) {
                result = Cache.load(locale);
                if (result == null) {
                    ULocale fallback = locale.getFallback();
                    result = fallback == null ? neutral : this.get(fallback);
                }
                this.cache.put(locale, result);
            }
            return result;
        }

        private static GenderInfo load(ULocale ulocale) {
            UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "genderList", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
            UResourceBundle genderList = rb.get("genderList");
            try {
                return new GenderInfo(ListGenderStyle.fromName(genderList.getString(ulocale.toString())));
            }
            catch (MissingResourceException mre) {
                return null;
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum ListGenderStyle {
        NEUTRAL,
        MIXED_NEUTRAL,
        MALE_TAINTS;

        private static Map<String, ListGenderStyle> fromNameMap;

        public static ListGenderStyle fromName(String name) {
            ListGenderStyle result = fromNameMap.get(name);
            if (result == null) {
                throw new IllegalArgumentException("Unknown gender style name: " + name);
            }
            return result;
        }

        static {
            fromNameMap = new HashMap<String, ListGenderStyle>(3);
            fromNameMap.put("neutral", NEUTRAL);
            fromNameMap.put("maleTaints", MALE_TAINTS);
            fromNameMap.put("mixedNeutral", MIXED_NEUTRAL);
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum Gender {
        MALE,
        FEMALE,
        OTHER;

    }
}

