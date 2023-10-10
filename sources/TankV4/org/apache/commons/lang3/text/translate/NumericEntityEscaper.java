package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class NumericEntityEscaper extends CodePointTranslator {
   private final int below;
   private final int above;
   private final boolean between;

   private NumericEntityEscaper(int var1, int var2, boolean var3) {
      this.below = var1;
      this.above = var2;
      this.between = var3;
   }

   public NumericEntityEscaper() {
      this(0, Integer.MAX_VALUE, true);
   }

   public static NumericEntityEscaper below(int var0) {
      return outsideOf(var0, Integer.MAX_VALUE);
   }

   public static NumericEntityEscaper above(int var0) {
      return outsideOf(0, var0);
   }

   public static NumericEntityEscaper between(int var0, int var1) {
      return new NumericEntityEscaper(var0, var1, true);
   }

   public static NumericEntityEscaper outsideOf(int var0, int var1) {
      return new NumericEntityEscaper(var0, var1, false);
   }

   public boolean translate(int var1, Writer var2) throws IOException {
      if (this.between) {
         if (var1 < this.below || var1 > this.above) {
            return false;
         }
      } else if (var1 >= this.below && var1 <= this.above) {
         return false;
      }

      var2.write("&#");
      var2.write(Integer.toString(var1, 10));
      var2.write(59);
      return true;
   }
}
