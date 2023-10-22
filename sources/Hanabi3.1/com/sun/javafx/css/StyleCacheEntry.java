package com.sun.javafx.css;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.scene.text.Font;

public final class StyleCacheEntry {
   private Map calculatedValues;

   public CalculatedValue get(String var1) {
      CalculatedValue var2 = null;
      if (this.calculatedValues != null && !this.calculatedValues.isEmpty()) {
         var2 = (CalculatedValue)this.calculatedValues.get(var1);
      }

      return var2;
   }

   public void put(String var1, CalculatedValue var2) {
      if (this.calculatedValues == null) {
         this.calculatedValues = new HashMap(5);
      }

      this.calculatedValues.put(var1, var2);
   }

   public static final class Key {
      private final Set[] pseudoClassStates;
      private final double fontSize;
      private int hash = Integer.MIN_VALUE;

      public Key(Set[] var1, Font var2) {
         this.pseudoClassStates = new Set[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.pseudoClassStates[var3] = new PseudoClassState();
            this.pseudoClassStates[var3].addAll(var1[var3]);
         }

         this.fontSize = var2 != null ? var2.getSize() : Font.getDefault().getSize();
      }

      public String toString() {
         return Arrays.toString(this.pseudoClassStates) + ", " + this.fontSize;
      }

      public static int hashCode(double var0) {
         long var2 = Double.doubleToLongBits(var0);
         return (int)(var2 ^ var2 >>> 32);
      }

      public int hashCode() {
         if (this.hash == Integer.MIN_VALUE) {
            this.hash = hashCode(this.fontSize);
            int var1 = this.pseudoClassStates != null ? this.pseudoClassStates.length : 0;

            for(int var2 = 0; var2 < var1; ++var2) {
               Set var3 = this.pseudoClassStates[var2];
               if (var3 != null) {
                  this.hash = 67 * (this.hash + var3.hashCode());
               }
            }
         }

         return this.hash;
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (var1 != null && var1.getClass() == this.getClass()) {
            Key var2 = (Key)var1;
            if (this.hash != var2.hash) {
               return false;
            } else {
               double var3 = this.fontSize - var2.fontSize;
               if (!(var3 < -1.0E-6) && !(1.0E-6 < var3)) {
                  if (this.pseudoClassStates == null ^ var2.pseudoClassStates == null) {
                     return false;
                  } else if (this.pseudoClassStates == null) {
                     return true;
                  } else if (this.pseudoClassStates.length != var2.pseudoClassStates.length) {
                     return false;
                  } else {
                     int var5 = 0;

                     while(true) {
                        if (var5 >= this.pseudoClassStates.length) {
                           return true;
                        }

                        Set var6 = this.pseudoClassStates[var5];
                        Set var7 = var2.pseudoClassStates[var5];
                        if (var6 == null) {
                           if (var7 != null) {
                              break;
                           }
                        } else if (!var6.equals(var7)) {
                           break;
                        }

                        ++var5;
                     }

                     return false;
                  }
               } else {
                  return false;
               }
            }
         } else {
            return false;
         }
      }
   }
}
