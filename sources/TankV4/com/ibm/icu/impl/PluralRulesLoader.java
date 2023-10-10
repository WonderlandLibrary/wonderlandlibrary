package com.ibm.icu.impl;

import com.ibm.icu.text.PluralRules;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeMap;

public class PluralRulesLoader {
   private final Map rulesIdToRules = new HashMap();
   private Map localeIdToCardinalRulesId;
   private Map localeIdToOrdinalRulesId;
   private Map rulesIdToEquivalentULocale;
   public static final PluralRulesLoader loader = new PluralRulesLoader();

   private PluralRulesLoader() {
   }

   public ULocale[] getAvailableULocales() {
      Set var1 = this.getLocaleIdToRulesIdMap(PluralRules.PluralType.CARDINAL).keySet();
      ULocale[] var2 = new ULocale[var1.size()];
      int var3 = 0;

      for(Iterator var4 = var1.iterator(); var4.hasNext(); var2[var3++] = ULocale.createCanonical((String)var4.next())) {
      }

      return var2;
   }

   public ULocale getFunctionalEquivalent(ULocale var1, boolean[] var2) {
      String var3;
      if (var2 != null && var2.length > 0) {
         var3 = ULocale.canonicalize(var1.getBaseName());
         Map var4 = this.getLocaleIdToRulesIdMap(PluralRules.PluralType.CARDINAL);
         var2[0] = var4.containsKey(var3);
      }

      var3 = this.getRulesIdForLocale(var1, PluralRules.PluralType.CARDINAL);
      if (var3 != null && var3.trim().length() != 0) {
         ULocale var5 = (ULocale)this.getRulesIdToEquivalentULocaleMap().get(var3);
         return var5 == null ? ULocale.ROOT : var5;
      } else {
         return ULocale.ROOT;
      }
   }

   private Map getLocaleIdToRulesIdMap(PluralRules.PluralType var1) {
      this.checkBuildRulesIdMaps();
      return var1 == PluralRules.PluralType.CARDINAL ? this.localeIdToCardinalRulesId : this.localeIdToOrdinalRulesId;
   }

   private Map getRulesIdToEquivalentULocaleMap() {
      this.checkBuildRulesIdMaps();
      return this.rulesIdToEquivalentULocale;
   }

   private void checkBuildRulesIdMaps() {
      synchronized(this){}
      boolean var1 = this.localeIdToCardinalRulesId != null;
      if (!var1) {
         Object var2;
         Object var3;
         Object var4;
         try {
            UResourceBundle var5 = this.getPluralBundle();
            UResourceBundle var6 = var5.get("locales");
            var2 = new TreeMap();
            var4 = new HashMap();

            int var7;
            UResourceBundle var8;
            String var9;
            String var10;
            for(var7 = 0; var7 < var6.getSize(); ++var7) {
               var8 = var6.get(var7);
               var9 = var8.getKey();
               var10 = var8.getString().intern();
               ((Map)var2).put(var9, var10);
               if (!((Map)var4).containsKey(var10)) {
                  ((Map)var4).put(var10, new ULocale(var9));
               }
            }

            var6 = var5.get("locales_ordinals");
            var3 = new TreeMap();

            for(var7 = 0; var7 < var6.getSize(); ++var7) {
               var8 = var6.get(var7);
               var9 = var8.getKey();
               var10 = var8.getString().intern();
               ((Map)var3).put(var9, var10);
            }
         } catch (MissingResourceException var12) {
            var2 = Collections.emptyMap();
            var3 = Collections.emptyMap();
            var4 = Collections.emptyMap();
         }

         synchronized(this){}
         if (this.localeIdToCardinalRulesId == null) {
            this.localeIdToCardinalRulesId = (Map)var2;
            this.localeIdToOrdinalRulesId = (Map)var3;
            this.rulesIdToEquivalentULocale = (Map)var4;
         }
      }

   }

   public String getRulesIdForLocale(ULocale var1, PluralRules.PluralType var2) {
      Map var3 = this.getLocaleIdToRulesIdMap(var2);
      String var4 = ULocale.canonicalize(var1.getBaseName());

      String var5;
      int var6;
      for(var5 = null; null == (var5 = (String)var3.get(var4)); var4 = var4.substring(0, var6)) {
         var6 = var4.lastIndexOf("_");
         if (var6 == -1) {
            break;
         }
      }

      return var5;
   }

   public PluralRules getRulesForRulesId(String var1) {
      PluralRules var2 = null;
      Map var4;
      synchronized(var4 = this.rulesIdToRules){}
      boolean var3 = this.rulesIdToRules.containsKey(var1);
      if (var3) {
         var2 = (PluralRules)this.rulesIdToRules.get(var1);
      }

      if (!var3) {
         try {
            UResourceBundle var13 = this.getPluralBundle();
            UResourceBundle var5 = var13.get("rules");
            UResourceBundle var6 = var5.get(var1);
            StringBuilder var7 = new StringBuilder();

            for(int var8 = 0; var8 < var6.getSize(); ++var8) {
               UResourceBundle var9 = var6.get(var8);
               if (var8 > 0) {
                  var7.append("; ");
               }

               var7.append(var9.getKey());
               var7.append(": ");
               var7.append(var9.getString());
            }

            var2 = PluralRules.parseDescription(var7.toString());
         } catch (ParseException var11) {
         } catch (MissingResourceException var12) {
         }

         synchronized(var4 = this.rulesIdToRules){}
         if (this.rulesIdToRules.containsKey(var1)) {
            var2 = (PluralRules)this.rulesIdToRules.get(var1);
         } else {
            this.rulesIdToRules.put(var1, var2);
         }
      }

      return var2;
   }

   public UResourceBundle getPluralBundle() throws MissingResourceException {
      return ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "plurals", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
   }

   public PluralRules forLocale(ULocale var1, PluralRules.PluralType var2) {
      String var3 = this.getRulesIdForLocale(var1, var2);
      if (var3 != null && var3.trim().length() != 0) {
         PluralRules var4 = this.getRulesForRulesId(var3);
         if (var4 == null) {
            var4 = PluralRules.DEFAULT;
         }

         return var4;
      } else {
         return PluralRules.DEFAULT;
      }
   }
}
