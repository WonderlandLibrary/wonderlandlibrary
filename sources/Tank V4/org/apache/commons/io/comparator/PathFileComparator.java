package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.IOCase;

public class PathFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator PATH_COMPARATOR = new PathFileComparator();
   public static final Comparator PATH_REVERSE;
   public static final Comparator PATH_INSENSITIVE_COMPARATOR;
   public static final Comparator PATH_INSENSITIVE_REVERSE;
   public static final Comparator PATH_SYSTEM_COMPARATOR;
   public static final Comparator PATH_SYSTEM_REVERSE;
   private final IOCase caseSensitivity;

   public PathFileComparator() {
      this.caseSensitivity = IOCase.SENSITIVE;
   }

   public PathFileComparator(IOCase var1) {
      this.caseSensitivity = var1 == null ? IOCase.SENSITIVE : var1;
   }

   public int compare(File var1, File var2) {
      return this.caseSensitivity.checkCompareTo(var1.getPath(), var2.getPath());
   }

   public String toString() {
      return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
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
      PATH_REVERSE = new ReverseComparator(PATH_COMPARATOR);
      PATH_INSENSITIVE_COMPARATOR = new PathFileComparator(IOCase.INSENSITIVE);
      PATH_INSENSITIVE_REVERSE = new ReverseComparator(PATH_INSENSITIVE_COMPARATOR);
      PATH_SYSTEM_COMPARATOR = new PathFileComparator(IOCase.SYSTEM);
      PATH_SYSTEM_REVERSE = new ReverseComparator(PATH_SYSTEM_COMPARATOR);
   }
}
