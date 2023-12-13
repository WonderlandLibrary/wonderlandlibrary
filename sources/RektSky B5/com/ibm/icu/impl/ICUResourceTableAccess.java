/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.LocaleIDs;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

public class ICUResourceTableAccess {
    public static String getTableString(String path, ULocale locale, String tableName, String itemName, String defaultValue) {
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(path, locale.getBaseName());
        return ICUResourceTableAccess.getTableString(bundle, tableName, null, itemName, defaultValue);
    }

    public static String getTableString(ICUResourceBundle bundle, String tableName, String subtableName, String item, String defaultValue) {
        String result = null;
        try {
            while (true) {
                String fallbackLocale;
                ICUResourceBundle table;
                if ((table = bundle.findWithFallback(tableName)) == null) {
                    return defaultValue;
                }
                ICUResourceBundle stable = table;
                if (subtableName != null) {
                    stable = table.findWithFallback(subtableName);
                }
                if (stable != null && (result = stable.findStringWithFallback(item)) != null) break;
                if (subtableName == null) {
                    String currentName = null;
                    if (tableName.equals("Countries")) {
                        currentName = LocaleIDs.getCurrentCountryID(item);
                    } else if (tableName.equals("Languages")) {
                        currentName = LocaleIDs.getCurrentLanguageID(item);
                    }
                    if (currentName != null && (result = table.findStringWithFallback(currentName)) != null) break;
                }
                if ((fallbackLocale = table.findStringWithFallback("Fallback")) == null) {
                    return defaultValue;
                }
                if (fallbackLocale.length() == 0) {
                    fallbackLocale = "root";
                }
                if (fallbackLocale.equals(table.getULocale().getName())) {
                    return defaultValue;
                }
                bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(bundle.getBaseName(), fallbackLocale);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return result != null && result.length() > 0 ? result : defaultValue;
    }
}

