package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.Serializable;

public class DelegateFileFilter extends AbstractFileFilter implements Serializable {
   private final FilenameFilter filenameFilter;
   private final FileFilter fileFilter;

   public DelegateFileFilter(FilenameFilter var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The FilenameFilter must not be null");
      } else {
         this.filenameFilter = var1;
         this.fileFilter = null;
      }
   }

   public DelegateFileFilter(FileFilter var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The FileFilter must not be null");
      } else {
         this.fileFilter = var1;
         this.filenameFilter = null;
      }
   }

   public boolean accept(File var1) {
      return this.fileFilter != null ? this.fileFilter.accept(var1) : super.accept(var1);
   }

   public boolean accept(File var1, String var2) {
      return this.filenameFilter != null ? this.filenameFilter.accept(var1, var2) : super.accept(var1, var2);
   }

   public String toString() {
      String var1 = this.fileFilter != null ? this.fileFilter.toString() : this.filenameFilter.toString();
      return super.toString() + "(" + var1 + ")";
   }
}
