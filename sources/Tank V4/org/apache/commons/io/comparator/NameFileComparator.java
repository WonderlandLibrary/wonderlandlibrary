package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.IOCase;

public class NameFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator NAME_COMPARATOR = new NameFileComparator();
   public static final Comparator NAME_REVERSE;
   public static final Comparator NAME_INSENSITIVE_COMPARATOR;
   public static final Comparator NAME_INSENSITIVE_REVERSE;
   public static final Comparator NAME_SYSTEM_COMPARATOR;
   public static final Comparator NAME_SYSTEM_REVERSE;
   private final IOCase caseSensitivity;

   public NameFileComparator() {
      this.caseSensitivity = IOCase.SENSITIVE;
   }

   public NameFileComparator(IOCase var1) {
      this.caseSensitivity = var1 == null ? IOCase.SENSITIVE : var1;
   }

   public int compare(File var1, File var2) {
      return this.caseSensitivity.checkCompareTo(var1.getName(), var2.getName());
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
      NAME_REVERSE = new ReverseComparator(NAME_COMPARATOR);
      NAME_INSENSITIVE_COMPARATOR = new NameFileComparator(IOCase.INSENSITIVE);
      NAME_INSENSITIVE_REVERSE = new ReverseComparator(NAME_INSENSITIVE_COMPARATOR);
      NAME_SYSTEM_COMPARATOR = new NameFileComparator(IOCase.SYSTEM);
      NAME_SYSTEM_REVERSE = new ReverseComparator(NAME_SYSTEM_COMPARATOR);
   }
}
