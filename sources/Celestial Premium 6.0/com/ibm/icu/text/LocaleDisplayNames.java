/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.LocaleDisplayNamesImpl;
import com.ibm.icu.text.DisplayContext;
import com.ibm.icu.util.ULocale;
import java.util.Locale;

public abstract class LocaleDisplayNames {
    public static LocaleDisplayNames getInstance(ULocale locale) {
        return LocaleDisplayNames.getInstance(locale, DialectHandling.STANDARD_NAMES);
    }

    public static LocaleDisplayNames getInstance(ULocale locale, DialectHandling dialectHandling) {
        return LocaleDisplayNamesImpl.getInstance(locale, dialectHandling);
    }

    public static LocaleDisplayNames getInstance(ULocale locale, DisplayContext ... contexts) {
        return LocaleDisplayNamesImpl.getInstance(locale, contexts);
    }

    public abstract ULocale getLocale();

    public abstract DialectHandling getDialectHandling();

    public abstract DisplayContext getContext(DisplayContext.Type var1);

    public abstract String localeDisplayName(ULocale var1);

    public abstract String localeDisplayName(Locale var1);

    public abstract String localeDisplayName(String var1);

    public abstract String languageDisplayName(String var1);

    public abstract String scriptDisplayName(String var1);

    public String scriptDisplayNameInContext(String script) {
        return this.scriptDisplayName(script);
    }

    public abstract String scriptDisplayName(int var1);

    public abstract String regionDisplayName(String var1);

    public abstract String variantDisplayName(String var1);

    public abstract String keyDisplayName(String var1);

    public abstract String keyValueDisplayName(String var1, String var2);

    protected LocaleDisplayNames() {
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum DialectHandling {
        STANDARD_NAMES,
        DIALECT_NAMES;

    }
}

