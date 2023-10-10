package org.apache.commons.io;

import java.io.Serializable;

public final class IOCase implements Serializable {
   public static final IOCase SENSITIVE = new IOCase("Sensitive", true);
   public static final IOCase INSENSITIVE = new IOCase("Insensitive", false);
   public static final IOCase SYSTEM = new IOCase("System", !FilenameUtils.isSystemWindows());
   private static final long serialVersionUID = -6343169151696340687L;
   private final String name;
   private final transient boolean sensitive;

   public static IOCase forName(String var0) {
      if (SENSITIVE.name.equals(var0)) {
         return SENSITIVE;
      } else if (INSENSITIVE.name.equals(var0)) {
         return INSENSITIVE;
      } else if (SYSTEM.name.equals(var0)) {
         return SYSTEM;
      } else {
         throw new IllegalArgumentException("Invalid IOCase name: " + var0);
      }
   }

   private IOCase(String var1, boolean var2) {
      this.name = var1;
      this.sensitive = var2;
   }

   private Object readResolve() {
      return forName(this.name);
   }

   public String getName() {
      return this.name;
   }

   public boolean isCaseSensitive() {
      return this.sensitive;
   }

   public int checkCompareTo(String var1, String var2) {
      if (var1 != null && var2 != null) {
         return this.sensitive ? var1.compareTo(var2) : var1.compareToIgnoreCase(var2);
      } else {
         throw new NullPointerException("The strings must not be null");
      }
   }

   public boolean checkEquals(String var1, String var2) {
      if (var1 != null && var2 != null) {
         return this.sensitive ? var1.equals(var2) : var1.equalsIgnoreCase(var2);
      } else {
         throw new NullPointerException("The strings must not be null");
      }
   }

   public boolean checkStartsWith(String var1, String var2) {
      return var1.regionMatches(!this.sensitive, 0, var2, 0, var2.length());
   }

   public boolean checkEndsWith(String var1, String var2) {
      int var3 = var2.length();
      return var1.regionMatches(!this.sensitive, var1.length() - var3, var2, 0, var3);
   }

   public int checkIndexOf(String var1, int var2, String var3) {
      int var4 = var1.length() - var3.length();
      if (var4 >= var2) {
         for(int var5 = var2; var5 <= var4; ++var5) {
            if (var3 == false) {
               return var5;
            }
         }
      }

      return -1;
   }

   public String toString() {
      return this.name;
   }
}
