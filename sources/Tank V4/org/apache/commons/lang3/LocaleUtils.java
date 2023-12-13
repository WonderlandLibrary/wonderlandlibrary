package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtils {
   private static final ConcurrentMap cLanguagesByCountry = new ConcurrentHashMap();
   private static final ConcurrentMap cCountriesByLanguage = new ConcurrentHashMap();

   public static Locale toLocale(String var0) {
      if (var0 == null) {
         return null;
      } else if (var0.isEmpty()) {
         return new Locale("", "");
      } else if (var0.contains("#")) {
         throw new IllegalArgumentException("Invalid locale format: " + var0);
      } else {
         int var1 = var0.length();
         if (var1 < 2) {
            throw new IllegalArgumentException("Invalid locale format: " + var0);
         } else {
            char var2 = var0.charAt(0);
            if (var2 == '_') {
               if (var1 < 3) {
                  throw new IllegalArgumentException("Invalid locale format: " + var0);
               } else {
                  char var5 = var0.charAt(1);
                  char var6 = var0.charAt(2);
                  if (Character.isUpperCase(var5) && Character.isUpperCase(var6)) {
                     if (var1 == 3) {
                        return new Locale("", var0.substring(1, 3));
                     } else if (var1 < 5) {
                        throw new IllegalArgumentException("Invalid locale format: " + var0);
                     } else if (var0.charAt(3) != '_') {
                        throw new IllegalArgumentException("Invalid locale format: " + var0);
                     } else {
                        return new Locale("", var0.substring(1, 3), var0.substring(4));
                     }
                  } else {
                     throw new IllegalArgumentException("Invalid locale format: " + var0);
                  }
               }
            } else {
               String[] var3 = var0.split("_", -1);
               int var4 = var3.length - 1;
               switch(var4) {
               case 0:
                  if (!StringUtils.isAllLowerCase(var0) || var1 != 2 && var1 != 3) {
                     throw new IllegalArgumentException("Invalid locale format: " + var0);
                  }

                  return new Locale(var0);
               case 1:
                  if (StringUtils.isAllLowerCase(var3[0]) && (var3[0].length() == 2 || var3[0].length() == 3) && var3[1].length() == 2 && StringUtils.isAllUpperCase(var3[1])) {
                     return new Locale(var3[0], var3[1]);
                  }

                  throw new IllegalArgumentException("Invalid locale format: " + var0);
               case 2:
                  if (StringUtils.isAllLowerCase(var3[0]) && (var3[0].length() == 2 || var3[0].length() == 3) && (var3[1].length() == 0 || var3[1].length() == 2 && StringUtils.isAllUpperCase(var3[1])) && var3[2].length() > 0) {
                     return new Locale(var3[0], var3[1], var3[2]);
                  }
               default:
                  throw new IllegalArgumentException("Invalid locale format: " + var0);
               }
            }
         }
      }
   }

   public static List localeLookupList(Locale var0) {
      return localeLookupList(var0, var0);
   }

   public static List localeLookupList(Locale var0, Locale var1) {
      ArrayList var2 = new ArrayList(4);
      if (var0 != null) {
         var2.add(var0);
         if (var0.getVariant().length() > 0) {
            var2.add(new Locale(var0.getLanguage(), var0.getCountry()));
         }

         if (var0.getCountry().length() > 0) {
            var2.add(new Locale(var0.getLanguage(), ""));
         }

         if (!var2.contains(var1)) {
            var2.add(var1);
         }
      }

      return Collections.unmodifiableList(var2);
   }

   public static List availableLocaleList() {
      return LocaleUtils.SyncAvoid.access$000();
   }

   public static Set availableLocaleSet() {
      return LocaleUtils.SyncAvoid.access$100();
   }

   public static boolean isAvailableLocale(Locale var0) {
      return availableLocaleList().contains(var0);
   }

   public static List languagesByCountry(String var0) {
      if (var0 == null) {
         return Collections.emptyList();
      } else {
         List var1 = (List)cLanguagesByCountry.get(var0);
         if (var1 == null) {
            ArrayList var5 = new ArrayList();
            List var2 = availableLocaleList();

            for(int var3 = 0; var3 < var2.size(); ++var3) {
               Locale var4 = (Locale)var2.get(var3);
               if (var0.equals(var4.getCountry()) && var4.getVariant().isEmpty()) {
                  var5.add(var4);
               }
            }

            var1 = Collections.unmodifiableList(var5);
            cLanguagesByCountry.putIfAbsent(var0, var1);
            var1 = (List)cLanguagesByCountry.get(var0);
         }

         return var1;
      }
   }

   public static List countriesByLanguage(String var0) {
      if (var0 == null) {
         return Collections.emptyList();
      } else {
         List var1 = (List)cCountriesByLanguage.get(var0);
         if (var1 == null) {
            ArrayList var5 = new ArrayList();
            List var2 = availableLocaleList();

            for(int var3 = 0; var3 < var2.size(); ++var3) {
               Locale var4 = (Locale)var2.get(var3);
               if (var0.equals(var4.getLanguage()) && var4.getCountry().length() != 0 && var4.getVariant().isEmpty()) {
                  var5.add(var4);
               }
            }

            var1 = Collections.unmodifiableList(var5);
            cCountriesByLanguage.putIfAbsent(var0, var1);
            var1 = (List)cCountriesByLanguage.get(var0);
         }

         return var1;
      }
   }

   static class SyncAvoid {
      private static final List AVAILABLE_LOCALE_LIST;
      private static final Set AVAILABLE_LOCALE_SET;

      static List access$000() {
         return AVAILABLE_LOCALE_LIST;
      }

      static Set access$100() {
         return AVAILABLE_LOCALE_SET;
      }

      static {
         ArrayList var0 = new ArrayList(Arrays.asList(Locale.getAvailableLocales()));
         AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(var0);
         AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet(var0));
      }
   }
}
