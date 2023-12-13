package org.apache.logging.log4j.core.pattern;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.status.StatusLogger;

public final class PatternParser {
   private static final char ESCAPE_CHAR = '%';
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static final int BUF_SIZE = 32;
   private static final int DECIMAL = 10;
   private final Configuration config;
   private final Map converterRules;

   public PatternParser(String var1) {
      this((Configuration)null, var1, (Class)null, (Class)null);
   }

   public PatternParser(Configuration var1, String var2, Class var3) {
      this(var1, var2, var3, (Class)null);
   }

   public PatternParser(Configuration var1, String var2, Class var3, Class var4) {
      this.config = var1;
      PluginManager var5 = new PluginManager(var2, var3);
      var5.collectPlugins();
      Map var6 = var5.getPlugins();
      HashMap var7 = new HashMap();
      Iterator var8 = var6.values().iterator();

      while(var8.hasNext()) {
         PluginType var9 = (PluginType)var8.next();

         try {
            Class var10 = var9.getPluginClass();
            if (var4 == null || var4.isAssignableFrom(var10)) {
               ConverterKeys var11 = (ConverterKeys)var10.getAnnotation(ConverterKeys.class);
               if (var11 != null) {
                  String[] var12 = var11.value();
                  int var13 = var12.length;

                  for(int var14 = 0; var14 < var13; ++var14) {
                     String var15 = var12[var14];
                     var7.put(var15, var10);
                  }
               }
            }
         } catch (Exception var16) {
            LOGGER.error((String)("Error processing plugin " + var9.getElementName()), (Throwable)var16);
         }
      }

      this.converterRules = var7;
   }

   public List parse(String var1) {
      return this.parse(var1, false);
   }

   public List parse(String var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      this.parse(var1, var4, var5);
      Iterator var6 = var5.iterator();
      boolean var7 = false;

      Object var10;
      FormattingInfo var11;
      for(Iterator var8 = var4.iterator(); var8.hasNext(); var3.add(new PatternFormatter((LogEventPatternConverter)var10, var11))) {
         PatternConverter var9 = (PatternConverter)var8.next();
         if (var9 instanceof LogEventPatternConverter) {
            var10 = (LogEventPatternConverter)var9;
            var7 |= ((LogEventPatternConverter)var10).handlesThrowable();
         } else {
            var10 = new LiteralPatternConverter(this.config, "");
         }

         if (var6.hasNext()) {
            var11 = (FormattingInfo)var6.next();
         } else {
            var11 = FormattingInfo.getDefault();
         }
      }

      if (var2 && !var7) {
         ExtendedThrowablePatternConverter var12 = ExtendedThrowablePatternConverter.newInstance((String[])null);
         var3.add(new PatternFormatter(var12, FormattingInfo.getDefault()));
      }

      return var3;
   }

   private static int extractConverter(char var0, String var1, int var2, StringBuilder var3, StringBuilder var4) {
      var3.setLength(0);
      if (!Character.isUnicodeIdentifierStart(var0)) {
         return var2;
      } else {
         var3.append(var0);

         while(var2 < var1.length() && Character.isUnicodeIdentifierPart(var1.charAt(var2))) {
            var3.append(var1.charAt(var2));
            var4.append(var1.charAt(var2));
            ++var2;
         }

         return var2;
      }
   }

   private static int extractOptions(String var0, int var1, List var2) {
      while(true) {
         if (var1 < var0.length() && var0.charAt(var1) == '{') {
            int var3 = var1++;
            int var5 = 0;

            int var4;
            do {
               var4 = var0.indexOf(125, var1);
               if (var4 != -1) {
                  int var6 = var0.indexOf("{", var1);
                  if (var6 != -1 && var6 < var4) {
                     var1 = var4 + 1;
                     ++var5;
                  } else if (var5 > 0) {
                     --var5;
                  }
               }
            } while(var5 > 0);

            if (var4 != -1) {
               String var7 = var0.substring(var3 + 1, var4);
               var2.add(var7);
               var1 = var4 + 1;
               continue;
            }
         }

         return var1;
      }
   }

   public void parse(String var1, List var2, List var3) {
      if (var1 == null) {
         throw new NullPointerException("pattern");
      } else {
         StringBuilder var4 = new StringBuilder(32);
         int var5 = var1.length();
         PatternParser.ParserState var6 = PatternParser.ParserState.LITERAL_STATE;
         int var8 = 0;
         FormattingInfo var9 = FormattingInfo.getDefault();

         while(true) {
            while(true) {
               while(var8 < var5) {
                  char var7 = var1.charAt(var8++);
                  switch(var6) {
                  case LITERAL_STATE:
                     if (var8 == var5) {
                        var4.append(var7);
                     } else if (var7 == '%') {
                        switch(var1.charAt(var8)) {
                        case '%':
                           var4.append(var7);
                           ++var8;
                           break;
                        default:
                           if (var4.length() != 0) {
                              var2.add(new LiteralPatternConverter(this.config, var4.toString()));
                              var3.add(FormattingInfo.getDefault());
                           }

                           var4.setLength(0);
                           var4.append(var7);
                           var6 = PatternParser.ParserState.CONVERTER_STATE;
                           var9 = FormattingInfo.getDefault();
                        }
                     } else {
                        var4.append(var7);
                     }
                     break;
                  case CONVERTER_STATE:
                     var4.append(var7);
                     switch(var7) {
                     case '-':
                        var9 = new FormattingInfo(true, var9.getMinLength(), var9.getMaxLength());
                        continue;
                     case '.':
                        var6 = PatternParser.ParserState.DOT_STATE;
                        continue;
                     default:
                        if (var7 >= '0' && var7 <= '9') {
                           var9 = new FormattingInfo(var9.isLeftAligned(), var7 - 48, var9.getMaxLength());
                           var6 = PatternParser.ParserState.MIN_STATE;
                           continue;
                        }

                        var8 = this.finalizeConverter(var7, var1, var8, var4, var9, this.converterRules, var2, var3);
                        var6 = PatternParser.ParserState.LITERAL_STATE;
                        var9 = FormattingInfo.getDefault();
                        var4.setLength(0);
                        continue;
                     }
                  case MIN_STATE:
                     var4.append(var7);
                     if (var7 >= '0' && var7 <= '9') {
                        var9 = new FormattingInfo(var9.isLeftAligned(), var9.getMinLength() * 10 + var7 - 48, var9.getMaxLength());
                     } else if (var7 == '.') {
                        var6 = PatternParser.ParserState.DOT_STATE;
                     } else {
                        var8 = this.finalizeConverter(var7, var1, var8, var4, var9, this.converterRules, var2, var3);
                        var6 = PatternParser.ParserState.LITERAL_STATE;
                        var9 = FormattingInfo.getDefault();
                        var4.setLength(0);
                     }
                     break;
                  case DOT_STATE:
                     var4.append(var7);
                     if (var7 >= '0' && var7 <= '9') {
                        var9 = new FormattingInfo(var9.isLeftAligned(), var9.getMinLength(), var7 - 48);
                        var6 = PatternParser.ParserState.MAX_STATE;
                        break;
                     }

                     LOGGER.error("Error occurred in position " + var8 + ".\n Was expecting digit, instead got char \"" + var7 + "\".");
                     var6 = PatternParser.ParserState.LITERAL_STATE;
                     break;
                  case MAX_STATE:
                     var4.append(var7);
                     if (var7 >= '0' && var7 <= '9') {
                        var9 = new FormattingInfo(var9.isLeftAligned(), var9.getMinLength(), var9.getMaxLength() * 10 + var7 - 48);
                     } else {
                        var8 = this.finalizeConverter(var7, var1, var8, var4, var9, this.converterRules, var2, var3);
                        var6 = PatternParser.ParserState.LITERAL_STATE;
                        var9 = FormattingInfo.getDefault();
                        var4.setLength(0);
                     }
                  }
               }

               if (var4.length() != 0) {
                  var2.add(new LiteralPatternConverter(this.config, var4.toString()));
                  var3.add(FormattingInfo.getDefault());
               }

               return;
            }
         }
      }
   }

   private PatternConverter createConverter(String var1, StringBuilder var2, Map var3, List var4) {
      String var5 = var1;
      Class var6 = null;

      for(int var7 = var1.length(); var7 > 0 && var6 == null; --var7) {
         var5 = var5.substring(0, var7);
         if (var6 == null && var3 != null) {
            var6 = (Class)var3.get(var5);
         }
      }

      if (var6 == null) {
         LOGGER.error("Unrecognized format specifier [" + var1 + "]");
         return null;
      } else {
         Method[] var19 = var6.getDeclaredMethods();
         Method var8 = null;
         Method[] var9 = var19;
         int var10 = var19.length;

         int var11;
         for(var11 = 0; var11 < var10; ++var11) {
            Method var12 = var9[var11];
            if (Modifier.isStatic(var12.getModifiers()) && var12.getDeclaringClass().equals(var6) && var12.getName().equals("newInstance")) {
               if (var8 == null) {
                  var8 = var12;
               } else if (var12.getReturnType().equals(var8.getReturnType())) {
                  LOGGER.error("Class " + var6 + " cannot contain multiple static newInstance methods");
                  return null;
               }
            }
         }

         if (var8 == null) {
            LOGGER.error("Class " + var6 + " does not contain a static newInstance method");
            return null;
         } else {
            Class[] var20 = var8.getParameterTypes();
            Object[] var21 = var20.length > 0 ? new Object[var20.length] : null;
            if (var21 != null) {
               var11 = 0;
               boolean var22 = false;
               Class[] var13 = var20;
               int var14 = var20.length;

               for(int var15 = 0; var15 < var14; ++var15) {
                  Class var16 = var13[var15];
                  if (var16.isArray() && var16.getName().equals("[Ljava.lang.String;")) {
                     String[] var17 = (String[])var4.toArray(new String[var4.size()]);
                     var21[var11] = var17;
                  } else if (var16.isAssignableFrom(Configuration.class)) {
                     var21[var11] = this.config;
                  } else {
                     LOGGER.error("Unknown parameter type " + var16.getName() + " for static newInstance method of " + var6.getName());
                     var22 = true;
                  }

                  ++var11;
               }

               if (var22) {
                  return null;
               }
            }

            try {
               Object var23 = var8.invoke((Object)null, var21);
               if (var23 instanceof PatternConverter) {
                  var2.delete(0, var2.length() - (var1.length() - var5.length()));
                  return (PatternConverter)var23;
               }

               LOGGER.warn("Class " + var6.getName() + " does not extend PatternConverter.");
            } catch (Exception var18) {
               LOGGER.error((String)("Error creating converter for " + var1), (Throwable)var18);
            }

            return null;
         }
      }
   }

   private int finalizeConverter(char var1, String var2, int var3, StringBuilder var4, FormattingInfo var5, Map var6, List var7, List var8) {
      StringBuilder var9 = new StringBuilder();
      var3 = extractConverter(var1, var2, var3, var9, var4);
      String var10 = var9.toString();
      ArrayList var11 = new ArrayList();
      var3 = extractOptions(var2, var3, var11);
      PatternConverter var12 = this.createConverter(var10, var4, var6, var11);
      if (var12 == null) {
         StringBuilder var13;
         if (Strings.isEmpty(var10)) {
            var13 = new StringBuilder("Empty conversion specifier starting at position ");
         } else {
            var13 = new StringBuilder("Unrecognized conversion specifier [");
            var13.append(var10);
            var13.append("] starting at position ");
         }

         var13.append(Integer.toString(var3));
         var13.append(" in conversion pattern.");
         LOGGER.error(var13.toString());
         var7.add(new LiteralPatternConverter(this.config, var4.toString()));
         var8.add(FormattingInfo.getDefault());
      } else {
         var7.add(var12);
         var8.add(var5);
         if (var4.length() > 0) {
            var7.add(new LiteralPatternConverter(this.config, var4.toString()));
            var8.add(FormattingInfo.getDefault());
         }
      }

      var4.setLength(0);
      return var3;
   }

   private static enum ParserState {
      LITERAL_STATE,
      CONVERTER_STATE,
      DOT_STATE,
      MIN_STATE,
      MAX_STATE;

      private static final PatternParser.ParserState[] $VALUES = new PatternParser.ParserState[]{LITERAL_STATE, CONVERTER_STATE, DOT_STATE, MIN_STATE, MAX_STATE};
   }
}
