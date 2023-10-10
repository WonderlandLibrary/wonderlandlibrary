package org.apache.commons.io.filefilter;

import java.io.File;

public abstract class AbstractFileFilter implements IOFileFilter {
   public boolean accept(File var1) {
      return this.accept(var1.getParentFile(), var1.getName());
   }

   public boolean accept(File var1, String var2) {
      return this.accept(new File(var1, var2));
   }

   public String toString() {
      return this.getClass().getSimpleName();
   }
}
