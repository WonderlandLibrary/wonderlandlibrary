package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;

public class SuffixFileFilter extends AbstractFileFilter implements Serializable {
   private final String[] suffixes;
   private final IOCase caseSensitivity;

   public SuffixFileFilter(String var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public SuffixFileFilter(String var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The suffix must not be null");
      } else {
         this.suffixes = new String[]{var1};
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public SuffixFileFilter(String[] var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public SuffixFileFilter(String[] var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The array of suffixes must not be null");
      } else {
         this.suffixes = new String[var1.length];
         System.arraycopy(var1, 0, this.suffixes, 0, var1.length);
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public SuffixFileFilter(List var1) {
      this(var1, IOCase.SENSITIVE);
   }

   public SuffixFileFilter(List var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The list of suffixes must not be null");
      } else {
         this.suffixes = (String[])var1.toArray(new String[var1.size()]);
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public boolean accept(File var1) {
      String var2 = var1.getName();
      String[] var3 = this.suffixes;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (this.caseSensitivity.checkEndsWith(var2, var6)) {
            return true;
         }
      }

      return false;
   }

   public boolean accept(File var1, String var2) {
      String[] var3 = this.suffixes;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (this.caseSensitivity.checkEndsWith(var2, var6)) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("(");
      if (this.suffixes != null) {
         for(int var2 = 0; var2 < this.suffixes.length; ++var2) {
            if (var2 > 0) {
               var1.append(",");
            }

            var1.append(this.suffixes[var2]);
         }
      }

      var1.append(")");
      return var1.toString();
   }
}
