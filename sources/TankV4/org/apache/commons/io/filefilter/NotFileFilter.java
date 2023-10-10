package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class NotFileFilter extends AbstractFileFilter implements Serializable {
   private final IOFileFilter filter;

   public NotFileFilter(IOFileFilter var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The filter must not be null");
      } else {
         this.filter = var1;
      }
   }

   public boolean accept(File var1) {
      return !this.filter.accept(var1);
   }

   public boolean accept(File var1, String var2) {
      return !this.filter.accept(var1, var2);
   }

   public String toString() {
      return super.toString() + "(" + this.filter.toString() + ")";
   }
}
