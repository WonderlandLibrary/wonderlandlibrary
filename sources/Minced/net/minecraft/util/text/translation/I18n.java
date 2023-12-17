// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.text.translation;

@Deprecated
public class I18n
{
    private static final LanguageMap localizedName;
    private static final LanguageMap fallbackTranslator;
    
    @Deprecated
    public static String translateToLocal(final String key) {
        return I18n.localizedName.translateKey(key);
    }
    
    @Deprecated
    public static String translateToLocalFormatted(final String key, final Object... format) {
        return I18n.localizedName.translateKeyFormat(key, format);
    }
    
    @Deprecated
    public static String translateToFallback(final String key) {
        return I18n.fallbackTranslator.translateKey(key);
    }
    
    @Deprecated
    public static boolean canTranslate(final String key) {
        return I18n.localizedName.isKeyTranslated(key);
    }
    
    public static long getLastTranslationUpdateTimeInMilliseconds() {
        return I18n.localizedName.getLastUpdateTimeInMilliseconds();
    }
    
    static {
        localizedName = LanguageMap.getInstance();
        fallbackTranslator = new LanguageMap();
    }
}
