package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class SelectFormat extends Format {
   private static final long serialVersionUID = 2993154333257524984L;
   private String pattern = null;
   private transient MessagePattern msgPattern;
   static final boolean $assertionsDisabled = !SelectFormat.class.desiredAssertionStatus();

   public SelectFormat(String var1) {
      this.applyPattern(var1);
   }

   private void reset() {
      this.pattern = null;
      if (this.msgPattern != null) {
         this.msgPattern.clear();
      }

   }

   public void applyPattern(String var1) {
      this.pattern = var1;
      if (this.msgPattern == null) {
         this.msgPattern = new MessagePattern();
      }

      try {
         this.msgPattern.parseSelectStyle(var1);
      } catch (RuntimeException var3) {
         this.reset();
         throw var3;
      }
   }

   public String toPattern() {
      return this.pattern;
   }

   static int findSubMessage(MessagePattern var0, int var1, String var2) {
      int var3 = var0.countParts();
      int var4 = 0;

      while(true) {
         MessagePattern.Part var5 = var0.getPart(var1++);
         MessagePattern.Part.Type var6 = var5.getType();
         if (var6 != MessagePattern.Part.Type.ARG_LIMIT) {
            if (!$assertionsDisabled && var6 != MessagePattern.Part.Type.ARG_SELECTOR) {
               throw new AssertionError();
            }

            if (var0.partSubstringMatches(var5, var2)) {
               return var1;
            }

            if (var4 == 0 && var0.partSubstringMatches(var5, "other")) {
               var4 = var1;
            }

            var1 = var0.getLimitPartIndex(var1);
            ++var1;
            if (var1 < var3) {
               continue;
            }
         }

         return var4;
      }
   }

   public final String format(String var1) {
      if (!PatternProps.isIdentifier(var1)) {
         throw new IllegalArgumentException("Invalid formatting argument.");
      } else if (this.msgPattern != null && this.msgPattern.countParts() != 0) {
         int var2 = findSubMessage(this.msgPattern, 0, var1);
         if (!this.msgPattern.jdkAposMode()) {
            int var9 = this.msgPattern.getLimitPartIndex(var2);
            return this.msgPattern.getPatternString().substring(this.msgPattern.getPart(var2).getLimit(), this.msgPattern.getPatternIndex(var9));
         } else {
            StringBuilder var3 = null;
            int var4 = this.msgPattern.getPart(var2).getLimit();
            int var5 = var2;

            while(true) {
               ++var5;
               MessagePattern.Part var6 = this.msgPattern.getPart(var5);
               MessagePattern.Part.Type var7 = var6.getType();
               int var8 = var6.getIndex();
               if (var7 == MessagePattern.Part.Type.MSG_LIMIT) {
                  if (var3 == null) {
                     return this.pattern.substring(var4, var8);
                  }

                  return var3.append(this.pattern, var4, var8).toString();
               }

               if (var7 == MessagePattern.Part.Type.SKIP_SYNTAX) {
                  if (var3 == null) {
                     var3 = new StringBuilder();
                  }

                  var3.append(this.pattern, var4, var8);
                  var4 = var6.getLimit();
               } else if (var7 == MessagePattern.Part.Type.ARG_START) {
                  if (var3 == null) {
                     var3 = new StringBuilder();
                  }

                  var3.append(this.pattern, var4, var8);
                  var4 = var8;
                  var5 = this.msgPattern.getLimitPartIndex(var5);
                  var8 = this.msgPattern.getPart(var5).getLimit();
                  MessagePattern.appendReducedApostrophes(this.pattern, var4, var8, var3);
                  var4 = var8;
               }
            }
         }
      } else {
         throw new IllegalStateException("Invalid format error.");
      }
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      if (var1 instanceof String) {
         var2.append(this.format((String)var1));
         return var2;
      } else {
         throw new IllegalArgumentException("'" + var1 + "' is not a String");
      }
   }

   public Object parseObject(String var1, ParsePosition var2) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         SelectFormat var2 = (SelectFormat)var1;
         return this.msgPattern == null ? var2.msgPattern == null : this.msgPattern.equals(var2.msgPattern);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.pattern != null ? this.pattern.hashCode() : 0;
   }

   public String toString() {
      return "pattern='" + this.pattern + "'";
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.pattern != null) {
         this.applyPattern(this.pattern);
      }

   }
}
