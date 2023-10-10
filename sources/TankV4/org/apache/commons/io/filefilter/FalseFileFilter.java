package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class FalseFileFilter implements IOFileFilter, Serializable {
   public static final IOFileFilter FALSE = new FalseFileFilter();
   public static final IOFileFilter INSTANCE;

   protected FalseFileFilter() {
   }

   public boolean accept(File var1) {
      return false;
   }

   public boolean accept(File var1, String var2) {
      return false;
   }

   static {
      INSTANCE = FALSE;
   }
}
