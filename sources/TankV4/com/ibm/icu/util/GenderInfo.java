package com.ibm.icu.util;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

public class GenderInfo {
   private final GenderInfo.ListGenderStyle style;
   private static GenderInfo neutral;
   private static GenderInfo.Cache genderInfoCache;

   public static GenderInfo getInstance(ULocale var0) {
      return genderInfoCache.get(var0);
   }

   public static GenderInfo getInstance(Locale var0) {
      return getInstance(ULocale.forLocale(var0));
   }

   public GenderInfo.Gender getListGender(GenderInfo.Gender... var1) {
      return this.getListGender(Arrays.asList(var1));
   }

   public GenderInfo.Gender getListGender(List var1) {
      if (var1.size() == 0) {
         return GenderInfo.Gender.OTHER;
      } else if (var1.size() == 1) {
         return (GenderInfo.Gender)var1.get(0);
      } else {
         Iterator var4;
         GenderInfo.Gender var5;
         switch(this.style) {
         case NEUTRAL:
            return GenderInfo.Gender.OTHER;
         case MIXED_NEUTRAL:
            boolean var2 = false;
            boolean var3 = false;
            var4 = var1.iterator();

            while(var4.hasNext()) {
               var5 = (GenderInfo.Gender)var4.next();
               switch(var5) {
               case FEMALE:
                  if (var3) {
                     return GenderInfo.Gender.OTHER;
                  }

                  var2 = true;
                  break;
               case MALE:
                  if (var2) {
                     return GenderInfo.Gender.OTHER;
                  }

                  var3 = true;
                  break;
               case OTHER:
                  return GenderInfo.Gender.OTHER;
               }
            }

            return var3 ? GenderInfo.Gender.MALE : GenderInfo.Gender.FEMALE;
         case MALE_TAINTS:
            var4 = var1.iterator();

            do {
               if (!var4.hasNext()) {
                  return GenderInfo.Gender.FEMALE;
               }

               var5 = (GenderInfo.Gender)var4.next();
            } while(var5 == GenderInfo.Gender.FEMALE);

            return GenderInfo.Gender.MALE;
         default:
            return GenderInfo.Gender.OTHER;
         }
      }
   }

   public GenderInfo(GenderInfo.ListGenderStyle var1) {
      this.style = var1;
   }

   static GenderInfo access$000() {
      return neutral;
   }

   static {
      neutral = new GenderInfo(GenderInfo.ListGenderStyle.NEUTRAL);
      genderInfoCache = new GenderInfo.Cache();
   }

   private static class Cache {
      private final ICUCache cache;

      private Cache() {
         this.cache = new SimpleCache();
      }

      public GenderInfo get(ULocale var1) {
         GenderInfo var2 = (GenderInfo)this.cache.get(var1);
         if (var2 == null) {
            var2 = load(var1);
            if (var2 == null) {
               ULocale var3 = var1.getFallback();
               var2 = var3 == null ? GenderInfo.access$000() : this.get(var3);
            }

            this.cache.put(var1, var2);
         }

         return var2;
      }

      private static GenderInfo load(ULocale var0) {
         UResourceBundle var1 = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "genderList", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
         UResourceBundle var2 = var1.get("genderList");

         try {
            return new GenderInfo(GenderInfo.ListGenderStyle.fromName(var2.getString(var0.toString())));
         } catch (MissingResourceException var4) {
            return null;
         }
      }

      Cache(Object var1) {
         this();
      }
   }

   public static enum ListGenderStyle {
      NEUTRAL,
      MIXED_NEUTRAL,
      MALE_TAINTS;

      private static Map fromNameMap = new HashMap(3);
      private static final GenderInfo.ListGenderStyle[] $VALUES = new GenderInfo.ListGenderStyle[]{NEUTRAL, MIXED_NEUTRAL, MALE_TAINTS};

      public static GenderInfo.ListGenderStyle fromName(String var0) {
         GenderInfo.ListGenderStyle var1 = (GenderInfo.ListGenderStyle)fromNameMap.get(var0);
         if (var1 == null) {
            throw new IllegalArgumentException("Unknown gender style name: " + var0);
         } else {
            return var1;
         }
      }

      static {
         fromNameMap.put("neutral", NEUTRAL);
         fromNameMap.put("maleTaints", MALE_TAINTS);
         fromNameMap.put("mixedNeutral", MIXED_NEUTRAL);
      }
   }

   public static enum Gender {
      MALE,
      FEMALE,
      OTHER;

      private static final GenderInfo.Gender[] $VALUES = new GenderInfo.Gender[]{MALE, FEMALE, OTHER};
   }
}
