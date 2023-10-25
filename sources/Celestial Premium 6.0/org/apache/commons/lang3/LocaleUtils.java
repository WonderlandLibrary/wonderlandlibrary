/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.StringUtils;

public class LocaleUtils {
    private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap<String, List<Locale>>();
    private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap<String, List<Locale>>();

    public static Locale toLocale(String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return new Locale("", "");
        }
        if (str.contains("#")) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        int len = str.length();
        if (len < 2) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch0 = str.charAt(0);
        if (ch0 == '_') {
            if (len < 3) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            char ch1 = str.charAt(1);
            char ch2 = str.charAt(2);
            if (!Character.isUpperCase(ch1) || !Character.isUpperCase(ch2)) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 3) {
                return new Locale("", str.substring(1, 3));
            }
            if (len < 5) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (str.charAt(3) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            return new Locale("", str.substring(1, 3), str.substring(4));
        }
        String[] split = str.split("_", -1);
        int occurrences = split.length - 1;
        switch (occurrences) {
            case 0: {
                if (StringUtils.isAllLowerCase(str) && (len == 2 || len == 3)) {
                    return new Locale(str);
                }
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            case 1: {
                if (StringUtils.isAllLowerCase(split[0]) && (split[0].length() == 2 || split[0].length() == 3) && split[1].length() == 2 && StringUtils.isAllUpperCase(split[1])) {
                    return new Locale(split[0], split[1]);
                }
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            case 2: {
                if (!StringUtils.isAllLowerCase(split[0]) || split[0].length() != 2 && split[0].length() != 3 || split[1].length() != 0 && (split[1].length() != 2 || !StringUtils.isAllUpperCase(split[1])) || split[2].length() <= 0) break;
                return new Locale(split[0], split[1], split[2]);
            }
        }
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    public static List<Locale> localeLookupList(Locale locale) {
        return LocaleUtils.localeLookupList(locale, locale);
    }

    public static List<Locale> localeLookupList(Locale locale, Locale defaultLocale) {
        ArrayList<Locale> list = new ArrayList<Locale>(4);
        if (locale != null) {
            list.add(locale);
            if (locale.getVariant().length() > 0) {
                list.add(new Locale(locale.getLanguage(), locale.getCountry()));
            }
            if (locale.getCountry().length() > 0) {
                list.add(new Locale(locale.getLanguage(), ""));
            }
            if (!list.contains(defaultLocale)) {
                list.add(defaultLocale);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public static List<Locale> availableLocaleList() {
        return SyncAvoid.AVAILABLE_LOCALE_LIST;
    }

    public static Set<Locale> availableLocaleSet() {
        return SyncAvoid.AVAILABLE_LOCALE_SET;
    }

    public static boolean isAvailableLocale(Locale locale) {
        return LocaleUtils.availableLocaleList().contains(locale);
    }

    public static List<Locale> languagesByCountry(String countryCode) {
        if (countryCode == null) {
            return Collections.emptyList();
        }
        List<Locale> langs = (ArrayList)cLanguagesByCountry.get(countryCode);
        if (langs == null) {
            langs = new ArrayList();
            List<Locale> locales = LocaleUtils.availableLocaleList();
            for (int i = 0; i < locales.size(); ++i) {
                Locale locale = locales.get(i);
                if (!countryCode.equals(locale.getCountry()) || !locale.getVariant().isEmpty()) continue;
                langs.add(locale);
            }
            langs = Collections.unmodifiableList(langs);
            cLanguagesByCountry.putIfAbsent(countryCode, langs);
            langs = (List)cLanguagesByCountry.get(countryCode);
        }
        return langs;
    }

    public static List<Locale> countriesByLanguage(String languageCode) {
        if (languageCode == null) {
            return Collections.emptyList();
        }
        List<Locale> countries = (ArrayList)cCountriesByLanguage.get(languageCode);
        if (countries == null) {
            countries = new ArrayList();
            List<Locale> locales = LocaleUtils.availableLocaleList();
            for (int i = 0; i < locales.size(); ++i) {
                Locale locale = locales.get(i);
                if (!languageCode.equals(locale.getLanguage()) || locale.getCountry().length() == 0 || !locale.getVariant().isEmpty()) continue;
                countries.add(locale);
            }
            countries = Collections.unmodifiableList(countries);
            cCountriesByLanguage.putIfAbsent(languageCode, countries);
            countries = (List)cCountriesByLanguage.get(languageCode);
        }
        return countries;
    }

    static class SyncAvoid {
        private static final List<Locale> AVAILABLE_LOCALE_LIST;
        private static final Set<Locale> AVAILABLE_LOCALE_SET;

        SyncAvoid() {
        }

        static {
            ArrayList<Locale> list = new ArrayList<Locale>(Arrays.asList(Locale.getAvailableLocales()));
            AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(list);
            AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet<Locale>(list));
        }
    }
}

