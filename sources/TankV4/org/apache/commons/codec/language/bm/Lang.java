package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class Lang {
   private static final Map Langs = new EnumMap(NameType.class);
   private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
   private final Languages languages;
   private final List rules;

   public static Lang instance(NameType var0) {
      return (Lang)Langs.get(var0);
   }

   public static Lang loadFromResource(String var0, Languages var1) {
      ArrayList var2 = new ArrayList();
      InputStream var3 = Lang.class.getClassLoader().getResourceAsStream(var0);
      if (var3 == null) {
         throw new IllegalStateException("Unable to resolve required resource:org/apache/commons/codec/language/bm/lang.txt");
      } else {
         Scanner var4 = new Scanner(var3, "UTF-8");
         boolean var5 = false;

         while(var4.hasNextLine()) {
            String var6 = var4.nextLine();
            String var7 = var6;
            if (var5) {
               if (var6.endsWith("*/")) {
                  var5 = false;
               }
            } else if (var6.startsWith("/*")) {
               var5 = true;
            } else {
               int var8 = var6.indexOf("//");
               if (var8 >= 0) {
                  var7 = var6.substring(0, var8);
               }

               var7 = var7.trim();
               if (var7.length() != 0) {
                  String[] var9 = var7.split("\\s+");
                  if (var9.length != 3) {
                     throw new IllegalArgumentException("Malformed line '" + var6 + "' in language resource '" + var0 + "'");
                  }

                  Pattern var10 = Pattern.compile(var9[0]);
                  String[] var11 = var9[1].split("\\+");
                  boolean var12 = var9[2].equals("true");
                  var2.add(new Lang.LangRule(var10, new HashSet(Arrays.asList(var11)), var12));
               }
            }
         }

         var4.close();
         return new Lang(var2, var1);
      }
   }

   private Lang(List var1, Languages var2) {
      this.rules = Collections.unmodifiableList(var1);
      this.languages = var2;
   }

   public String guessLanguage(String var1) {
      Languages.LanguageSet var2 = this.guessLanguages(var1);
      return var2.isSingleton() ? var2.getAny() : "any";
   }

   public Languages.LanguageSet guessLanguages(String var1) {
      String var2 = var1.toLowerCase(Locale.ENGLISH);
      HashSet var3 = new HashSet(this.languages.getLanguages());
      Iterator var4 = this.rules.iterator();

      while(var4.hasNext()) {
         Lang.LangRule var5 = (Lang.LangRule)var4.next();
         if (var5.matches(var2)) {
            if (Lang.LangRule.access$100(var5)) {
               var3.retainAll(Lang.LangRule.access$200(var5));
            } else {
               var3.removeAll(Lang.LangRule.access$200(var5));
            }
         }
      }

      Languages.LanguageSet var6 = Languages.LanguageSet.from(var3);
      return var6.equals(Languages.NO_LANGUAGES) ? Languages.ANY_LANGUAGE : var6;
   }

   static {
      NameType[] var0 = NameType.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         NameType var3 = var0[var2];
         Langs.put(var3, loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(var3)));
      }

   }

   private static final class LangRule {
      private final boolean acceptOnMatch;
      private final Set languages;
      private final Pattern pattern;

      private LangRule(Pattern var1, Set var2, boolean var3) {
         this.pattern = var1;
         this.languages = var2;
         this.acceptOnMatch = var3;
      }

      public boolean matches(String var1) {
         return this.pattern.matcher(var1).find();
      }

      LangRule(Pattern var1, Set var2, boolean var3, Object var4) {
         this(var1, var2, var3);
      }

      static boolean access$100(Lang.LangRule var0) {
         return var0.acceptOnMatch;
      }

      static Set access$200(Lang.LangRule var0) {
         return var0.languages;
      }
   }
}
