// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

public class StatCollector
{
    private static StringTranslate localizedName;
    private static StringTranslate fallbackTranslator;
    private static final String __OBFID = "CL_00001211";
    
    public static String translateToLocal(final String p_74838_0_) {
        return StatCollector.localizedName.translateKey(p_74838_0_);
    }
    
    public static String translateToLocalFormatted(final String p_74837_0_, final Object... p_74837_1_) {
        return StatCollector.localizedName.translateKeyFormat(p_74837_0_, p_74837_1_);
    }
    
    public static String translateToFallback(final String p_150826_0_) {
        return StatCollector.fallbackTranslator.translateKey(p_150826_0_);
    }
    
    public static boolean canTranslate(final String p_94522_0_) {
        return StatCollector.localizedName.isKeyTranslated(p_94522_0_);
    }
    
    public static long getLastTranslationUpdateTimeInMilliseconds() {
        return StatCollector.localizedName.getLastUpdateTimeInMilliseconds();
    }
    
    static {
        StatCollector.localizedName = StringTranslate.getInstance();
        StatCollector.fallbackTranslator = new StringTranslate();
    }
}
