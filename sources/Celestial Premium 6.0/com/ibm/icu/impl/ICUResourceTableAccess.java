/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.LocaleIDs;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

public class ICUResourceTableAccess {
    public static String getTableString(String path, ULocale locale, String tableName, String itemName) {
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(path, locale.getBaseName());
        return ICUResourceTableAccess.getTableString(bundle, tableName, null, itemName);
    }

    public static String getTableString(ICUResourceBundle bundle, String tableName, String subtableName, String item) {
        String result = null;
        try {
            while (true) {
                ICUResourceBundle fbundle;
                ICUResourceBundle sbundle;
                ICUResourceBundle table;
                if ("currency".equals(subtableName)) {
                    table = bundle.getWithFallback("Currencies");
                    table = table.getWithFallback(item);
                    return table.getString(1);
                }
                table = ICUResourceTableAccess.lookup(bundle, tableName);
                if (table == null) {
                    return item;
                }
                ICUResourceBundle stable = table;
                if (subtableName != null) {
                    stable = ICUResourceTableAccess.lookup(table, subtableName);
                }
                if (stable != null && (sbundle = ICUResourceTableAccess.lookup(stable, item)) != null) {
                    result = sbundle.getString();
                    break;
                }
                if (subtableName == null) {
                    String currentName = null;
                    if (tableName.equals("Countries")) {
                        currentName = LocaleIDs.getCurrentCountryID(item);
                    } else if (tableName.equals("Languages")) {
                        currentName = LocaleIDs.getCurrentLanguageID(item);
                    }
                    ICUResourceBundle sbundle2 = ICUResourceTableAccess.lookup(table, currentName);
                    if (sbundle2 != null) {
                        result = sbundle2.getString();
                        break;
                    }
                }
                if ((fbundle = ICUResourceTableAccess.lookup(table, "Fallback")) == null) {
                    return item;
                }
                String fallbackLocale = fbundle.getString();
                if (fallbackLocale.length() == 0) {
                    fallbackLocale = "root";
                }
                if (fallbackLocale.equals(table.getULocale().getName())) {
                    return item;
                }
                bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(bundle.getBaseName(), fallbackLocale);
            }
        }
        catch (Exception e) {
            // empty catch block
        }
        return result != null && result.length() > 0 ? result : item;
    }

    private static ICUResourceBundle lookup(ICUResourceBundle bundle, String resName) {
        return ICUResourceBundle.findResourceWithFallback(resName, bundle, null);
    }
}

