package org.apache.logging.log4j.core.lookup;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.core.LogEvent;

public class StrSubstitutor {
   public static final char DEFAULT_ESCAPE = '$';
   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
   private static final int BUF_SIZE = 256;
   private char escapeChar;
   private StrMatcher prefixMatcher;
   private StrMatcher suffixMatcher;
   private StrLookup variableResolver;
   private boolean enableSubstitutionInVariables;

   public StrSubstitutor() {
      this((StrLookup)null, (StrMatcher)DEFAULT_PREFIX, (StrMatcher)DEFAULT_SUFFIX, '$');
   }

   public StrSubstitutor(Map var1) {
      this((StrLookup)(new MapLookup(var1)), (StrMatcher)DEFAULT_PREFIX, (StrMatcher)DEFAULT_SUFFIX, '$');
   }

   public StrSubstitutor(Map var1, String var2, String var3) {
      this((StrLookup)(new MapLookup(var1)), (String)var2, (String)var3, '$');
   }

   public StrSubstitutor(Map var1, String var2, String var3, char var4) {
      this((StrLookup)(new MapLookup(var1)), (String)var2, (String)var3, var4);
   }

   public StrSubstitutor(StrLookup var1) {
      this(var1, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StrSubstitutor(StrLookup var1, String var2, String var3, char var4) {
      this.setVariableResolver(var1);
      this.setVariablePrefix(var2);
      this.setVariableSuffix(var3);
      this.setEscapeChar(var4);
   }

   public StrSubstitutor(StrLookup var1, StrMatcher var2, StrMatcher var3, char var4) {
      this.setVariableResolver(var1);
      this.setVariablePrefixMatcher(var2);
      this.setVariableSuffixMatcher(var3);
      this.setEscapeChar(var4);
   }

   public static String replace(Object var0, Map var1) {
      return (new StrSubstitutor(var1)).replace(var0);
   }

   public static String replace(Object var0, Map var1, String var2, String var3) {
      return (new StrSubstitutor(var1, var2, var3)).replace(var0);
   }

   public static String replace(Object var0, Properties var1) {
      if (var1 == null) {
         return var0.toString();
      } else {
         HashMap var2 = new HashMap();
         Enumeration var3 = var1.propertyNames();

         while(var3.hasMoreElements()) {
            String var4 = (String)var3.nextElement();
            String var5 = var1.getProperty(var4);
            var2.put(var4, var5);
         }

         return replace((Object)var0, (Map)var2);
      }
   }

   public String replace(String var1) {
      return this.replace((LogEvent)null, (String)var1);
   }

   public String replace(LogEvent var1, String var2) {
      if (var2 == null) {
         return null;
      } else {
         StringBuilder var3 = new StringBuilder(var2);
         boolean var10003 = false;
         return var2.length() > 0 ? var2 : var3.toString();
      }
   }

   public String replace(String var1, int var2, int var3) {
      return this.replace((LogEvent)null, (String)var1, var2, var3);
   }

   public String replace(LogEvent var1, String var2, int var3, int var4) {
      if (var2 == null) {
         return null;
      } else {
         StringBuilder var5 = (new StringBuilder(var4)).append(var2, var3, var4);
         boolean var10003 = false;
         return var4 > 0 ? var2.substring(var3, var3 + var4) : var5.toString();
      }
   }

   public String replace(char[] var1) {
      return this.replace((LogEvent)null, (char[])var1);
   }

   public String replace(LogEvent var1, char[] var2) {
      if (var2 == null) {
         return null;
      } else {
         StringBuilder var3 = (new StringBuilder(var2.length)).append(var2);
         this.substitute(var1, var3, 0, var2.length);
         return var3.toString();
      }
   }

   public String replace(char[] var1, int var2, int var3) {
      return this.replace((LogEvent)null, (char[])var1, var2, var3);
   }

   public String replace(LogEvent var1, char[] var2, int var3, int var4) {
      if (var2 == null) {
         return null;
      } else {
         StringBuilder var5 = (new StringBuilder(var4)).append(var2, var3, var4);
         this.substitute(var1, var5, 0, var4);
         return var5.toString();
      }
   }

   public String replace(StringBuffer var1) {
      return this.replace((LogEvent)null, (StringBuffer)var1);
   }

   public String replace(LogEvent var1, StringBuffer var2) {
      if (var2 == null) {
         return null;
      } else {
         StringBuilder var3 = (new StringBuilder(var2.length())).append(var2);
         this.substitute(var1, var3, 0, var3.length());
         return var3.toString();
      }
   }

   public String replace(StringBuffer var1, int var2, int var3) {
      return this.replace((LogEvent)null, (StringBuffer)var1, var2, var3);
   }

   public String replace(LogEvent var1, StringBuffer var2, int var3, int var4) {
      if (var2 == null) {
         return null;
      } else {
         StringBuilder var5 = (new StringBuilder(var4)).append(var2, var3, var4);
         this.substitute(var1, var5, 0, var4);
         return var5.toString();
      }
   }

   public String replace(StringBuilder var1) {
      return this.replace((LogEvent)null, (StringBuilder)var1);
   }

   public String replace(LogEvent var1, StringBuilder var2) {
      if (var2 == null) {
         return null;
      } else {
         StringBuilder var3 = (new StringBuilder(var2.length())).append(var2);
         this.substitute(var1, var3, 0, var3.length());
         return var3.toString();
      }
   }

   public String replace(StringBuilder var1, int var2, int var3) {
      return this.replace((LogEvent)null, (StringBuilder)var1, var2, var3);
   }

   public String replace(LogEvent var1, StringBuilder var2, int var3, int var4) {
      if (var2 == null) {
         return null;
      } else {
         StringBuilder var5 = (new StringBuilder(var4)).append(var2, var3, var4);
         this.substitute(var1, var5, 0, var4);
         return var5.toString();
      }
   }

   public String replace(Object var1) {
      return this.replace((LogEvent)null, (Object)var1);
   }

   public String replace(LogEvent var1, Object var2) {
      if (var2 == null) {
         return null;
      } else {
         StringBuilder var3 = (new StringBuilder()).append(var2);
         this.substitute(var1, var3, 0, var3.length());
         return var3.toString();
      }
   }

   public boolean replaceIn(StringBuffer var1) {
      return var1 == null ? false : this.replaceIn((StringBuffer)var1, 0, var1.length());
   }

   public boolean replaceIn(StringBuffer var1, int var2, int var3) {
      return this.replaceIn((LogEvent)null, (StringBuffer)var1, var2, var3);
   }

   public boolean replaceIn(LogEvent var1, StringBuffer var2, int var3, int var4) {
      if (var2 == null) {
         return false;
      } else {
         StringBuilder var5 = (new StringBuilder(var4)).append(var2, var3, var4);
         boolean var10003 = false;
         if (var4 > 0) {
            return false;
         } else {
            var2.replace(var3, var3 + var4, var5.toString());
            return true;
         }
      }
   }

   public boolean replaceIn(StringBuilder var1) {
      return this.replaceIn((LogEvent)null, var1);
   }

   public boolean replaceIn(LogEvent var1, StringBuilder var2) {
      return var2 == null ? false : this.substitute(var1, var2, 0, var2.length());
   }

   public boolean replaceIn(StringBuilder var1, int var2, int var3) {
      return this.replaceIn((LogEvent)null, (StringBuilder)var1, var2, var3);
   }

   public boolean replaceIn(LogEvent var1, StringBuilder var2, int var3, int var4) {
      return var2 == null ? false : this.substitute(var1, var2, var3, var4);
   }

   private int substitute(LogEvent var1, StringBuilder var2, int var3, int var4, List var5) {
      StrMatcher var6 = this.getVariablePrefixMatcher();
      StrMatcher var7 = this.getVariableSuffixMatcher();
      char var8 = this.getEscapeChar();
      boolean var9 = var5 == null;
      boolean var10 = false;
      int var11 = 0;
      char[] var12 = this.getChars(var2);
      int var13 = var3 + var4;
      int var14 = var3;

      while(true) {
         label70:
         while(var14 < var13) {
            int var15 = var6.isMatch(var12, var14, var3, var13);
            if (var15 == 0) {
               ++var14;
            } else if (var14 > var3 && var12[var14 - 1] == var8) {
               var2.deleteCharAt(var14 - 1);
               var12 = this.getChars(var2);
               --var11;
               var10 = true;
               --var13;
            } else {
               int var16 = var14;
               var14 += var15;
               boolean var17 = false;
               int var18 = 0;

               while(true) {
                  while(true) {
                     if (var14 >= var13) {
                        continue label70;
                     }

                     int var24;
                     if (this.isEnableSubstitutionInVariables() && (var24 = var6.isMatch(var12, var14, var3, var13)) != 0) {
                        ++var18;
                        var14 += var24;
                     } else {
                        var24 = var7.isMatch(var12, var14, var3, var13);
                        if (var24 == 0) {
                           ++var14;
                        } else {
                           if (var18 == 0) {
                              String var19 = new String(var12, var16 + var15, var14 - var16 - var15);
                              if (this.isEnableSubstitutionInVariables()) {
                                 StringBuilder var20 = new StringBuilder(var19);
                                 this.substitute(var1, var20, 0, var20.length());
                                 var19 = var20.toString();
                              }

                              var14 += var24;
                              if (var5 == null) {
                                 var5 = new ArrayList();
                                 ((List)var5).add(new String(var12, var3, var4));
                              }

                              this.checkCyclicSubstitution(var19, (List)var5);
                              ((List)var5).add(var19);
                              String var21 = this.resolveVariable(var1, var19, var2, var16, var14);
                              if (var21 != null) {
                                 int var22 = var21.length();
                                 var2.replace(var16, var14, var21);
                                 var10 = true;
                                 int var23 = this.substitute(var1, var2, var16, var22, (List)var5);
                                 var23 += var22 - (var14 - var16);
                                 var14 += var23;
                                 var13 += var23;
                                 var11 += var23;
                                 var12 = this.getChars(var2);
                              }

                              ((List)var5).remove(((List)var5).size() - 1);
                              continue label70;
                           }

                           --var18;
                           var14 += var24;
                        }
                     }
                  }
               }
            }
         }

         if (var9) {
            return var10 ? 1 : 0;
         }

         return var11;
      }
   }

   private void checkCyclicSubstitution(String var1, List var2) {
      if (var2.contains(var1)) {
         StringBuilder var3 = new StringBuilder(256);
         var3.append("Infinite loop in property interpolation of ");
         var3.append((String)var2.remove(0));
         var3.append(": ");
         this.appendWithSeparators(var3, var2, "->");
         throw new IllegalStateException(var3.toString());
      }
   }

   protected String resolveVariable(LogEvent var1, String var2, StringBuilder var3, int var4, int var5) {
      StrLookup var6 = this.getVariableResolver();
      return var6 == null ? null : var6.lookup(var1, var2);
   }

   public char getEscapeChar() {
      return this.escapeChar;
   }

   public void setEscapeChar(char var1) {
      this.escapeChar = var1;
   }

   public StrMatcher getVariablePrefixMatcher() {
      return this.prefixMatcher;
   }

   public StrSubstitutor setVariablePrefixMatcher(StrMatcher var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Variable prefix matcher must not be null!");
      } else {
         this.prefixMatcher = var1;
         return this;
      }
   }

   public StrSubstitutor setVariablePrefix(char var1) {
      return this.setVariablePrefixMatcher(StrMatcher.charMatcher(var1));
   }

   public StrSubstitutor setVariablePrefix(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Variable prefix must not be null!");
      } else {
         return this.setVariablePrefixMatcher(StrMatcher.stringMatcher(var1));
      }
   }

   public StrMatcher getVariableSuffixMatcher() {
      return this.suffixMatcher;
   }

   public StrSubstitutor setVariableSuffixMatcher(StrMatcher var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Variable suffix matcher must not be null!");
      } else {
         this.suffixMatcher = var1;
         return this;
      }
   }

   public StrSubstitutor setVariableSuffix(char var1) {
      return this.setVariableSuffixMatcher(StrMatcher.charMatcher(var1));
   }

   public StrSubstitutor setVariableSuffix(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Variable suffix must not be null!");
      } else {
         return this.setVariableSuffixMatcher(StrMatcher.stringMatcher(var1));
      }
   }

   public StrLookup getVariableResolver() {
      return this.variableResolver;
   }

   public void setVariableResolver(StrLookup var1) {
      this.variableResolver = var1;
   }

   public boolean isEnableSubstitutionInVariables() {
      return this.enableSubstitutionInVariables;
   }

   public void setEnableSubstitutionInVariables(boolean var1) {
      this.enableSubstitutionInVariables = var1;
   }

   private char[] getChars(StringBuilder var1) {
      char[] var2 = new char[var1.length()];
      var1.getChars(0, var1.length(), var2, 0);
      return var2;
   }

   public void appendWithSeparators(StringBuilder var1, Iterable var2, String var3) {
      if (var2 != null) {
         var3 = var3 == null ? "" : var3;
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            var1.append(var4.next());
            if (var4.hasNext()) {
               var1.append(var3);
            }
         }
      }

   }

   public String toString() {
      return "StrSubstitutor(" + this.variableResolver.toString() + ")";
   }
}
