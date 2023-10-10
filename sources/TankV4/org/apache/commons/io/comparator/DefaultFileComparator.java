package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class DefaultFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator DEFAULT_COMPARATOR = new DefaultFileComparator();
   public static final Comparator DEFAULT_REVERSE;

   public int compare(File var1, File var2) {
      return var1.compareTo(var2);
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
      DEFAULT_REVERSE = new ReverseComparator(DEFAULT_COMPARATOR);
   }
}
