package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Map;

@Beta
@GwtCompatible
public abstract class ArrayBasedCharEscaper extends CharEscaper {
   private final char[][] replacements;
   private final int replacementsLength;
   private final char safeMin;
   private final char safeMax;

   protected ArrayBasedCharEscaper(Map var1, char var2, char var3) {
      this(ArrayBasedEscaperMap.create(var1), var2, var3);
   }

   protected ArrayBasedCharEscaper(ArrayBasedEscaperMap var1, char var2, char var3) {
      Preconditions.checkNotNull(var1);
      this.replacements = var1.getReplacementArray();
      this.replacementsLength = this.replacements.length;
      if (var3 < var2) {
         var3 = 0;
         var2 = '\uffff';
      }

      this.safeMin = var2;
      this.safeMax = var3;
   }

   public final String escape(String var1) {
      Preconditions.checkNotNull(var1);

      for(int var2 = 0; var2 < var1.length(); ++var2) {
         char var3 = var1.charAt(var2);
         if (var3 < this.replacementsLength && this.replacements[var3] != null || var3 > this.safeMax || var3 < this.safeMin) {
            return this.escapeSlow(var1, var2);
         }
      }

      return var1;
   }

   protected final char[] escape(char var1) {
      if (var1 < this.replacementsLength) {
         char[] var2 = this.replacements[var1];
         if (var2 != null) {
            return var2;
         }
      }

      return var1 >= this.safeMin && var1 <= this.safeMax ? null : this.escapeUnsafe(var1);
   }

   protected abstract char[] escapeUnsafe(char var1);
}
