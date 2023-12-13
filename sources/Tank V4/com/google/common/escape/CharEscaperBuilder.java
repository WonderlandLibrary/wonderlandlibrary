package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@Beta
@GwtCompatible
public final class CharEscaperBuilder {
   private final Map map = new HashMap();
   private int max = -1;

   public CharEscaperBuilder addEscape(char var1, String var2) {
      this.map.put(var1, Preconditions.checkNotNull(var2));
      if (var1 > this.max) {
         this.max = var1;
      }

      return this;
   }

   public CharEscaperBuilder addEscapes(char[] var1, String var2) {
      Preconditions.checkNotNull(var2);
      char[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char var6 = var3[var5];
         this.addEscape(var6, var2);
      }

      return this;
   }

   public char[][] toArray() {
      char[][] var1 = new char[this.max + 1][];

      Entry var3;
      for(Iterator var2 = this.map.entrySet().iterator(); var2.hasNext(); var1[(Character)var3.getKey()] = ((String)var3.getValue()).toCharArray()) {
         var3 = (Entry)var2.next();
      }

      return var1;
   }

   public Escaper toEscaper() {
      return new CharEscaperBuilder.CharArrayDecorator(this.toArray());
   }

   private static class CharArrayDecorator extends CharEscaper {
      private final char[][] replacements;
      private final int replaceLength;

      CharArrayDecorator(char[][] var1) {
         this.replacements = var1;
         this.replaceLength = var1.length;
      }

      public String escape(String var1) {
         int var2 = var1.length();

         for(int var3 = 0; var3 < var2; ++var3) {
            char var4 = var1.charAt(var3);
            if (var4 < this.replacements.length && this.replacements[var4] != null) {
               return this.escapeSlow(var1, var3);
            }
         }

         return var1;
      }

      protected char[] escape(char var1) {
         return var1 < this.replaceLength ? this.replacements[var1] : null;
      }
   }
}
