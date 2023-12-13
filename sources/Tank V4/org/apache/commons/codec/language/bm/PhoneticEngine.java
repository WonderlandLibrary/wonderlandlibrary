package org.apache.commons.codec.language.bm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class PhoneticEngine {
   private static final Map NAME_PREFIXES = new EnumMap(NameType.class);
   private static final int DEFAULT_MAX_PHONEMES = 20;
   private final Lang lang;
   private final NameType nameType;
   private final RuleType ruleType;
   private final boolean concat;
   private final int maxPhonemes;

   private static String join(Iterable var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      Iterator var3 = var0.iterator();
      if (var3.hasNext()) {
         var2.append((String)var3.next());
      }

      while(var3.hasNext()) {
         var2.append(var1).append((String)var3.next());
      }

      return var2.toString();
   }

   public PhoneticEngine(NameType var1, RuleType var2, boolean var3) {
      this(var1, var2, var3, 20);
   }

   public PhoneticEngine(NameType var1, RuleType var2, boolean var3, int var4) {
      if (var2 == RuleType.RULES) {
         throw new IllegalArgumentException("ruleType must not be " + RuleType.RULES);
      } else {
         this.nameType = var1;
         this.ruleType = var2;
         this.concat = var3;
         this.lang = Lang.instance(var1);
         this.maxPhonemes = var4;
      }
   }

   private PhoneticEngine.PhonemeBuilder applyFinalRules(PhoneticEngine.PhonemeBuilder var1, Map var2) {
      if (var2 == null) {
         throw new NullPointerException("finalRules can not be null");
      } else if (var2.isEmpty()) {
         return var1;
      } else {
         TreeSet var3 = new TreeSet(Rule.Phoneme.COMPARATOR);
         Iterator var4 = var1.getPhonemes().iterator();

         while(var4.hasNext()) {
            Rule.Phoneme var5 = (Rule.Phoneme)var4.next();
            PhoneticEngine.PhonemeBuilder var6 = PhoneticEngine.PhonemeBuilder.empty(var5.getLanguages());
            String var7 = var5.getPhonemeText().toString();

            PhoneticEngine.RulesApplication var9;
            for(int var8 = 0; var8 < var7.length(); var8 = var9.getI()) {
               var9 = (new PhoneticEngine.RulesApplication(var2, var7, var6, var8, this.maxPhonemes)).invoke();
               boolean var10 = var9.isFound();
               var6 = var9.getPhonemeBuilder();
               if (!var10) {
                  var6.append(var7.subSequence(var8, var8 + 1));
               }
            }

            var3.addAll(var6.getPhonemes());
         }

         return new PhoneticEngine.PhonemeBuilder(var3);
      }
   }

   public String encode(String var1) {
      Languages.LanguageSet var2 = this.lang.guessLanguages(var1);
      return this.encode(var1, var2);
   }

   public String encode(String var1, Languages.LanguageSet var2) {
      Map var3 = Rule.getInstanceMap(this.nameType, RuleType.RULES, var2);
      Map var4 = Rule.getInstanceMap(this.nameType, this.ruleType, "common");
      Map var5 = Rule.getInstanceMap(this.nameType, this.ruleType, var2);
      var1 = var1.toLowerCase(Locale.ENGLISH).replace('-', ' ').trim();
      String var9;
      if (this.nameType == NameType.GENERIC) {
         String var7;
         if (var1.length() >= 2 && var1.substring(0, 2).equals("d'")) {
            String var13 = var1.substring(2);
            var7 = "d" + var13;
            return "(" + this.encode(var13) + ")-(" + this.encode(var7) + ")";
         }

         Iterator var6 = ((Set)NAME_PREFIXES.get(this.nameType)).iterator();

         while(var6.hasNext()) {
            var7 = (String)var6.next();
            if (var1.startsWith(var7 + " ")) {
               String var8 = var1.substring(var7.length() + 1);
               var9 = var7 + var8;
               return "(" + this.encode(var8) + ")-(" + this.encode(var9) + ")";
            }
         }
      }

      List var12 = Arrays.asList(var1.split("\\s+"));
      ArrayList var14 = new ArrayList();
      switch(this.nameType) {
      case SEPHARDIC:
         Iterator var15 = var12.iterator();

         while(var15.hasNext()) {
            var9 = (String)var15.next();
            String[] var10 = var9.split("'");
            String var11 = var10[var10.length - 1];
            var14.add(var11);
         }

         var14.removeAll((Collection)NAME_PREFIXES.get(this.nameType));
         break;
      case ASHKENAZI:
         var14.addAll(var12);
         var14.removeAll((Collection)NAME_PREFIXES.get(this.nameType));
         break;
      case GENERIC:
         var14.addAll(var12);
         break;
      default:
         throw new IllegalStateException("Unreachable case: " + this.nameType);
      }

      if (this.concat) {
         var1 = join(var14, " ");
      } else {
         if (var14.size() != 1) {
            StringBuilder var18 = new StringBuilder();
            Iterator var19 = var14.iterator();

            while(var19.hasNext()) {
               String var21 = (String)var19.next();
               var18.append("-").append(this.encode(var21));
            }

            return var18.substring(1);
         }

         var1 = (String)var12.iterator().next();
      }

      PhoneticEngine.PhonemeBuilder var16 = PhoneticEngine.PhonemeBuilder.empty(var2);

      PhoneticEngine.RulesApplication var20;
      for(int var17 = 0; var17 < var1.length(); var16 = var20.getPhonemeBuilder()) {
         var20 = (new PhoneticEngine.RulesApplication(var3, var1, var16, var17, this.maxPhonemes)).invoke();
         var17 = var20.getI();
      }

      var16 = this.applyFinalRules(var16, var4);
      var16 = this.applyFinalRules(var16, var5);
      return var16.makeString();
   }

   public Lang getLang() {
      return this.lang;
   }

   public NameType getNameType() {
      return this.nameType;
   }

   public RuleType getRuleType() {
      return this.ruleType;
   }

   public boolean isConcat() {
      return this.concat;
   }

   public int getMaxPhonemes() {
      return this.maxPhonemes;
   }

   static {
      NAME_PREFIXES.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet(Arrays.asList("bar", "ben", "da", "de", "van", "von"))));
      NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet(Arrays.asList("al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
      NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet(Arrays.asList("da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"))));
   }

   private static final class RulesApplication {
      private final Map finalRules;
      private final CharSequence input;
      private PhoneticEngine.PhonemeBuilder phonemeBuilder;
      private int i;
      private final int maxPhonemes;
      private boolean found;

      public RulesApplication(Map var1, CharSequence var2, PhoneticEngine.PhonemeBuilder var3, int var4, int var5) {
         if (var1 == null) {
            throw new NullPointerException("The finalRules argument must not be null");
         } else {
            this.finalRules = var1;
            this.phonemeBuilder = var3;
            this.input = var2;
            this.i = var4;
            this.maxPhonemes = var5;
         }
      }

      public int getI() {
         return this.i;
      }

      public PhoneticEngine.PhonemeBuilder getPhonemeBuilder() {
         return this.phonemeBuilder;
      }

      public PhoneticEngine.RulesApplication invoke() {
         this.found = false;
         int var1 = 1;
         List var2 = (List)this.finalRules.get(this.input.subSequence(this.i, this.i + var1));
         if (var2 != null) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Rule var4 = (Rule)var3.next();
               String var5 = var4.getPattern();
               var1 = var5.length();
               if (var4.patternAndContextMatches(this.input, this.i)) {
                  this.phonemeBuilder.apply(var4.getPhoneme(), this.maxPhonemes);
                  this.found = true;
                  break;
               }
            }
         }

         if (!this.found) {
            var1 = 1;
         }

         this.i += var1;
         return this;
      }

      public boolean isFound() {
         return this.found;
      }
   }

   static final class PhonemeBuilder {
      private final Set phonemes;

      public static PhoneticEngine.PhonemeBuilder empty(Languages.LanguageSet var0) {
         return new PhoneticEngine.PhonemeBuilder(new Rule.Phoneme("", var0));
      }

      private PhonemeBuilder(Rule.Phoneme var1) {
         this.phonemes = new LinkedHashSet();
         this.phonemes.add(var1);
      }

      private PhonemeBuilder(Set var1) {
         this.phonemes = var1;
      }

      public void append(CharSequence var1) {
         Iterator var2 = this.phonemes.iterator();

         while(var2.hasNext()) {
            Rule.Phoneme var3 = (Rule.Phoneme)var2.next();
            var3.append(var1);
         }

      }

      public void apply(Rule.PhonemeExpr var1, int var2) {
         LinkedHashSet var3 = new LinkedHashSet(var2);
         Iterator var4 = this.phonemes.iterator();

         label25:
         while(var4.hasNext()) {
            Rule.Phoneme var5 = (Rule.Phoneme)var4.next();
            Iterator var6 = var1.getPhonemes().iterator();

            while(var6.hasNext()) {
               Rule.Phoneme var7 = (Rule.Phoneme)var6.next();
               Languages.LanguageSet var8 = var5.getLanguages().restrictTo(var7.getLanguages());
               if (!var8.isEmpty()) {
                  Rule.Phoneme var9 = new Rule.Phoneme(var5, var7, var8);
                  if (var3.size() < var2) {
                     var3.add(var9);
                     if (var3.size() >= var2) {
                        break label25;
                     }
                  }
               }
            }
         }

         this.phonemes.clear();
         this.phonemes.addAll(var3);
      }

      public Set getPhonemes() {
         return this.phonemes;
      }

      public String makeString() {
         StringBuilder var1 = new StringBuilder();

         Rule.Phoneme var3;
         for(Iterator var2 = this.phonemes.iterator(); var2.hasNext(); var1.append(var3.getPhonemeText())) {
            var3 = (Rule.Phoneme)var2.next();
            if (var1.length() > 0) {
               var1.append("|");
            }
         }

         return var1.toString();
      }

      PhonemeBuilder(Set var1, Object var2) {
         this(var1);
      }
   }
}
