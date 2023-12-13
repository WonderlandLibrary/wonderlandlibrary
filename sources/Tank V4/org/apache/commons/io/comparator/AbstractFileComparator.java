package org.apache.commons.io.comparator;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

abstract class AbstractFileComparator implements Comparator {
   public File[] sort(File... var1) {
      if (var1 != null) {
         Arrays.sort(var1, this);
      }

      return var1;
   }

   public List sort(List var1) {
      if (var1 != null) {
         Collections.sort(var1, this);
      }

      return var1;
   }

   public String toString() {
      return this.getClass().getSimpleName();
   }
}
