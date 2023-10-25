/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.util.ULocale;
import java.util.Map;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class CurrencyDisplayNames {
    public static CurrencyDisplayNames getInstance(ULocale locale) {
        return CurrencyData.provider.getInstance(locale, true);
    }

    public static CurrencyDisplayNames getInstance(ULocale locale, boolean noSubstitute) {
        return CurrencyData.provider.getInstance(locale, !noSubstitute);
    }

    public static boolean hasData() {
        return CurrencyData.provider.hasData();
    }

    public abstract ULocale getULocale();

    public abstract String getSymbol(String var1);

    public abstract String getName(String var1);

    public abstract String getPluralName(String var1, String var2);

    public abstract Map<String, String> symbolMap();

    public abstract Map<String, String> nameMap();

    protected CurrencyDisplayNames() {
    }
}

