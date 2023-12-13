package com.ibm.icu.text;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.util.ULocale;
import java.util.Map;

public abstract class CurrencyDisplayNames {
   public static CurrencyDisplayNames getInstance(ULocale var0) {
      return CurrencyData.provider.getInstance(var0, true);
   }

   public static CurrencyDisplayNames getInstance(ULocale var0, boolean var1) {
      return CurrencyData.provider.getInstance(var0, !var1);
   }

   /** @deprecated */
   public static boolean hasData() {
      return CurrencyData.provider.hasData();
   }

   public abstract ULocale getULocale();

   public abstract String getSymbol(String var1);

   public abstract String getName(String var1);

   public abstract String getPluralName(String var1, String var2);

   public abstract Map symbolMap();

   public abstract Map nameMap();

   /** @deprecated */
   protected CurrencyDisplayNames() {
   }
}
