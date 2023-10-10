package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class HiddenFileFilter extends AbstractFileFilter implements Serializable {
   public static final IOFileFilter HIDDEN = new HiddenFileFilter();
   public static final IOFileFilter VISIBLE;

   protected HiddenFileFilter() {
   }

   public boolean accept(File var1) {
      return var1.isHidden();
   }

   static {
      VISIBLE = new NotFileFilter(HIDDEN);
   }
}
