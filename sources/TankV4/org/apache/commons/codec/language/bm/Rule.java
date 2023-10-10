package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
   public static final Rule.RPattern ALL_STRINGS_RMATCHER = new Rule.RPattern() {
      public boolean isMatch(CharSequence var1) {
         return true;
      }
   };
   public static final String ALL = "ALL";
   private static final String DOUBLE_QUOTE = "\"";
   private static final String HASH_INCLUDE = "#include";
   private static final Map RULES = new EnumMap(NameType.class);
   private final Rule.RPattern lContext;
   private final String pattern;
   private final Rule.PhonemeExpr phoneme;
   private final Rule.RPattern rContext;

   private static boolean contains(CharSequence var0, char var1) {
      for(int var2 = 0; var2 < var0.length(); ++var2) {
         if (var0.charAt(var2) == var1) {
            return true;
         }
      }

      return false;
   }

   private static String createResourceName(NameType var0, RuleType var1, String var2) {
      return String.format("org/apache/commons/codec/language/bm/%s_%s_%s.txt", var0.getName(), var1.getName(), var2);
   }

   private static Scanner createScanner(NameType var0, RuleType var1, String var2) {
      String var3 = createResourceName(var0, var1, var2);
      InputStream var4 = Languages.class.getClassLoader().getResourceAsStream(var3);
      if (var4 == null) {
         throw new IllegalArgumentException("Unable to load resource: " + var3);
      } else {
         return new Scanner(var4, "UTF-8");
      }
   }

   private static Scanner createScanner(String var0) {
      String var1 = String.format("org/apache/commons/codec/language/bm/%s.txt", var0);
      InputStream var2 = Languages.class.getClassLoader().getResourceAsStream(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Unable to load resource: " + var1);
      } else {
         return new Scanner(var2, "UTF-8");
      }
   }

   private static boolean endsWith(CharSequence var0, CharSequence var1) {
      if (var1.length() > var0.length()) {
         return false;
      } else {
         int var2 = var0.length() - 1;

         for(int var3 = var1.length() - 1; var3 >= 0; --var3) {
            if (var0.charAt(var2) != var1.charAt(var3)) {
               return false;
            }

            --var2;
         }

         return true;
      }
   }

   public static List getInstance(NameType var0, RuleType var1, Languages.LanguageSet var2) {
      Map var3 = getInstanceMap(var0, var1, var2);
      ArrayList var4 = new ArrayList();
      Iterator var5 = var3.values().iterator();

      while(var5.hasNext()) {
         List var6 = (List)var5.next();
         var4.addAll(var6);
      }

      return var4;
   }

   public static List getInstance(NameType var0, RuleType var1, String var2) {
      return getInstance(var0, var1, Languages.LanguageSet.from(new HashSet(Arrays.asList(var2))));
   }

   public static Map getInstanceMap(NameType var0, RuleType var1, Languages.LanguageSet var2) {
      return var2.isSingleton() ? getInstanceMap(var0, var1, var2.getAny()) : getInstanceMap(var0, var1, "any");
   }

   public static Map getInstanceMap(NameType var0, RuleType var1, String var2) {
      Map var3 = (Map)((Map)((Map)RULES.get(var0)).get(var1)).get(var2);
      if (var3 == null) {
         throw new IllegalArgumentException(String.format("No rules found for %s, %s, %s.", var0.getName(), var1.getName(), var2));
      } else {
         return var3;
      }
   }

   private static Rule.Phoneme parsePhoneme(String var0) {
      int var1 = var0.indexOf("[");
      if (var1 >= 0) {
         if (!var0.endsWith("]")) {
            throw new IllegalArgumentException("Phoneme expression contains a '[' but does not end in ']'");
         } else {
            String var2 = var0.substring(0, var1);
            String var3 = var0.substring(var1 + 1, var0.length() - 1);
            HashSet var4 = new HashSet(Arrays.asList(var3.split("[+]")));
            return new Rule.Phoneme(var2, Languages.LanguageSet.from(var4));
         }
      } else {
         return new Rule.Phoneme(var0, Languages.ANY_LANGUAGE);
      }
   }

   private static Rule.PhonemeExpr parsePhonemeExpr(String var0) {
      if (!var0.startsWith("(")) {
         return parsePhoneme(var0);
      } else if (!var0.endsWith(")")) {
         throw new IllegalArgumentException("Phoneme starts with '(' so must end with ')'");
      } else {
         ArrayList var1 = new ArrayList();
         String var2 = var0.substring(1, var0.length() - 1);
         String[] var3 = var2.split("[|]");
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            var1.add(parsePhoneme(var6));
         }

         if (var2.startsWith("|") || var2.endsWith("|")) {
            var1.add(new Rule.Phoneme("", Languages.ANY_LANGUAGE));
         }

         return new Rule.PhonemeList(var1);
      }
   }

   private static Map parseRules(Scanner var0, String var1) {
      HashMap var2 = new HashMap();
      int var3 = 0;
      boolean var4 = false;

      while(var0.hasNextLine()) {
         ++var3;
         String var5 = var0.nextLine();
         String var6 = var5;
         if (var4) {
            if (var5.endsWith("*/")) {
               var4 = false;
            }
         } else if (var5.startsWith("/*")) {
            var4 = true;
         } else {
            int var7 = var5.indexOf("//");
            if (var7 >= 0) {
               var6 = var5.substring(0, var7);
            }

            var6 = var6.trim();
            if (var6.length() != 0) {
               if (var6.startsWith("#include")) {
                  String var8 = var6.substring(8).trim();
                  if (var8.contains(" ")) {
                     throw new IllegalArgumentException("Malformed import statement '" + var5 + "' in " + var1);
                  }

                  var2.putAll(parseRules(createScanner(var8), var1 + "->" + var8));
               } else {
                  String[] var18 = var6.split("\\s+");
                  if (var18.length != 4) {
                     throw new IllegalArgumentException("Malformed rule statement split into " + var18.length + " parts: " + var5 + " in " + var1);
                  }

                  try {
                     String var9 = stripQuotes(var18[0]);
                     String var10 = stripQuotes(var18[1]);
                     String var11 = stripQuotes(var18[2]);
                     Rule.PhonemeExpr var12 = parsePhonemeExpr(stripQuotes(var18[3]));
                     Rule var14 = new Rule(var9, var10, var11, var12, var3, var1) {
                        private final int myLine;
                        private final String loc;
                        final int val$cLine;
                        final String val$location;

                        {
                           this.val$cLine = var5;
                           this.val$location = var6;
                           this.myLine = this.val$cLine;
                           this.loc = this.val$location;
                        }

                        public String toString() {
                           StringBuilder var1 = new StringBuilder();
                           var1.append("Rule");
                           var1.append("{line=").append(this.myLine);
                           var1.append(", loc='").append(this.loc).append('\'');
                           var1.append('}');
                           return var1.toString();
                        }
                     };
                     String var15 = var14.pattern.substring(0, 1);
                     Object var16 = (List)var2.get(var15);
                     if (var16 == null) {
                        var16 = new ArrayList();
                        var2.put(var15, var16);
                     }

                     ((List)var16).add(var14);
                  } catch (IllegalArgumentException var17) {
                     throw new IllegalStateException("Problem parsing line '" + var3 + "' in " + var1, var17);
                  }
               }
            }
         }
      }

      return var2;
   }

   private static Rule.RPattern pattern(String var0) {
      boolean var1 = var0.startsWith("^");
      boolean var2 = var0.endsWith("$");
      String var3 = var0.substring(var1 ? 1 : 0, var2 ? var0.length() - 1 : var0.length());
      boolean var4 = var3.contains("[");
      if (!var4) {
         if (var1 && var2) {
            if (var3.length() == 0) {
               return new Rule.RPattern() {
                  public boolean isMatch(CharSequence var1) {
                     return var1.length() == 0;
                  }
               };
            }

            return new Rule.RPattern(var3) {
               final String val$content;

               {
                  this.val$content = var1;
               }

               public boolean isMatch(CharSequence var1) {
                  return var1.equals(this.val$content);
               }
            };
         }

         if ((var1 || var2) && var3.length() == 0) {
            return ALL_STRINGS_RMATCHER;
         }

         if (var1) {
            return new Rule.RPattern(var3) {
               final String val$content;

               {
                  this.val$content = var1;
               }

               public boolean isMatch(CharSequence var1) {
                  return Rule.access$100(var1, this.val$content);
               }
            };
         }

         if (var2) {
            return new Rule.RPattern(var3) {
               final String val$content;

               {
                  this.val$content = var1;
               }

               public boolean isMatch(CharSequence var1) {
                  return Rule.access$200(var1, this.val$content);
               }
            };
         }
      } else {
         boolean var5 = var3.startsWith("[");
         boolean var6 = var3.endsWith("]");
         if (var5 && var6) {
            String var7 = var3.substring(1, var3.length() - 1);
            if (!var7.contains("[")) {
               boolean var8 = var7.startsWith("^");
               if (var8) {
                  var7 = var7.substring(1);
               }

               boolean var10 = !var8;
               if (var1 && var2) {
                  return new Rule.RPattern(var7, var10) {
                     final String val$bContent;
                     final boolean val$shouldMatch;

                     {
                        this.val$bContent = var1;
                        this.val$shouldMatch = var2;
                     }

                     public boolean isMatch(CharSequence var1) {
                        return var1.length() == 1 && Rule.access$300(this.val$bContent, var1.charAt(0)) == this.val$shouldMatch;
                     }
                  };
               }

               if (var1) {
                  return new Rule.RPattern(var7, var10) {
                     final String val$bContent;
                     final boolean val$shouldMatch;

                     {
                        this.val$bContent = var1;
                        this.val$shouldMatch = var2;
                     }

                     public boolean isMatch(CharSequence var1) {
                        return var1.length() > 0 && Rule.access$300(this.val$bContent, var1.charAt(0)) == this.val$shouldMatch;
                     }
                  };
               }

               if (var2) {
                  return new Rule.RPattern(var7, var10) {
                     final String val$bContent;
                     final boolean val$shouldMatch;

                     {
                        this.val$bContent = var1;
                        this.val$shouldMatch = var2;
                     }

                     public boolean isMatch(CharSequence var1) {
                        return var1.length() > 0 && Rule.access$300(this.val$bContent, var1.charAt(var1.length() - 1)) == this.val$shouldMatch;
                     }
                  };
               }
            }
         }
      }

      return new Rule.RPattern(var0) {
         Pattern pattern;
         final String val$regex;

         {
            this.val$regex = var1;
            this.pattern = Pattern.compile(this.val$regex);
         }

         public boolean isMatch(CharSequence var1) {
            Matcher var2 = this.pattern.matcher(var1);
            return var2.find();
         }
      };
   }

   private static boolean startsWith(CharSequence var0, CharSequence var1) {
      if (var1.length() > var0.length()) {
         return false;
      } else {
         for(int var2 = 0; var2 < var1.length(); ++var2) {
            if (var0.charAt(var2) != var1.charAt(var2)) {
               return false;
            }
         }

         return true;
      }
   }

   private static String stripQuotes(String var0) {
      if (var0.startsWith("\"")) {
         var0 = var0.substring(1);
      }

      if (var0.endsWith("\"")) {
         var0 = var0.substring(0, var0.length() - 1);
      }

      return var0;
   }

   public Rule(String var1, String var2, String var3, Rule.PhonemeExpr var4) {
      this.pattern = var1;
      this.lContext = pattern(var2 + "$");
      this.rContext = pattern("^" + var3);
      this.phoneme = var4;
   }

   public Rule.RPattern getLContext() {
      return this.lContext;
   }

   public String getPattern() {
      return this.pattern;
   }

   public Rule.PhonemeExpr getPhoneme() {
      return this.phoneme;
   }

   public Rule.RPattern getRContext() {
      return this.rContext;
   }

   public boolean patternAndContextMatches(CharSequence var1, int var2) {
      if (var2 < 0) {
         throw new IndexOutOfBoundsException("Can not match pattern at negative indexes");
      } else {
         int var3 = this.pattern.length();
         int var4 = var2 + var3;
         if (var4 > var1.length()) {
            return false;
         } else if (!var1.subSequence(var2, var4).equals(this.pattern)) {
            return false;
         } else {
            return !this.rContext.isMatch(var1.subSequence(var4, var1.length())) ? false : this.lContext.isMatch(var1.subSequence(0, var2));
         }
      }
   }

   static boolean access$100(CharSequence var0, CharSequence var1) {
      return startsWith(var0, var1);
   }

   static boolean access$200(CharSequence var0, CharSequence var1) {
      return endsWith(var0, var1);
   }

   static boolean access$300(CharSequence var0, char var1) {
      return contains(var0, var1);
   }

   static {
      NameType[] var0 = NameType.values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         NameType var3 = var0[var2];
         EnumMap var4 = new EnumMap(RuleType.class);
         RuleType[] var5 = RuleType.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            RuleType var8 = var5[var7];
            HashMap var9 = new HashMap();
            Languages var10 = Languages.getInstance(var3);
            Iterator var11 = var10.getLanguages().iterator();

            while(var11.hasNext()) {
               String var12 = (String)var11.next();

               try {
                  var9.put(var12, parseRules(createScanner(var3, var8, var12), createResourceName(var3, var8, var12)));
               } catch (IllegalStateException var14) {
                  throw new IllegalStateException("Problem processing " + createResourceName(var3, var8, var12), var14);
               }
            }

            if (!var8.equals(RuleType.RULES)) {
               var9.put("common", parseRules(createScanner(var3, var8, "common"), createResourceName(var3, var8, "common")));
            }

            var4.put(var8, Collections.unmodifiableMap(var9));
         }

         RULES.put(var3, Collections.unmodifiableMap(var4));
      }

   }

   public interface RPattern {
      boolean isMatch(CharSequence var1);
   }

   public static final class PhonemeList implements Rule.PhonemeExpr {
      private final List phonemes;

      public PhonemeList(List var1) {
         this.phonemes = var1;
      }

      public List getPhonemes() {
         return this.phonemes;
      }

      public Iterable getPhonemes() {
         return this.getPhonemes();
      }
   }

   public interface PhonemeExpr {
      Iterable getPhonemes();
   }

   public static final class Phoneme implements Rule.PhonemeExpr {
      public static final Comparator COMPARATOR = new Comparator() {
         public int compare(Rule.Phoneme var1, Rule.Phoneme var2) {
            for(int var3 = 0; var3 < Rule.Phoneme.access$000(var1).length(); ++var3) {
               if (var3 >= Rule.Phoneme.access$000(var2).length()) {
                  return 1;
               }

               int var4 = Rule.Phoneme.access$000(var1).charAt(var3) - Rule.Phoneme.access$000(var2).charAt(var3);
               if (var4 != 0) {
                  return var4;
               }
            }

            if (Rule.Phoneme.access$000(var1).length() < Rule.Phoneme.access$000(var2).length()) {
               return -1;
            } else {
               return 0;
            }
         }

         public int compare(Object var1, Object var2) {
            return this.compare((Rule.Phoneme)var1, (Rule.Phoneme)var2);
         }
      };
      private final StringBuilder phonemeText;
      private final Languages.LanguageSet languages;

      public Phoneme(CharSequence var1, Languages.LanguageSet var2) {
         this.phonemeText = new StringBuilder(var1);
         this.languages = var2;
      }

      public Phoneme(Rule.Phoneme var1, Rule.Phoneme var2) {
         this((CharSequence)var1.phonemeText, (Languages.LanguageSet)var1.languages);
         this.phonemeText.append(var2.phonemeText);
      }

      public Phoneme(Rule.Phoneme var1, Rule.Phoneme var2, Languages.LanguageSet var3) {
         this((CharSequence)var1.phonemeText, (Languages.LanguageSet)var3);
         this.phonemeText.append(var2.phonemeText);
      }

      public Rule.Phoneme append(CharSequence var1) {
         this.phonemeText.append(var1);
         return this;
      }

      public Languages.LanguageSet getLanguages() {
         return this.languages;
      }

      public Iterable getPhonemes() {
         return Collections.singleton(this);
      }

      public CharSequence getPhonemeText() {
         return this.phonemeText;
      }

      /** @deprecated */
      @Deprecated
      public Rule.Phoneme join(Rule.Phoneme var1) {
         return new Rule.Phoneme(this.phonemeText.toString() + var1.phonemeText.toString(), this.languages.restrictTo(var1.languages));
      }

      static StringBuilder access$000(Rule.Phoneme var0) {
         return var0.phonemeText;
      }
   }
}
