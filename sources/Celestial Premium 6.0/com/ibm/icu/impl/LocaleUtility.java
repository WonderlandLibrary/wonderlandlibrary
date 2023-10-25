/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import java.util.Locale;

public class LocaleUtility {
    public static Locale getLocaleFromName(String name) {
        String language = "";
        String country = "";
        String variant = "";
        int i1 = name.indexOf(95);
        if (i1 < 0) {
            language = name;
        } else {
            int i2;
            language = name.substring(0, i1);
            if ((i2 = name.indexOf(95, ++i1)) < 0) {
                country = name.substring(i1);
            } else {
                country = name.substring(i1, i2);
                variant = name.substring(i2 + 1);
            }
        }
        return new Locale(language, country, variant);
    }

    public static boolean isFallbackOf(String parent, String child) {
        if (!child.startsWith(parent)) {
            return false;
        }
        int i = parent.length();
        return i == child.length() || child.charAt(i) == '_';
    }

    public static boolean isFallbackOf(Locale parent, Locale child) {
        return LocaleUtility.isFallbackOf(parent.toString(), child.toString());
    }

    public static Locale fallback(Locale loc) {
        int i;
        String[] parts = new String[]{loc.getLanguage(), loc.getCountry(), loc.getVariant()};
        for (i = 2; i >= 0; --i) {
            if (parts[i].length() == 0) continue;
            parts[i] = "";
            break;
        }
        if (i < 0) {
            return null;
        }
        return new Locale(parts[0], parts[1], parts[2]);
    }
}

