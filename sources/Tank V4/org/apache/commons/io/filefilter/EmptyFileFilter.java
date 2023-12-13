package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class EmptyFileFilter extends AbstractFileFilter implements Serializable {
   public static final IOFileFilter EMPTY = new EmptyFileFilter();
   public static final IOFileFilter NOT_EMPTY;

   protected EmptyFileFilter() {
   }

   public boolean accept(File var1) {
      if (!var1.isDirectory()) {
         return var1.length() == 0L;
      } else {
         File[] var2 = var1.listFiles();
         return var2 == null || var2.length == 0;
      }
   }

   static {
      NOT_EMPTY = new NotFileFilter(EMPTY);
   }
}
