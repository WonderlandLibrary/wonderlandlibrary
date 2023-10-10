package com.ibm.icu.util;

import com.ibm.icu.lang.UCharacter;

public class CaseInsensitiveString {
   private String string;
   private int hash = 0;
   private String folded = null;

   private static String foldCase(String var0) {
      return UCharacter.foldCase(var0, true);
   }

   private void getFolded() {
      if (this.folded == null) {
         this.folded = foldCase(this.string);
      }

   }

   public CaseInsensitiveString(String var1) {
      this.string = var1;
   }

   public String getString() {
      return this.string;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         this.getFolded();

         try {
            CaseInsensitiveString var2 = (CaseInsensitiveString)var1;
            var2.getFolded();
            return this.folded.equals(var2.folded);
         } catch (ClassCastException var5) {
            try {
               String var3 = (String)var1;
               return this.folded.equals(foldCase(var3));
            } catch (ClassCastException var4) {
               return false;
            }
         }
      }
   }

   public int hashCode() {
      this.getFolded();
      if (this.hash == 0) {
         this.hash = this.folded.hashCode();
      }

      return this.hash;
   }

   public String toString() {
      return this.string;
   }
}
