package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.io.FileUtils;

public class AgeFileFilter extends AbstractFileFilter implements Serializable {
   private final long cutoff;
   private final boolean acceptOlder;

   public AgeFileFilter(long var1) {
      this(var1, true);
   }

   public AgeFileFilter(long var1, boolean var3) {
      this.acceptOlder = var3;
      this.cutoff = var1;
   }

   public AgeFileFilter(Date var1) {
      this(var1, true);
   }

   public AgeFileFilter(Date var1, boolean var2) {
      this(var1.getTime(), var2);
   }

   public AgeFileFilter(File var1) {
      this(var1, true);
   }

   public AgeFileFilter(File var1, boolean var2) {
      this(var1.lastModified(), var2);
   }

   public boolean accept(File var1) {
      boolean var2 = FileUtils.isFileNewer(var1, this.cutoff);
      return this.acceptOlder ? !var2 : var2;
   }

   public String toString() {
      String var1 = this.acceptOlder ? "<=" : ">";
      return super.toString() + "(" + var1 + this.cutoff + ")";
   }
}
