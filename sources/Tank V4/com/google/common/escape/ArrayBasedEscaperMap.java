package com.google.common.escape;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

@Beta
@GwtCompatible
public final class ArrayBasedEscaperMap {
   private final char[][] replacementArray;
   private static final char[][] EMPTY_REPLACEMENT_ARRAY = new char[0][0];

   public static ArrayBasedEscaperMap create(Map var0) {
      return new ArrayBasedEscaperMap(createReplacementArray(var0));
   }

   private ArrayBasedEscaperMap(char[][] var1) {
      this.replacementArray = var1;
   }

   char[][] getReplacementArray() {
      return this.replacementArray;
   }

   @VisibleForTesting
   static char[][] createReplacementArray(Map var0) {
      Preconditions.checkNotNull(var0);
      if (var0.isEmpty()) {
         return EMPTY_REPLACEMENT_ARRAY;
      } else {
         char var1 = (Character)Collections.max(var0.keySet());
         char[][] var2 = new char[var1 + 1][];

         char var4;
         for(Iterator var3 = var0.keySet().iterator(); var3.hasNext(); var2[var4] = ((String)var0.get(var4)).toCharArray()) {
            var4 = (Character)var3.next();
         }

         return var2;
      }
   }
}
