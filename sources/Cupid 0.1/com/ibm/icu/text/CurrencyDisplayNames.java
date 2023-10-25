package com.ibm.icu.text;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.util.ULocale;
import java.util.Map;

public abstract class CurrencyDisplayNames {
  public static CurrencyDisplayNames getInstance(ULocale locale) {
    return (CurrencyDisplayNames)CurrencyData.provider.getInstance(locale, true);
  }
  
  public static CurrencyDisplayNames getInstance(ULocale locale, boolean noSubstitute) {
    return (CurrencyDisplayNames)CurrencyData.provider.getInstance(locale, !noSubstitute);
  }
  
  public static boolean hasData() {
    return CurrencyData.provider.hasData();
  }
  
  public abstract ULocale getULocale();
  
  public abstract String getSymbol(String paramString);
  
  public abstract String getName(String paramString);
  
  public abstract String getPluralName(String paramString1, String paramString2);
  
  public abstract Map<String, String> symbolMap();
  
  public abstract Map<String, String> nameMap();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CurrencyDisplayNames.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */