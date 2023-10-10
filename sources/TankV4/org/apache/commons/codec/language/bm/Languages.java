package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class Languages {
   public static final String ANY = "any";
   private static final Map LANGUAGES = new EnumMap(NameType.class);
   private final Set languages;
   public static final Languages.LanguageSet NO_LANGUAGES;
   public static final Languages.LanguageSet ANY_LANGUAGE;

   public static Languages getInstance(NameType var0) {
      return (Languages)LANGUAGES.get(var0);
   }

   public static Languages getInstance(String var0) {
      HashSet var1 = new HashSet();
      InputStream var2 = Languages.class.getClassLoader().getResourceAsStream(var0);
      if (var2 == null) {
         throw new IllegalArgumentException("Unable to resolve required resource: " + var0);
      } else {
         Scanner var3 = new Scanner(var2, "UTF-8");
         boolean var4 = false;

         while(var3.hasNextLine()) {
            String var5 = var3.nextLine().trim();
            if (var4) {
               if (var5.endsWith("*/")) {
                  var4 = false;
               }
            } else if (var5.startsWith("/*")) {
               var4 = true;
            } else if (var5.length() > 0) {
               var1.add(var5);
            }
         }

         var3.close();
         return new Languages(Collections.unmodifiableSet(var1));
      }
   }

   private static String langResourceName(NameType var0) {
      return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", var0.getName());
   }

   private Languages(Set var1) {
      this.languages = var1;
   }

   public Set getLanguages() {
      return this.languages;
   }

   static {
      NameType[] var0 = NameType.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         NameType var3 = var0[var2];
         LANGUAGES.put(var3, getInstance(langResourceName(var3)));
      }

      NO_LANGUAGES = new Languages.LanguageSet() {
         public boolean contains(String var1) {
            return false;
         }

         public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the empty language set.");
         }

         public boolean isEmpty() {
            return true;
         }

         public boolean isSingleton() {
            return false;
         }

         public Languages.LanguageSet restrictTo(Languages.LanguageSet var1) {
            return this;
         }

         public String toString() {
            return "NO_LANGUAGES";
         }
      };
      ANY_LANGUAGE = new Languages.LanguageSet() {
         public boolean contains(String var1) {
            return true;
         }

         public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the any language set.");
         }

         public boolean isEmpty() {
            return false;
         }

         public boolean isSingleton() {
            return false;
         }

         public Languages.LanguageSet restrictTo(Languages.LanguageSet var1) {
            return var1;
         }

         public String toString() {
            return "ANY_LANGUAGE";
         }
      };
   }

   public static final class SomeLanguages extends Languages.LanguageSet {
      private final Set languages;

      private SomeLanguages(Set var1) {
         this.languages = Collections.unmodifiableSet(var1);
      }

      public boolean contains(String var1) {
         return this.languages.contains(var1);
      }

      public String getAny() {
         return (String)this.languages.iterator().next();
      }

      public Set getLanguages() {
         return this.languages;
      }

      public boolean isEmpty() {
         return this.languages.isEmpty();
      }

      public boolean isSingleton() {
         return this.languages.size() == 1;
      }

      public Languages.LanguageSet restrictTo(Languages.LanguageSet var1) {
         if (var1 == Languages.NO_LANGUAGES) {
            return var1;
         } else if (var1 == Languages.ANY_LANGUAGE) {
            return this;
         } else {
            Languages.SomeLanguages var2 = (Languages.SomeLanguages)var1;
            HashSet var3 = new HashSet(Math.min(this.languages.size(), var2.languages.size()));
            Iterator var4 = this.languages.iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               if (var2.languages.contains(var5)) {
                  var3.add(var5);
               }
            }

            return from(var3);
         }
      }

      public String toString() {
         return "Languages(" + this.languages.toString() + ")";
      }

      SomeLanguages(Set var1, Object var2) {
         this(var1);
      }
   }

   public abstract static class LanguageSet {
      public static Languages.LanguageSet from(Set var0) {
         return (Languages.LanguageSet)(var0.isEmpty() ? Languages.NO_LANGUAGES : new Languages.SomeLanguages(var0));
      }

      public abstract boolean contains(String var1);

      public abstract String getAny();

      public abstract boolean isEmpty();

      public abstract boolean isSingleton();

      public abstract Languages.LanguageSet restrictTo(Languages.LanguageSet var1);
   }
}
