package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;

public class WildcardFileFilter extends AbstractFileFilter implements Serializable {
   private final String[] wildcards;
   private final IOCase caseSensitivity;

   public WildcardFileFilter(String var1) {
      this((String)var1, (IOCase)null);
   }

   public WildcardFileFilter(String var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard must not be null");
      } else {
         this.wildcards = new String[]{var1};
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public WildcardFileFilter(String[] var1) {
      this((String[])var1, (IOCase)null);
   }

   public WildcardFileFilter(String[] var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard array must not be null");
      } else {
         this.wildcards = new String[var1.length];
         System.arraycopy(var1, 0, this.wildcards, 0, var1.length);
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public WildcardFileFilter(List var1) {
      this((List)var1, (IOCase)null);
   }

   public WildcardFileFilter(List var1, IOCase var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The wildcard list must not be null");
      } else {
         this.wildcards = (String[])var1.toArray(new String[var1.size()]);
         this.caseSensitivity = var2 == null ? IOCase.SENSITIVE : var2;
      }
   }

   public boolean accept(File var1, String var2) {
      String[] var3 = this.wildcards;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (FilenameUtils.wildcardMatch(var2, var6, this.caseSensitivity)) {
            return true;
         }
      }

      return false;
   }

   public boolean accept(File var1) {
      String var2 = var1.getName();
      String[] var3 = this.wildcards;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (FilenameUtils.wildcardMatch(var2, var6, this.caseSensitivity)) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("(");
      if (this.wildcards != null) {
         for(int var2 = 0; var2 < this.wildcards.length; ++var2) {
            if (var2 > 0) {
               var1.append(",");
            }

            var1.append(this.wildcards[var2]);
         }
      }

      var1.append(")");
      return var1.toString();
   }
}
