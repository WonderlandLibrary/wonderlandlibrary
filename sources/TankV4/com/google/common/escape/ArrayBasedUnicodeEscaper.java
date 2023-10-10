package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public abstract class ArrayBasedUnicodeEscaper extends UnicodeEscaper {
   private final char[][] replacements;
   private final int replacementsLength;
   private final int safeMin;
   private final int safeMax;
   private final char safeMinChar;
   private final char safeMaxChar;

   protected ArrayBasedUnicodeEscaper(Map var1, int var2, int var3, @Nullable String var4) {
      this(ArrayBasedEscaperMap.create(var1), var2, var3, var4);
   }

   protected ArrayBasedUnicodeEscaper(ArrayBasedEscaperMap var1, int var2, int var3, @Nullable String var4) {
      Preconditions.checkNotNull(var1);
      this.replacements = var1.getReplacementArray();
      this.replacementsLength = this.replacements.length;
      if (var3 < var2) {
         var3 = -1;
         var2 = Integer.MAX_VALUE;
      }

      this.safeMin = var2;
      this.safeMax = var3;
      if (var2 >= 55296) {
         this.safeMinChar = '\uffff';
         this.safeMaxChar = 0;
      } else {
         this.safeMinChar = (char)var2;
         this.safeMaxChar = (char)Math.min(var3, 55295);
      }

   }

   public final String escape(String var1) {
      Preconditions.checkNotNull(var1);

      for(int var2 = 0; var2 < var1.length(); ++var2) {
         char var3 = var1.charAt(var2);
         if (var3 < this.replacementsLength && this.replacements[var3] != null || var3 > this.safeMaxChar || var3 < this.safeMinChar) {
            return this.escapeSlow(var1, var2);
         }
      }

      return var1;
   }

   protected final int nextEscapeIndex(CharSequence var1, int var2, int var3) {
      while(true) {
         if (var2 < var3) {
            char var4 = var1.charAt(var2);
            if ((var4 >= this.replacementsLength || this.replacements[var4] == null) && var4 <= this.safeMaxChar && var4 >= this.safeMinChar) {
               ++var2;
               continue;
            }
         }

         return var2;
      }
   }

   protected final char[] escape(int var1) {
      if (var1 < this.replacementsLength) {
         char[] var2 = this.replacements[var1];
         if (var2 != null) {
            return var2;
         }
      }

      return var1 >= this.safeMin && var1 <= this.safeMax ? null : this.escapeUnsafe(var1);
   }

   protected abstract char[] escapeUnsafe(int var1);
}
