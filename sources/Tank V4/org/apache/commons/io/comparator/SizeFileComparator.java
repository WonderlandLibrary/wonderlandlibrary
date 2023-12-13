package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class SizeFileComparator extends AbstractFileComparator implements Serializable {
   public static final Comparator SIZE_COMPARATOR = new SizeFileComparator();
   public static final Comparator SIZE_REVERSE;
   public static final Comparator SIZE_SUMDIR_COMPARATOR;
   public static final Comparator SIZE_SUMDIR_REVERSE;
   private final boolean sumDirectoryContents;

   public SizeFileComparator() {
      this.sumDirectoryContents = false;
   }

   public SizeFileComparator(boolean var1) {
      this.sumDirectoryContents = var1;
   }

   public int compare(File var1, File var2) {
      long var3 = 0L;
      if (var1.isDirectory()) {
         var3 = this.sumDirectoryContents && var1.exists() ? FileUtils.sizeOfDirectory(var1) : 0L;
      } else {
         var3 = var1.length();
      }

      long var5 = 0L;
      if (var2.isDirectory()) {
         var5 = this.sumDirectoryContents && var2.exists() ? FileUtils.sizeOfDirectory(var2) : 0L;
      } else {
         var5 = var2.length();
      }

      long var7 = var3 - var5;
      if (var7 < 0L) {
         return -1;
      } else {
         return var7 > 0L ? 1 : 0;
      }
   }

   public String toString() {
      return super.toString() + "[sumDirectoryContents=" + this.sumDirectoryContents + "]";
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
      SIZE_REVERSE = new ReverseComparator(SIZE_COMPARATOR);
      SIZE_SUMDIR_COMPARATOR = new SizeFileComparator(true);
      SIZE_SUMDIR_REVERSE = new ReverseComparator(SIZE_SUMDIR_COMPARATOR);
   }
}
