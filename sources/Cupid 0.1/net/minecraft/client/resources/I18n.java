package net.minecraft.client.resources;

import java.util.Map;

public class I18n {
  private static Locale i18nLocale;
  
  private static final String __OBFID = "CL_00001094";
  
  static void setLocale(Locale i18nLocaleIn) {
    i18nLocale = i18nLocaleIn;
  }
  
  public static String format(String translateKey, Object... parameters) {
    return i18nLocale.formatMessage(translateKey, parameters);
  }
  
  public static Map getLocaleProperties() {
    return i18nLocale.properties;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\resources\I18n.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */