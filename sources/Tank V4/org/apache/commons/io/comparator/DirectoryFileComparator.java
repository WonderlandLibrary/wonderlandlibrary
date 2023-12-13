package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class DirectoryFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator DIRECTORY_COMPARATOR = new DirectoryFileComparator();
   public static final Comparator DIRECTORY_REVERSE;

   public int compare(File var1, File var2) {
      return this.getType(var1) - this.getType(var2);
   }

   private int getType(File var1) {
      return var1.isDirectory() ? 1 : 2;
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
      DIRECTORY_REVERSE = new ReverseComparator(DIRECTORY_COMPARATOR);
   }
}
