/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.text.translation;

import net.minecraft.util.text.translation.LanguageMap;

@Deprecated
public class I18n {
    private static final LanguageMap localizedName = LanguageMap.getInstance();
    private static final LanguageMap fallbackTranslator = new LanguageMap();

    @Deprecated
    public static String translateToLocal(String key) {
        return localizedName.translateKey(key);
    }

    @Deprecated
    public static String translateToLocalFormatted(String key, Object ... format) {
        return localizedName.translateKeyFormat(key, format);
    }

    @Deprecated
    public static String translateToFallback(String key) {
        return fallbackTranslator.translateKey(key);
    }

    @Deprecated
    public static boolean canTranslate(String key) {
        return localizedName.isKeyTranslated(key);
    }

    public static long getLastTranslationUpdateTimeInMilliseconds() {
        return localizedName.getLastUpdateTimeInMilliseconds();
    }
}

