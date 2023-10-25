/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.util.StringTranslate;

public class StatCollector {
    private static StringTranslate fallbackTranslator;
    private static StringTranslate localizedName;

    public static String translateToLocalFormatted(String string, Object ... objectArray) {
        return localizedName.translateKeyFormat(string, objectArray);
    }

    public static String translateToLocal(String string) {
        return localizedName.translateKey(string);
    }

    static {
        localizedName = StringTranslate.getInstance();
        fallbackTranslator = new StringTranslate();
    }

    public static String translateToFallback(String string) {
        return fallbackTranslator.translateKey(string);
    }

    public static long getLastTranslationUpdateTimeInMilliseconds() {
        return localizedName.getLastUpdateTimeInMilliseconds();
    }

    public static boolean canTranslate(String string) {
        return localizedName.isKeyTranslated(string);
    }
}

