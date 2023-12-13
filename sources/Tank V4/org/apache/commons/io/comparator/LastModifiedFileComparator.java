package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class LastModifiedFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator LASTMODIFIED_COMPARATOR = new LastModifiedFileComparator();
   public static final Comparator LASTMODIFIED_REVERSE;

   public int compare(File var1, File var2) {
      long var3 = var1.lastModified() - var2.lastModified();
      if (var3 < 0L) {
         return -1;
      } else {
         return var3 > 0L ? 1 : 0;
      }
   }

   public String toString() {
      return super.toString();
   }

   public List sort(List var1) {
      return super.sort(var1);
   }

   public File[] sort(File[] var1) {
      return super.sort(var1);
   }

   public int compare(Object var1, Object var2) {
      return this.compare((File)var1, (File)var2);
   }

   static {
      LASTMODIFIED_REVERSE = new ReverseComparator(LASTMODIFIED_COMPARATOR);
   }
}
