package com.ibm.icu.text;

import com.ibm.icu.impl.CurrencyData;
import com.ibm.icu.util.ULocale;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class CurrencyPluralInfo implements Cloneable, Serializable {
   private static final long serialVersionUID = 1L;
   private static final char[] tripleCurrencySign = new char[]{'¤', '¤', '¤'};
   private static final String tripleCurrencyStr;
   private static final char[] defaultCurrencyPluralPatternChar;
   private static final String defaultCurrencyPluralPattern;
   private Map pluralCountToCurrencyUnitPattern = null;
   private PluralRules pluralRules = null;
   private ULocale ulocale = null;
   static final boolean $assertionsDisabled = !CurrencyPluralInfo.class.desiredAssertionStatus();

   public CurrencyPluralInfo() {
      this.initialize(ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public CurrencyPluralInfo(Locale var1) {
      this.initialize(ULocale.forLocale(var1));
   }

   public CurrencyPluralInfo(ULocale var1) {
      this.initialize(var1);
   }

   public static CurrencyPluralInfo getInstance() {
      return new CurrencyPluralInfo();
   }

   public static CurrencyPluralInfo getInstance(Locale var0) {
      return new CurrencyPluralInfo(var0);
   }

   public static CurrencyPluralInfo getInstance(ULocale var0) {
      return new CurrencyPluralInfo(var0);
   }

   public PluralRules getPluralRules() {
      return this.pluralRules;
   }

   public String getCurrencyPluralPattern(String var1) {
      String var2 = (String)this.pluralCountToCurrencyUnitPattern.get(var1);
      if (var2 == null) {
         if (!var1.equals("other")) {
            var2 = (String)this.pluralCountToCurrencyUnitPattern.get("other");
         }

         if (var2 == null) {
            var2 = defaultCurrencyPluralPattern;
         }
      }

      return var2;
   }

   public ULocale getLocale() {
      return this.ulocale;
   }

   public void setPluralRules(String var1) {
      this.pluralRules = PluralRules.createRules(var1);
   }

   public void setCurrencyPluralPattern(String var1, String var2) {
      this.pluralCountToCurrencyUnitPattern.put(var1, var2);
   }

   public void setLocale(ULocale var1) {
      this.ulocale = var1;
      this.initialize(var1);
   }

   public Object clone() {
      try {
         CurrencyPluralInfo var1 = (CurrencyPluralInfo)super.clone();
         var1.ulocale = (ULocale)this.ulocale.clone();
         var1.pluralCountToCurrencyUnitPattern = new HashMap();
         Iterator var2 = this.pluralCountToCurrencyUnitPattern.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            String var4 = (String)this.pluralCountToCurrencyUnitPattern.get(var3);
            var1.pluralCountToCurrencyUnitPattern.put(var3, var4);
         }

         return var1;
      } catch (CloneNotSupportedException var5) {
         throw new IllegalStateException();
      }
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof CurrencyPluralInfo)) {
         return false;
      } else {
         CurrencyPluralInfo var2 = (CurrencyPluralInfo)var1;
         return this.pluralRules.equals(var2.pluralRules) && this.pluralCountToCurrencyUnitPattern.equals(var2.pluralCountToCurrencyUnitPattern);
      }
   }

   /** @deprecated */
   public int hashCode() {
      if (!$assertionsDisabled) {
         throw new AssertionError("hashCode not designed");
      } else {
         return 42;
      }
   }

   String select(double var1) {
      return this.pluralRules.select(var1);
   }

   Iterator pluralPatternIterator() {
      return this.pluralCountToCurrencyUnitPattern.keySet().iterator();
   }

   private void initialize(ULocale var1) {
      this.ulocale = var1;
      this.pluralRules = PluralRules.forLocale(var1);
      this.setupCurrencyPluralPattern(var1);
   }

   private void setupCurrencyPluralPattern(ULocale var1) {
      this.pluralCountToCurrencyUnitPattern = new HashMap();
      String var2 = NumberFormat.getPattern((ULocale)var1, 0);
      int var3 = var2.indexOf(";");
      String var4 = null;
      if (var3 != -1) {
         var4 = var2.substring(var3 + 1);
         var2 = var2.substring(0, var3);
      }

      Map var5 = CurrencyData.provider.getInstance(var1, true).getUnitPatterns();

      String var8;
      String var11;
      for(Iterator var6 = var5.entrySet().iterator(); var6.hasNext(); this.pluralCountToCurrencyUnitPattern.put(var8, var11)) {
         Entry var7 = (Entry)var6.next();
         var8 = (String)var7.getKey();
         String var9 = (String)var7.getValue();
         String var10 = var9.replace("{0}", var2);
         var11 = var10.replace("{1}", tripleCurrencyStr);
         if (var3 != -1) {
            String var13 = var9.replace("{0}", var4);
            String var14 = var13.replace("{1}", tripleCurrencyStr);
            StringBuilder var15 = new StringBuilder(var11);
            var15.append(";");
            var15.append(var14);
            var11 = var15.toString();
         }
      }

   }

   static {
      tripleCurrencyStr = new String(tripleCurrencySign);
      defaultCurrencyPluralPatternChar = new char[]{'\u0000', '.', '#', '#', ' ', '¤', '¤', '¤'};
      defaultCurrencyPluralPattern = new String(defaultCurrencyPluralPatternChar);
   }
}
