package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class StrSubstitutor {
   public static final char DEFAULT_ESCAPE = '$';
   public static final StrMatcher DEFAULT_PREFIX = StrMatcher.stringMatcher("${");
   public static final StrMatcher DEFAULT_SUFFIX = StrMatcher.stringMatcher("}");
   public static final StrMatcher DEFAULT_VALUE_DELIMITER = StrMatcher.stringMatcher(":-");
   private char escapeChar;
   private StrMatcher prefixMatcher;
   private StrMatcher suffixMatcher;
   private StrMatcher valueDelimiterMatcher;
   private StrLookup variableResolver;
   private boolean enableSubstitutionInVariables;

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

         return replace(var0, (Map)var2);
      }
   }

   public static String replaceSystemProperties(Object var0) {
      return (new StrSubstitutor(StrLookup.systemPropertiesLookup())).replace(var0);
   }

   public StrSubstitutor() {
      this((StrLookup)null, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StrSubstitutor(Map var1) {
      this(StrLookup.mapLookup(var1), DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StrSubstitutor(Map var1, String var2, String var3) {
      this(StrLookup.mapLookup(var1), var2, var3, '$');
   }

   public StrSubstitutor(Map var1, String var2, String var3, char var4) {
      this(StrLookup.mapLookup(var1), var2, var3, var4);
   }

   public StrSubstitutor(Map var1, String var2, String var3, char var4, String var5) {
      this(StrLookup.mapLookup(var1), var2, var3, var4, var5);
   }

   public StrSubstitutor(StrLookup var1) {
      this(var1, DEFAULT_PREFIX, DEFAULT_SUFFIX, '$');
   }

   public StrSubstitutor(StrLookup var1, String var2, String var3, char var4) {
      this.setVariableResolver(var1);
      this.setVariablePrefix(var2);
      this.setVariableSuffix(var3);
      this.setEscapeChar(var4);
      this.setValueDelimiterMatcher(DEFAULT_VALUE_DELIMITER);
   }

   public StrSubstitutor(StrLookup var1, String var2, String var3, char var4, String var5) {
      this.setVariableResolver(var1);
      this.setVariablePrefix(var2);
      this.setVariableSuffix(var3);
      this.setEscapeChar(var4);
      this.setValueDelimiter(var5);
   }

   public StrSubstitutor(StrLookup var1, StrMatcher var2, StrMatcher var3, char var4) {
      this(var1, var2, var3, var4, DEFAULT_VALUE_DELIMITER);
   }

   public StrSubstitutor(StrLookup var1, StrMatcher var2, StrMatcher var3, char var4, StrMatcher var5) {
      this.setVariableResolver(var1);
      this.setVariablePrefixMatcher(var2);
      this.setVariableSuffixMatcher(var3);
      this.setEscapeChar(var4);
      this.setValueDelimiterMatcher(var5);
   }

   public String replace(String var1) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var2 = new StrBuilder(var1);
         boolean var10002 = false;
         return var1.length() > 0 ? var1 : var2.toString();
      }
   }

   public String replace(String var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         boolean var10002 = false;
         return var3 > 0 ? var1.substring(var2, var2 + var3) : var4.toString();
      }
   }

   public String replace(char[] var1) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var2 = (new StrBuilder(var1.length)).append(var1);
         this.substitute(var2, 0, var1.length);
         return var2.toString();
      }
   }

   public String replace(char[] var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         this.substitute(var4, 0, var3);
         return var4.toString();
      }
   }

   public String replace(StringBuffer var1) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var2 = (new StrBuilder(var1.length())).append(var1);
         this.substitute(var2, 0, var2.length());
         return var2.toString();
      }
   }

   public String replace(StringBuffer var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         this.substitute(var4, 0, var3);
         return var4.toString();
      }
   }

   public String replace(CharSequence var1) {
      return var1 == null ? null : this.replace((CharSequence)var1, 0, var1.length());
   }

   public String replace(CharSequence var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         this.substitute(var4, 0, var3);
         return var4.toString();
      }
   }

   public String replace(StrBuilder var1) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var2 = (new StrBuilder(var1.length())).append(var1);
         this.substitute(var2, 0, var2.length());
         return var2.toString();
      }
   }

   public String replace(StrBuilder var1, int var2, int var3) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         this.substitute(var4, 0, var3);
         return var4.toString();
      }
   }

   public String replace(Object var1) {
      if (var1 == null) {
         return null;
      } else {
         StrBuilder var2 = (new StrBuilder()).append(var1);
         this.substitute(var2, 0, var2.length());
         return var2.toString();
      }
   }

   public boolean replaceIn(StringBuffer var1) {
      return var1 == null ? false : this.replaceIn((StringBuffer)var1, 0, var1.length());
   }

   public boolean replaceIn(StringBuffer var1, int var2, int var3) {
      if (var1 == null) {
         return false;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         boolean var10002 = false;
         if (var3 > 0) {
            return false;
         } else {
            var1.replace(var2, var2 + var3, var4.toString());
            return true;
         }
      }
   }

   public boolean replaceIn(StringBuilder var1) {
      return var1 == null ? false : this.replaceIn((StringBuilder)var1, 0, var1.length());
   }

   public boolean replaceIn(StringBuilder var1, int var2, int var3) {
      if (var1 == null) {
         return false;
      } else {
         StrBuilder var4 = (new StrBuilder(var3)).append(var1, var2, var3);
         boolean var10002 = false;
         if (var3 > 0) {
            return false;
         } else {
            var1.replace(var2, var2 + var3, var4.toString());
            return true;
         }
      }
   }

   public boolean replaceIn(StrBuilder var1) {
      return var1 == null ? false : this.substitute(var1, 0, var1.length());
   }

   public boolean replaceIn(StrBuilder var1, int var2, int var3) {
      return var1 == null ? false : this.substitute(var1, var2, var3);
   }

   private int substitute(StrBuilder var1, int var2, int var3, List var4) {
      StrMatcher var5 = this.getVariablePrefixMatcher();
      StrMatcher var6 = this.getVariableSuffixMatcher();
      char var7 = this.getEscapeChar();
      StrMatcher var8 = this.getValueDelimiterMatcher();
      boolean var9 = this.isEnableSubstitutionInVariables();
      boolean var10 = var4 == null;
      boolean var11 = false;
      int var12 = 0;
      char[] var13 = var1.buffer;
      int var14 = var2 + var3;
      int var15 = var2;

      while(true) {
         label95:
         while(var15 < var14) {
            int var16 = var5.isMatch(var13, var15, var2, var14);
            if (var16 == 0) {
               ++var15;
            } else if (var15 > var2 && var13[var15 - 1] == var7) {
               var1.deleteCharAt(var15 - 1);
               var13 = var1.buffer;
               --var12;
               var11 = true;
               --var14;
            } else {
               int var17 = var15;
               var15 += var16;
               boolean var18 = false;
               int var19 = 0;

               while(true) {
                  while(true) {
                     if (var15 >= var14) {
                        continue label95;
                     }

                     int var27;
                     if (var9 && (var27 = var5.isMatch(var13, var15, var2, var14)) != 0) {
                        ++var19;
                        var15 += var27;
                     } else {
                        var27 = var6.isMatch(var13, var15, var2, var14);
                        if (var27 == 0) {
                           ++var15;
                        } else {
                           if (var19 == 0) {
                              String var20 = new String(var13, var17 + var16, var15 - var17 - var16);
                              if (var9) {
                                 StrBuilder var21 = new StrBuilder(var20);
                                 this.substitute(var21, 0, var21.length());
                                 var20 = var21.toString();
                              }

                              var15 += var27;
                              String var22 = var20;
                              String var23 = null;
                              int var26;
                              int var29;
                              if (var8 != null) {
                                 char[] var24 = var20.toCharArray();
                                 boolean var25 = false;

                                 for(var26 = 0; var26 < var24.length && (var9 || var5.isMatch(var24, var26, var26, var24.length) == 0); ++var26) {
                                    if ((var29 = var8.isMatch(var24, var26)) != 0) {
                                       var22 = var20.substring(0, var26);
                                       var23 = var20.substring(var26 + var29);
                                       break;
                                    }
                                 }
                              }

                              if (var4 == null) {
                                 var4 = new ArrayList();
                                 ((List)var4).add(new String(var13, var2, var3));
                              }

                              this.checkCyclicSubstitution(var22, (List)var4);
                              ((List)var4).add(var22);
                              String var28 = this.resolveVariable(var22, var1, var17, var15);
                              if (var28 == null) {
                                 var28 = var23;
                              }

                              if (var28 != null) {
                                 var29 = var28.length();
                                 var1.replace(var17, var15, var28);
                                 var11 = true;
                                 var26 = this.substitute(var1, var17, var29, (List)var4);
                                 var26 = var26 + var29 - (var15 - var17);
                                 var15 += var26;
                                 var14 += var26;
                                 var12 += var26;
                                 var13 = var1.buffer;
                              }

                              ((List)var4).remove(((List)var4).size() - 1);
                              continue label95;
                           }

                           --var19;
                           var15 += var27;
                        }
                     }
                  }
               }
            }
         }

         if (var10) {
            return var11 ? 1 : 0;
         }

         return var12;
      }
   }

   private void checkCyclicSubstitution(String var1, List var2) {
      if (var2.contains(var1)) {
         StrBuilder var3 = new StrBuilder(256);
         var3.append("Infinite loop in property interpolation of ");
         var3.append((String)var2.remove(0));
         var3.append(": ");
         var3.appendWithSeparators((Iterable)var2, "->");
         throw new IllegalStateException(var3.toString());
      }
   }

   protected String resolveVariable(String var1, StrBuilder var2, int var3, int var4) {
      StrLookup var5 = this.getVariableResolver();
      return var5 == null ? null : var5.lookup(var1);
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

   public StrMatcher getValueDelimiterMatcher() {
      return this.valueDelimiterMatcher;
   }

   public StrSubstitutor setValueDelimiterMatcher(StrMatcher var1) {
      this.valueDelimiterMatcher = var1;
      return this;
   }

   public StrSubstitutor setValueDelimiter(char var1) {
      return this.setValueDelimiterMatcher(StrMatcher.charMatcher(var1));
   }

   public StrSubstitutor setValueDelimiter(String var1) {
      if (StringUtils.isEmpty(var1)) {
         this.setValueDelimiterMatcher((StrMatcher)null);
         return this;
      } else {
         return this.setValueDelimiterMatcher(StrMatcher.stringMatcher(var1));
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
}
