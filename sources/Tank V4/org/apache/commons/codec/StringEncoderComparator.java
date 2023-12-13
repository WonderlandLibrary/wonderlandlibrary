package org.apache.commons.codec;

import java.util.Comparator;

public class StringEncoderComparator implements Comparator {
   private final StringEncoder stringEncoder;

   /** @deprecated */
   @Deprecated
   public StringEncoderComparator() {
      this.stringEncoder = null;
   }

   public StringEncoderComparator(StringEncoder var1) {
      this.stringEncoder = var1;
   }

   public int compare(Object var1, Object var2) {
      boolean var3 = false;

      int var7;
      try {
         Comparable var4 = (Comparable)this.stringEncoder.encode(var1);
         Comparable var5 = (Comparable)this.stringEncoder.encode(var2);
         var7 = var4.compareTo(var5);
      } catch (EncoderException var6) {
         var7 = 0;
      }

      return var7;
   }
}
