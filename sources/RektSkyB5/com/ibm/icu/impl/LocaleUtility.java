/*
 * Decompiled with CFR 0.152.
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
        int i2 = parent.length();
        return i2 == child.length() || child.charAt(i2) == '_';
    }

    public static boolean isFallbackOf(Locale parent, Locale child) {
        return LocaleUtility.isFallbackOf(parent.toString(), child.toString());
    }

    public static Locale fallback(Locale loc) {
        int i2;
        String[] parts = new String[]{loc.getLanguage(), loc.getCountry(), loc.getVariant()};
        for (i2 = 2; i2 >= 0; --i2) {
            if (parts[i2].length() == 0) continue;
            parts[i2] = "";
            break;
        }
        if (i2 < 0) {
            return null;
        }
        return new Locale(parts[0], parts[1], parts[2]);
    }
}

