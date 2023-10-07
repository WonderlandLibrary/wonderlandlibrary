// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

public class I18n
{
    private static Locale i18nLocale;
    private static final String __OBFID = "CL_00001094";
    
    static void setLocale(final Locale p_135051_0_) {
        I18n.i18nLocale = p_135051_0_;
    }
    
    public static String format(final String p_135052_0_, final Object... p_135052_1_) {
        return I18n.i18nLocale.formatMessage(p_135052_0_, p_135052_1_);
    }
}
