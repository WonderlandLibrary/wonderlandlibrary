package com.ibm.icu.text;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Map;

public class PluralFormat extends UFormat {
   private static final long serialVersionUID = 1L;
   private ULocale ulocale = null;
   private PluralRules pluralRules = null;
   private String pattern = null;
   private transient MessagePattern msgPattern;
   private Map parsedValues = null;
   private NumberFormat numberFormat = null;
   private transient double offset = 0.0D;
   private transient PluralFormat.PluralSelectorAdapter pluralRulesWrapper = new PluralFormat.PluralSelectorAdapter(this);
   static final boolean $assertionsDisabled = !PluralFormat.class.desiredAssertionStatus();

   public PluralFormat() {
      this.init((PluralRules)null, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public PluralFormat(ULocale var1) {
      this.init((PluralRules)null, PluralRules.PluralType.CARDINAL, var1);
   }

   public PluralFormat(PluralRules var1) {
      this.init(var1, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
   }

   public PluralFormat(ULocale var1, PluralRules var2) {
      this.init(var2, PluralRules.PluralType.CARDINAL, var1);
   }

   public PluralFormat(ULocale var1, PluralRules.PluralType var2) {
      this.init((PluralRules)null, var2, var1);
   }

   public PluralFormat(String var1) {
      this.init((PluralRules)null, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
      this.applyPattern(var1);
   }

   public PluralFormat(ULocale var1, String var2) {
      this.init((PluralRules)null, PluralRules.PluralType.CARDINAL, var1);
      this.applyPattern(var2);
   }

   public PluralFormat(PluralRules var1, String var2) {
      this.init(var1, PluralRules.PluralType.CARDINAL, ULocale.getDefault(ULocale.Category.FORMAT));
      this.applyPattern(var2);
   }

   public PluralFormat(ULocale var1, PluralRules var2, String var3) {
      this.init(var2, PluralRules.PluralType.CARDINAL, var1);
      this.applyPattern(var3);
   }

   public PluralFormat(ULocale var1, PluralRules.PluralType var2, String var3) {
      this.init((PluralRules)null, var2, var1);
      this.applyPattern(var3);
   }

   private void init(PluralRules var1, PluralRules.PluralType var2, ULocale var3) {
      this.ulocale = var3;
      this.pluralRules = var1 == null ? PluralRules.forLocale(this.ulocale, var2) : var1;
      this.resetPattern();
      this.numberFormat = NumberFormat.getInstance(this.ulocale);
   }

   private void resetPattern() {
      this.pattern = null;
      if (this.msgPattern != null) {
         this.msgPattern.clear();
      }

      this.offset = 0.0D;
   }

   public void applyPattern(String var1) {
      this.pattern = var1;
      if (this.msgPattern == null) {
         this.msgPattern = new MessagePattern();
      }

      try {
         this.msgPattern.parsePluralStyle(var1);
         this.offset = this.msgPattern.getPluralOffset(0);
      } catch (RuntimeException var3) {
         this.resetPattern();
         throw var3;
      }
   }

   public String toPattern() {
      return this.pattern;
   }

   static int findSubMessage(MessagePattern var0, int var1, PluralFormat.PluralSelector var2, double var3) {
      int var5 = var0.countParts();
      MessagePattern.Part var8 = var0.getPart(var1);
      double var6;
      if (var8.getType().hasNumericValue()) {
         var6 = var0.getNumericValue(var8);
         ++var1;
      } else {
         var6 = 0.0D;
      }

      String var9 = null;
      boolean var10 = false;
      int var11 = 0;

      do {
         var8 = var0.getPart(var1++);
         MessagePattern.Part.Type var12 = var8.getType();
         if (var12 == MessagePattern.Part.Type.ARG_LIMIT) {
            break;
         }

         if (!$assertionsDisabled && var12 != MessagePattern.Part.Type.ARG_SELECTOR) {
            throw new AssertionError();
         }

         if (var0.getPartType(var1).hasNumericValue()) {
            var8 = var0.getPart(var1++);
            if (var3 == var0.getNumericValue(var8)) {
               return var1;
            }
         } else if (!var10) {
            if (var0.partSubstringMatches(var8, "other")) {
               if (var11 == 0) {
                  var11 = var1;
                  if (var9 != null && var9.equals("other")) {
                     var10 = true;
                  }
               }
            } else {
               if (var9 == null) {
                  var9 = var2.select(var3 - var6);
                  if (var11 != 0 && var9.equals("other")) {
                     var10 = true;
                  }
               }

               if (!var10 && var0.partSubstringMatches(var8, var9)) {
                  var11 = var1;
                  var10 = true;
               }
            }
         }

         var1 = var0.getLimitPartIndex(var1);
         ++var1;
      } while(var1 < var5);

      return var11;
   }

   public final String format(double var1) {
      if (this.msgPattern != null && this.msgPattern.countParts() != 0) {
         int var3 = findSubMessage(this.msgPattern, 0, this.pluralRulesWrapper, var1);
         var1 -= this.offset;
         StringBuilder var4 = null;
         int var5 = this.msgPattern.getPart(var3).getLimit();

         while(true) {
            while(true) {
               ++var3;
               MessagePattern.Part var6 = this.msgPattern.getPart(var3);
               MessagePattern.Part.Type var7 = var6.getType();
               int var8 = var6.getIndex();
               if (var7 == MessagePattern.Part.Type.MSG_LIMIT) {
                  if (var4 == null) {
                     return this.pattern.substring(var5, var8);
                  }

                  return var4.append(this.pattern, var5, var8).toString();
               }

               if (var7 != MessagePattern.Part.Type.REPLACE_NUMBER && (var7 != MessagePattern.Part.Type.SKIP_SYNTAX || !this.msgPattern.jdkAposMode())) {
                  if (var7 == MessagePattern.Part.Type.ARG_START) {
                     if (var4 == null) {
                        var4 = new StringBuilder();
                     }

                     var4.append(this.pattern, var5, var8);
                     var5 = var8;
                     var3 = this.msgPattern.getLimitPartIndex(var3);
                     var8 = this.msgPattern.getPart(var3).getLimit();
                     MessagePattern.appendReducedApostrophes(this.pattern, var5, var8, var4);
                     var5 = var8;
                  }
               } else {
                  if (var4 == null) {
                     var4 = new StringBuilder();
                  }

                  var4.append(this.pattern, var5, var8);
                  if (var7 == MessagePattern.Part.Type.REPLACE_NUMBER) {
                     var4.append(this.numberFormat.format(var1));
                  }

                  var5 = var6.getLimit();
               }
            }
         }
      } else {
         return this.numberFormat.format(var1);
      }
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      if (var1 instanceof Number) {
         var2.append(this.format(((Number)var1).doubleValue()));
         return var2;
      } else {
         throw new IllegalArgumentException("'" + var1 + "' is not a Number");
      }
   }

   public Number parse(String var1, ParsePosition var2) {
      throw new UnsupportedOperationException();
   }

   public Object parseObject(String var1, ParsePosition var2) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   public void setLocale(ULocale var1) {
      if (var1 == null) {
         var1 = ULocale.getDefault(ULocale.Category.FORMAT);
      }

      this.init((PluralRules)null, PluralRules.PluralType.CARDINAL, var1);
   }

   public void setNumberFormat(NumberFormat var1) {
      this.numberFormat = var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         PluralFormat var2 = (PluralFormat)var1;
         return Utility.objectEquals(this.ulocale, var2.ulocale) && Utility.objectEquals(this.pluralRules, var2.pluralRules) && Utility.objectEquals(this.msgPattern, var2.msgPattern) && Utility.objectEquals(this.numberFormat, var2.numberFormat);
      } else {
         return false;
      }
   }

   public boolean equals(PluralFormat var1) {
      return this.equals((Object)var1);
   }

   public int hashCode() {
      return this.pluralRules.hashCode() ^ this.parsedValues.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("locale=" + this.ulocale);
      var1.append(", rules='" + this.pluralRules + "'");
      var1.append(", pattern='" + this.pattern + "'");
      var1.append(", format='" + this.numberFormat + "'");
      return var1.toString();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this.pluralRulesWrapper = new PluralFormat.PluralSelectorAdapter(this);
      this.parsedValues = null;
      if (this.pattern != null) {
         this.applyPattern(this.pattern);
      }

   }

   static PluralRules access$000(PluralFormat var0) {
      return var0.pluralRules;
   }

   private final class PluralSelectorAdapter implements PluralFormat.PluralSelector {
      final PluralFormat this$0;

      private PluralSelectorAdapter(PluralFormat var1) {
         this.this$0 = var1;
      }

      public String select(double var1) {
         return PluralFormat.access$000(this.this$0).select(var1);
      }

      PluralSelectorAdapter(PluralFormat var1, Object var2) {
         this(var1);
      }
   }

   interface PluralSelector {
      String select(double var1);
   }
}
