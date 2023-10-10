package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class SizeFileFilter extends AbstractFileFilter implements Serializable {
   private final long size;
   private final boolean acceptLarger;

   public SizeFileFilter(long var1) {
      this(var1, true);
   }

   public SizeFileFilter(long var1, boolean var3) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("The size must be non-negative");
      } else {
         this.size = var1;
         this.acceptLarger = var3;
      }
   }

   public boolean accept(File var1) {
      boolean var2 = var1.length() < this.size;
      return this.acceptLarger ? !var2 : var2;
   }

   public String toString() {
      String var1 = this.acceptLarger ? ">=" : "<";
      return super.toString() + "(" + var1 + this.size + ")";
   }
}
