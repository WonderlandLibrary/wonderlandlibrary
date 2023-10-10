// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.translation;

import java.util.Locale;
import java.util.function.Supplier;

final class TranslationLocales
{
    private static final Supplier<Locale> GLOBAL;
    
    private TranslationLocales() {
    }
    
    static Locale global() {
        return TranslationLocales.GLOBAL.get();
    }
    
    static {
        final String property = System.getProperty("net.kyo".concat("ri.adventure.defaultTranslationLocale"));
        if (property == null || property.isEmpty()) {
            GLOBAL = (() -> Locale.US);
        }
        else if (property.equals("system")) {
            GLOBAL = Locale::getDefault;
        }
        else {
            final Locale locale = Translator.parseLocale(property);
            GLOBAL = (() -> locale);
        }
    }
}
