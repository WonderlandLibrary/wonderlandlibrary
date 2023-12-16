// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.util.Map;

public class I18n
{
    private static Locale i18nLocale;
    
    static void setLocale(final Locale p_135051_0_) {
        I18n.i18nLocale = p_135051_0_;
    }
    
    public static String format(final String p_135052_0_, final Object... p_135052_1_) {
        return I18n.i18nLocale.formatMessage(p_135052_0_, p_135052_1_);
    }
    
    public static Map getLocaleProperties() {
        return I18n.i18nLocale.field_135032_a;
    }
}
