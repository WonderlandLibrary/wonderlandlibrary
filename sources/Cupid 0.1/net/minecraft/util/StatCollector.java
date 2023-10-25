package net.minecraft.util;

public class StatCollector {
  private static StringTranslate localizedName = StringTranslate.getInstance();
  
  private static StringTranslate fallbackTranslator = new StringTranslate();
  
  public static String translateToLocal(String key) {
    return localizedName.translateKey(key);
  }
  
  public static String translateToLocalFormatted(String key, Object... format) {
    return localizedName.translateKeyFormat(key, format);
  }
  
  public static String translateToFallback(String key) {
    return fallbackTranslator.translateKey(key);
  }
  
  public static boolean canTranslate(String key) {
    return localizedName.isKeyTranslated(key);
  }
  
  public static long getLastTranslationUpdateTimeInMilliseconds() {
    return localizedName.getLastUpdateTimeInMilliseconds();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\StatCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */