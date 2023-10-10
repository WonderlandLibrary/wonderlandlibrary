package com.ibm.icu.util;

import com.ibm.icu.impl.Row;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocaleMatcher {
   private static final boolean DEBUG = false;
   private static final double DEFAULT_THRESHOLD = 0.5D;
   private final ULocale defaultLanguage;
   Map maximizedLanguageToWeight;
   LocaleMatcher.LanguageMatcherData matcherData;
   private static LocaleMatcher.LanguageMatcherData defaultWritten = LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$100(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000(LocaleMatcher.LanguageMatcherData.access$000((new LocaleMatcher.LanguageMatcherData()).addDistance("no", "nb", 100, "The language no is normally taken as nb in content; we might alias this for lookup."), "nn", "nb", 96), "nn", "no", 96).addDistance("da", "no", 90, "Danish and norwegian are reasonably close."), "da", "nb", 90).addDistance("hr", "br", 96, "Serbo-croatian variants are all very close."), "sh", "br", 96), "sr", "br", 96), "sh", "hr", 96), "sr", "hr", 96), "sh", "sr", 96).addDistance("sr-Latn", "sr-Cyrl", 90, "Most serbs can read either script."), "*-Hans", "*-Hant", 85, true, "Readers of simplified can read traditional much better than reverse.").addDistance("*-Hant", "*-Hans", 75, true).addDistance("en-*-US", "en-*-CA", 98, "US is different than others, and Canadian is inbetween."), "en-*-US", "en-*-*", 97), "en-*-CA", "en-*-*", 98), "en-*-*", "en-*-*", 99).addDistance("es-*-ES", "es-*-ES", 100, "Latin American Spanishes are closer to each other. Approximate by having es-ES be further from everything else."), "es-*-ES", "es-*-*", 93).addDistance("*", "*", 1, "[Default value -- must be at end!] Normally there is no comprehension of different languages.").addDistance("*-*", "*-*", 20, "[Default value -- must be at end!] Normally there is little comprehension of different scripts.").addDistance("*-*-*", "*-*-*", 96, "[Default value -- must be at end!] Normally there are small differences across regions.").freeze();
   private static HashMap canonicalMap = new HashMap();

   public LocaleMatcher(LocalePriorityList var1) {
      this(var1, defaultWritten);
   }

   public LocaleMatcher(String var1) {
      this(LocalePriorityList.add(var1).build());
   }

   /** @deprecated */
   public LocaleMatcher(LocalePriorityList var1, LocaleMatcher.LanguageMatcherData var2) {
      this.maximizedLanguageToWeight = new LinkedHashMap();
      this.matcherData = var2;
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         ULocale var4 = (ULocale)var3.next();
         this.add(var4, var1.getWeight(var4));
      }

      var3 = var1.iterator();
      this.defaultLanguage = var3.hasNext() ? (ULocale)var3.next() : null;
   }

   public double match(ULocale var1, ULocale var2, ULocale var3, ULocale var4) {
      return this.matcherData.match(var1, var2, var3, var4);
   }

   public ULocale canonicalize(ULocale var1) {
      String var2 = var1.getLanguage();
      String var3 = (String)canonicalMap.get(var2);
      String var4 = var1.getScript();
      String var5 = (String)canonicalMap.get(var4);
      String var6 = var1.getCountry();
      String var7 = (String)canonicalMap.get(var6);
      return var3 == null && var5 == null && var7 == null ? var1 : new ULocale(var3 == null ? var2 : var3, var5 == null ? var4 : var5, var7 == null ? var6 : var7);
   }

   public ULocale getBestMatch(LocalePriorityList var1) {
      double var2 = 0.0D;
      ULocale var4 = null;
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         ULocale var6 = (ULocale)var5.next();
         Row.R2 var7 = this.getBestMatchInternal(var6);
         double var8 = (Double)var7.get1() * var1.getWeight(var6);
         if (var8 > var2) {
            var2 = var8;
            var4 = (ULocale)var7.get0();
         }
      }

      if (var2 < 0.5D) {
         var4 = this.defaultLanguage;
      }

      return var4;
   }

   public ULocale getBestMatch(String var1) {
      return this.getBestMatch(LocalePriorityList.add(var1).build());
   }

   public ULocale getBestMatch(ULocale var1) {
      return (ULocale)this.getBestMatchInternal(var1).get0();
   }

   public String toString() {
      return "{" + this.defaultLanguage + ", " + this.maximizedLanguageToWeight + "}";
   }

   private Row.R2 getBestMatchInternal(ULocale var1) {
      var1 = this.canonicalize(var1);
      ULocale var2 = this.addLikelySubtags(var1);
      double var3 = 0.0D;
      ULocale var5 = null;
      Iterator var6 = this.maximizedLanguageToWeight.keySet().iterator();

      while(var6.hasNext()) {
         ULocale var7 = (ULocale)var6.next();
         Row.R2 var8 = (Row.R2)this.maximizedLanguageToWeight.get(var7);
         double var9 = this.match(var1, var2, var7, (ULocale)var8.get0());
         double var11 = var9 * (Double)var8.get1();
         if (var11 > var3) {
            var3 = var11;
            var5 = var7;
         }
      }

      if (var3 < 0.5D) {
         var5 = this.defaultLanguage;
      }

      return Row.R2.of(var5, var3);
   }

   private void add(ULocale var1, Double var2) {
      var1 = this.canonicalize(var1);
      Row.R2 var3 = Row.of(this.addLikelySubtags(var1), var2);
      this.maximizedLanguageToWeight.put(var1, var3);
   }

   private ULocale addLikelySubtags(ULocale var1) {
      ULocale var2 = ULocale.addLikelySubtags(var1);
      if (var2 != null && !var2.equals(var1)) {
         return var2;
      } else {
         String var3 = var1.getLanguage();
         String var4 = var1.getScript();
         String var5 = var1.getCountry();
         return new ULocale((var3.length() == 0 ? "und" : var3) + "_" + (var4.length() == 0 ? "Zzzz" : var4) + "_" + (var5.length() == 0 ? "ZZ" : var5));
      }
   }

   static {
      canonicalMap.put("iw", "he");
      canonicalMap.put("mo", "ro");
      canonicalMap.put("tl", "fil");
   }

   /** @deprecated */
   public static class LanguageMatcherData implements Freezable {
      LocaleMatcher.ScoreData languageScores;
      LocaleMatcher.ScoreData scriptScores;
      LocaleMatcher.ScoreData regionScores;
      private boolean frozen;

      /** @deprecated */
      public LanguageMatcherData() {
         this.languageScores = new LocaleMatcher.ScoreData(LocaleMatcher.Level.language);
         this.scriptScores = new LocaleMatcher.ScoreData(LocaleMatcher.Level.script);
         this.regionScores = new LocaleMatcher.ScoreData(LocaleMatcher.Level.region);
         this.frozen = false;
      }

      /** @deprecated */
      public double match(ULocale var1, ULocale var2, ULocale var3, ULocale var4) {
         double var5 = 0.0D;
         var5 += this.languageScores.getScore(var1, var2, var1.getLanguage(), var2.getLanguage(), var3, var4, var3.getLanguage(), var4.getLanguage());
         var5 += this.scriptScores.getScore(var1, var2, var1.getScript(), var2.getScript(), var3, var4, var3.getScript(), var4.getScript());
         var5 += this.regionScores.getScore(var1, var2, var1.getCountry(), var2.getCountry(), var3, var4, var3.getCountry(), var4.getCountry());
         if (!var1.getVariant().equals(var3.getVariant())) {
            ++var5;
         }

         if (var5 < 0.0D) {
            var5 = 0.0D;
         } else if (var5 > 1.0D) {
            var5 = 1.0D;
         }

         return 1.0D - var5;
      }

      /** @deprecated */
      private LocaleMatcher.LanguageMatcherData addDistance(String var1, String var2, int var3) {
         return this.addDistance(var1, var2, var3, false, (String)null);
      }

      /** @deprecated */
      public LocaleMatcher.LanguageMatcherData addDistance(String var1, String var2, int var3, String var4) {
         return this.addDistance(var1, var2, var3, false, var4);
      }

      /** @deprecated */
      public LocaleMatcher.LanguageMatcherData addDistance(String var1, String var2, int var3, boolean var4) {
         return this.addDistance(var1, var2, var3, var4, (String)null);
      }

      private LocaleMatcher.LanguageMatcherData addDistance(String var1, String var2, int var3, boolean var4, String var5) {
         double var6 = 1.0D - (double)var3 / 100.0D;
         LocaleMatcher.LocalePatternMatcher var8 = new LocaleMatcher.LocalePatternMatcher(var1);
         LocaleMatcher.Level var9 = var8.getLevel();
         LocaleMatcher.LocalePatternMatcher var10 = new LocaleMatcher.LocalePatternMatcher(var2);
         LocaleMatcher.Level var11 = var10.getLevel();
         if (var9 != var11) {
            throw new IllegalArgumentException();
         } else {
            Row.R3 var12 = Row.of(var8, var10, var6);
            Row.R3 var13 = var4 ? null : Row.of(var10, var8, var6);
            switch(var9) {
            case language:
               String var14 = var8.getLanguage();
               String var15 = var10.getLanguage();
               this.languageScores.addDataToScores(var14, var15, var12);
               if (!var4) {
                  this.languageScores.addDataToScores(var15, var14, var13);
               }
               break;
            case script:
               String var16 = var8.getScript();
               String var17 = var10.getScript();
               this.scriptScores.addDataToScores(var16, var17, var12);
               if (!var4) {
                  this.scriptScores.addDataToScores(var17, var16, var13);
               }
               break;
            case region:
               String var18 = var8.getRegion();
               String var19 = var10.getRegion();
               this.regionScores.addDataToScores(var18, var19, var12);
               if (!var4) {
                  this.regionScores.addDataToScores(var19, var18, var13);
               }
            }

            return this;
         }
      }

      /** @deprecated */
      public LocaleMatcher.LanguageMatcherData cloneAsThawed() {
         try {
            LocaleMatcher.LanguageMatcherData var1 = (LocaleMatcher.LanguageMatcherData)this.clone();
            var1.languageScores = this.languageScores.cloneAsThawed();
            var1.scriptScores = this.scriptScores.cloneAsThawed();
            var1.regionScores = this.regionScores.cloneAsThawed();
            var1.frozen = false;
            return var1;
         } catch (CloneNotSupportedException var3) {
            throw new IllegalArgumentException(var3);
         }
      }

      /** @deprecated */
      public LocaleMatcher.LanguageMatcherData freeze() {
         return this;
      }

      /** @deprecated */
      public boolean isFrozen() {
         return this.frozen;
      }

      public Object cloneAsThawed() {
         return this.cloneAsThawed();
      }

      public Object freeze() {
         return this.freeze();
      }

      static LocaleMatcher.LanguageMatcherData access$000(LocaleMatcher.LanguageMatcherData var0, String var1, String var2, int var3) {
         return var0.addDistance(var1, var2, var3);
      }

      static LocaleMatcher.LanguageMatcherData access$100(LocaleMatcher.LanguageMatcherData var0, String var1, String var2, int var3, boolean var4, String var5) {
         return var0.addDistance(var1, var2, var3, var4, var5);
      }
   }

   private static class ScoreData implements Freezable {
      LinkedHashSet scores = new LinkedHashSet();
      final double worst;
      final LocaleMatcher.Level level;
      private boolean frozen = false;

      public ScoreData(LocaleMatcher.Level var1) {
         this.level = var1;
         this.worst = (double)(1 - (var1 == LocaleMatcher.Level.language ? 90 : (var1 == LocaleMatcher.Level.script ? 20 : 4))) / 100.0D;
      }

      void addDataToScores(String var1, String var2, Row.R3 var3) {
         this.scores.add(var3);
      }

      double getScore(ULocale var1, ULocale var2, String var3, String var4, ULocale var5, ULocale var6, String var7, String var8) {
         boolean var9 = var3.equals(var4);
         boolean var10 = var7.equals(var8);
         double var11;
         if (!var4.equals(var8)) {
            var11 = this.getRawScore(var2, var6);
            if (var9 == var10) {
               var11 *= 0.75D;
            } else if (var9) {
               var11 *= 0.5D;
            }
         } else if (var9 == var10) {
            var11 = 0.0D;
         } else {
            var11 = 0.25D * this.worst;
         }

         return var11;
      }

      private double getRawScore(ULocale var1, ULocale var2) {
         Iterator var3 = this.scores.iterator();

         Row.R3 var4;
         do {
            if (!var3.hasNext()) {
               return this.worst;
            }

            var4 = (Row.R3)var3.next();
         } while(!((LocaleMatcher.LocalePatternMatcher)var4.get0()).matches(var1) || !((LocaleMatcher.LocalePatternMatcher)var4.get1()).matches(var2));

         return (Double)var4.get2();
      }

      public String toString() {
         return this.level + ", " + this.scores;
      }

      public LocaleMatcher.ScoreData cloneAsThawed() {
         try {
            LocaleMatcher.ScoreData var1 = (LocaleMatcher.ScoreData)this.clone();
            var1.scores = (LinkedHashSet)var1.scores.clone();
            var1.frozen = false;
            return var1;
         } catch (CloneNotSupportedException var2) {
            throw new IllegalArgumentException(var2);
         }
      }

      public LocaleMatcher.ScoreData freeze() {
         return this;
      }

      public boolean isFrozen() {
         return this.frozen;
      }

      public Object cloneAsThawed() {
         return this.cloneAsThawed();
      }

      public Object freeze() {
         return this.freeze();
      }
   }

   static enum Level {
      language,
      script,
      region;

      private static final LocaleMatcher.Level[] $VALUES = new LocaleMatcher.Level[]{language, script, region};
   }

   private static class LocalePatternMatcher {
      private String lang;
      private String script;
      private String region;
      private LocaleMatcher.Level level;
      static Pattern pattern = Pattern.compile("([a-zA-Z]{1,8}|\\*)(?:-([a-zA-Z]{4}|\\*))?(?:-([a-zA-Z]{2}|[0-9]{3}|\\*))?");

      public LocalePatternMatcher(String var1) {
         Matcher var2 = pattern.matcher(var1);
         if (!var2.matches()) {
            throw new IllegalArgumentException("Bad pattern: " + var1);
         } else {
            this.lang = var2.group(1);
            this.script = var2.group(2);
            this.region = var2.group(3);
            this.level = this.region != null ? LocaleMatcher.Level.region : (this.script != null ? LocaleMatcher.Level.script : LocaleMatcher.Level.language);
            if (this.lang.equals("*")) {
               this.lang = null;
            }

            if (this.script != null && this.script.equals("*")) {
               this.script = null;
            }

            if (this.region != null && this.region.equals("*")) {
               this.region = null;
            }

         }
      }

      boolean matches(ULocale var1) {
         if (this.lang != null && !this.lang.equals(var1.getLanguage())) {
            return false;
         } else if (this.script != null && !this.script.equals(var1.getScript())) {
            return false;
         } else {
            return this.region == null || this.region.equals(var1.getCountry());
         }
      }

      public LocaleMatcher.Level getLevel() {
         return this.level;
      }

      public String getLanguage() {
         return this.lang == null ? "*" : this.lang;
      }

      public String getScript() {
         return this.script == null ? "*" : this.script;
      }

      public String getRegion() {
         return this.region == null ? "*" : this.region;
      }

      public String toString() {
         String var1 = this.getLanguage();
         if (this.level != LocaleMatcher.Level.language) {
            var1 = var1 + "-" + this.getScript();
            if (this.level != LocaleMatcher.Level.script) {
               var1 = var1 + "-" + this.getRegion();
            }
         }

         return var1;
      }
   }
}
