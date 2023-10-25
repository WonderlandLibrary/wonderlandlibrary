/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.resources;

import net.minecraft.client.resources.Locale;

public class I18n {
    private static Locale i18nLocale;

    public static String format(String string, Object ... objectArray) {
        return i18nLocale.formatMessage(string, objectArray);
    }

    static void setLocale(Locale locale) {
        i18nLocale = locale;
    }
}

